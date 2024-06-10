package org.eugene.server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eugene.common.Environment;
import org.eugene.common.commands.*;
import org.eugene.common.exceptions.CommandExecutingException;
import org.eugene.common.modelCSV.SpaceMarine;
import org.eugene.common.network.Request;
import org.eugene.common.network.Response;
import org.eugene.common.network.Status;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class CommandInvoker implements CommandVisitor {
    private final Logger logger = LogManager.getLogger(CommandInvoker.class);
    private final CollectionManager collectionManager;
    private final DBManager db;

    public CommandInvoker(CollectionManager collectionManager, DBManager db) {
        this.collectionManager = collectionManager;
        this.db = db;
    }

    private synchronized Response invoke(InvocationLogic logic) {
        updateCollectionFromDB();
        try {
            return logic.execute();
        } catch (Exception e) {
            logger.error(e);
            return new Response(Status.ERROR, e.getMessage());
        } finally {
            updateCollectionFromDB();
        }
    }

    public Response visit(ClearCommand clear) {
        return invoke(() -> {
            try {
                db.clear(clear.getUserCredentials().username());
            } catch (SQLException e) {
                throw new CommandExecutingException("" + e);
            }
            collectionManager.clearCollection();
            return new Response(Status.OK);
        });
    }

    @Override
    public Response visit(SortCommand sort) {
        return invoke(() -> {
            collectionManager.sortCollection();
            return new Response(Status.OK);
        });
    }

    @Override
    public Response visit(UpdateIdCommand updateId, Request request) {
        return invoke(() -> {
            var id = Integer.parseInt(request.getArgs().get(0));
            try {
                db.updateElementById(id, request.getSpaceMarine(), request.getUserCredentials());
            } catch (CommandExecutingException e) {
                return new Response(Status.ERROR, e.getMessage());
            }
            return new Response(Status.OK);
        });
    }

    @Override
    public Response visit(RemoveByIdCommand removeById, Request request) {
        return invoke(() -> {
            var id = Integer.parseInt(request.getArgs().get(0));

            try {
                db.removeById(id, request.getUserCredentials());
                return new Response(Status.OK);
            } catch (CommandExecutingException e) {
                return new Response(Status.ERROR, e.getMessage());
            }
        });
    }

    @Override
    public Response visit(RemoveAtCommand removeAt, Request request) {
        ;
        return invoke(() -> {
            try {
                var index = Integer.parseInt(request.getArgs().get(0));
                var col = collectionManager.getCollection();
                return new Response(Status.OK);
            } catch (ArrayIndexOutOfBoundsException e) {
                return new Response(Status.ERROR, "No item with such index");
            } catch (NumberFormatException e) {
                return new Response(Status.ERROR, "Index must be an integer");
            }
        });

    }

    @Override
    public Response visit(AddIfMaxCommand addIfMax, Request request) {
        visit(new AddCommand(), request);
        return null;
    }

    @Override
    public Response visit(InfoCommand info) {
        return invoke(() -> {
            String data;
            data = "Collection type: " + collectionManager.getCollection().getClass() +
                    "\nCollection size: " + collectionManager.getCollection().size() +
                    "\nCollection initialization date: " + collectionManager.getInitDate();
            return new Response(Status.OK, data);
        });
    }

    private void updateCollectionFromDB() {
        try {
            collectionManager.setCollection(db.readCollection());
        } catch (SQLException e) {
            throw new CommandExecutingException(e.getMessage());
        }
    }

    @Override
    public Response visit(ShowCommand show) {
        return invoke(() -> {
            String data;
            data = "[STORED DATA]:\n" + collectionManager.getCollection().stream()
                    .map(SpaceMarine::toString)
                    .collect(Collectors.joining("\n"));
            return new Response(Status.OK, data);
        });
    }

    @Override
    public Response visit(HelpCommand help) {
        return invoke(() -> {
            String data = "[AVAILABLE COMMANDS]:\n" + Environment.getAvailableCommands().values().stream()
                    .map(Command::getHelp)
                    .collect(Collectors.joining("\n"));
            return new Response(Status.OK, data);
        });
    }

    @Override
    public Response visit(AddCommand add, Request request) {
        return invoke(() -> {
            try {
                db.addSpaceMarine(request.getSpaceMarine(), request.getUserCredentials());
                return new Response(Status.OK, "Weapon added successfully");
            } catch (SQLException e) {
                throw new CommandExecutingException(e.getMessage());
            }
        });
    }

    @FunctionalInterface
    private interface InvocationLogic {
        Response execute();
    }
}
