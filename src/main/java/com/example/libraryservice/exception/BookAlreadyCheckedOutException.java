package com.example.libraryservice.exception;

public class BookAlreadyCheckedOutException extends RuntimeException {
    public BookAlreadyCheckedOutException(String message) {
        super(message);
    }
}
