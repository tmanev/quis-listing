package com.manev.quislisting.service.exception;

public class AttachmentStreamResourceException extends RuntimeException {

    public AttachmentStreamResourceException(String message) {
        super(message);
    }

    public AttachmentStreamResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
