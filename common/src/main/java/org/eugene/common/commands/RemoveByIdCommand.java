package org.eugene.common.commands;

import org.eugene.common.network.Request;
import org.eugene.common.network.Response;

import java.util.ArrayList;

public class RemoveByIdCommand extends Command {
    public static final String NAME = "remove_by_id";

    public RemoveByIdCommand() {
        super(NAME, "remove element from collection by id");
    }

    @Override
    public Request buildRequest(ArrayList<String> args) {
        return new Request(NAME, args);
    }

    @Override
    public Response accept(CommandVisitor visitor, Request request) {
        return visitor.visit(this, request);
    }
}
