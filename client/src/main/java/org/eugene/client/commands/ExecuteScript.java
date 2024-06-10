package org.eugene.client.commands;

import org.eugene.common.commands.Command;
import org.eugene.common.commands.CommandVisitor;
import org.eugene.common.network.Request;
import org.eugene.common.network.Response;

import java.util.ArrayList;

public class ExecuteScript extends Command {
    public static final String NAME = "execute_script";

    public ExecuteScript() {
        super(NAME, "executes script from file");
    }

    @Override
    public Request buildRequest(ArrayList<String> args) {
        throw new RuntimeException("Execute script can't build request");
    }

    @Override
    public Response accept(CommandVisitor visitor, Request request) {
        throw new RuntimeException("Execute script can't accept visitor");
    }
}
