package com.mobylab.springbackend.exception;

public class ConflictActionException extends RuntimeException {

    public ConflictActionException(String message) {
        super(message);
    }
}
