package org.eugene.common.exceptions;

public class InfiniteScriptCallLoopException extends CommandExecutingException {
    public InfiniteScriptCallLoopException() {
        super("Infinite cycle of calling script occurs, interrupting command execution, resuming program");
    }
}
