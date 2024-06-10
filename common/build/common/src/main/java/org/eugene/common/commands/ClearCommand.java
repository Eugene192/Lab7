package org.eugene.common.commands;

import org.eugene.common.network.Request;
import org.eugene.common.network.Response;

import java.util.ArrayList;

public class ClearCommand extends Command {
    public static String name = "clear";

    public ClearCommand() {
        super(ClearCommand.name, "clears the collection");
    }

    @Override
    public Request buildRequest(ArrayList<String> args) {
        return new Request(name);
    }

    @Override
    public Response accept(CommandVisitor visitor, Request request) {
        return visitor.visit(this);
    }
}
