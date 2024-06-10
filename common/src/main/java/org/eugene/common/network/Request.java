package org.eugene.common.network;

import org.eugene.common.UserCredentials;
import org.eugene.common.modelCSV.SpaceMarine;

import java.io.Serializable;
import java.util.ArrayList;


public class Request implements Serializable {
    private final String commandName;
    private final SpaceMarine spaceMarine;
    private final ArrayList<String> args;
    private UserCredentials userCredentials;


    public Request(String commandName, SpaceMarine movie) {
        this.commandName = commandName;
        this.spaceMarine = movie;
        args = null;
    }

    public Request(String commandName, ArrayList<String> args) {
        this.commandName = commandName;
        this.args = args;
        this.spaceMarine = null;
    }

    public Request(String commandName) {
        this.commandName = commandName;
        args = null;
        spaceMarine = null;
    }

    public Request(String commandName, SpaceMarine movie, ArrayList<String> args) {
        this.commandName = commandName;
        this.spaceMarine = movie;
        this.args = args;
    }

    public String getCommandName() {
        return commandName;
    }

    public SpaceMarine getSpaceMarine() {
        return spaceMarine;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }
}
