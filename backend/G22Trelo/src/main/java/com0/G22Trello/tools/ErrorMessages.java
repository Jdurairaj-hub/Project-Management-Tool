package com0.G22Trello.tools;

// This was created primarily to eliminate Long Statement
public enum ErrorMessages {
    INVALID_PASSWORD("Invalid password. Password should have a minimum length of 8 characters, at least 1 uppercase character, 1 lowercase character, 1 number, and 1 special character."),
    EMAIL_ALREADY_REGISTERED("Email address already registered. Please log in."),
    INVALID_DATE_ERROR("Date enter is not Valid, due date needs to be after creation date.");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
