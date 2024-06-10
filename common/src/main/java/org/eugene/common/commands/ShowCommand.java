package org.eugene.common.commands;

import org.eugene.common.network.Request;
import org.eugene.common.network.Response;

import java.util.ArrayList;

public class ShowCommand extends Command {
    public static String NAME = "show";

    public ShowCommand() {
        super(NAME, "shows stored data");
    }

    @Override
    public Request buildRequest(ArrayList<String> args) {
        return new Request(NAME);
    }

    @Override
    public Response accept(CommandVisitor visitor, Request request) {
        return visitor.visit(this);
    }
}
