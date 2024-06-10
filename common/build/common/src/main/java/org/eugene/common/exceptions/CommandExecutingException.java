package org.eugene.common.exceptions;


public class CommandExecutingException extends RuntimeException {
    public CommandExecutingException() {
        super("Fail while executing command");
    }

    public CommandExecutingException(String msg) {
        super(msg);
    }
}
