package org.eugene.common.commands;

import org.eugene.common.network.Request;
import org.eugene.common.network.Response;
import org.eugene.common.util.Ask;

import java.util.ArrayList;

public class UpdateIdCommand extends Command implements Ask {
    public static final String NAME = "update_id";

    public UpdateIdCommand() {
        super(NAME, "updates element with given id");
    }

    @Override
    public Request buildRequest(ArrayList<String> args) {

        return null;
    }

    @Override
    public Response accept(CommandVisitor visitor, Request request) {
        return visitor.visit(this, request);
    }
}
