package org.eugene.common.commands;

import org.eugene.common.UserCredentials;
import org.eugene.common.network.Request;
import org.eugene.common.network.Response;
import org.eugene.common.network.Status;

import java.io.BufferedReader;
import java.util.ArrayList;

public abstract class Command {
    protected final String name;
    protected final String description;
    protected BufferedReader defaultReader;
    protected BufferedReader reader;
    protected UserCredentials userCredentials;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;

    }


    public abstract Request buildRequest(ArrayList<String> args);

    public void handleResponse(Response response) {
        if (response.getStatusCode() == Status.OK || response.getStatusCode() == Status.WARNING) {
            if (response.getStatusCode() == Status.WARNING) {
                if (response.getMessage() != null) {
                    System.err.println("[" + response.getStatusCode() + "]: " + response.getMessage());
                } else {
                    System.err.println("[" + response.getStatusCode() + "]");
                }
            } else if (response.getMessage() != null) {
                System.out.println(response.getMessage());
            }
        } else if (response.getStatusCode() == Status.ERROR) {
            if (response.getMessage() != null) {
                System.err.println("[" + response.getStatusCode() + "]: " + response.getMessage());
            } else {
                System.err.println("[" + response.getStatusCode() + "]");
            }
        }
    }

    public abstract Response accept(CommandVisitor visitor, Request request);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHelp() {
        return "<" + this.getName() + ">: " + this.getDescription();
    }

    public void setDefaultReader(BufferedReader defaultReader) {
        this.defaultReader = defaultReader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }
}
