package me.kagura.exception;

public class FieldNotExistException extends Exception {
    public FieldNotExistException(String tempKey) {
        super("No property: " + tempKey);
    }
}
