package org.eugene.common.commands;

import org.eugene.common.network.Request;
import org.eugene.common.network.Response;

import java.util.ArrayList;

public class HelpCommand extends Command {
    public static String name = "help";


    public HelpCommand() {
        super(HelpCommand.name, "shows available commands");
    }

    @Override
    public Request buildRequest(ArrayList<String> args) {
        return new Request(this.getName());
    }

    @Override
    public Response accept(CommandVisitor visitor, Request request) {
        return visitor.visit(this);
    }
}
