package com.desirArman.blog.exception;

public class EmailSendingException extends RuntimeException{

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
