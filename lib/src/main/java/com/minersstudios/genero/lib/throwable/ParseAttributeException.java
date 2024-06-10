package com.minersstudios.genero.lib.throwable;

public class ParseAttributeException extends RuntimeException {

    public ParseAttributeException() {
        super();
    }

    public ParseAttributeException(String message) {
        super(message);
    }

    public ParseAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseAttributeException(Throwable cause) {
        super(cause);
    }
}
