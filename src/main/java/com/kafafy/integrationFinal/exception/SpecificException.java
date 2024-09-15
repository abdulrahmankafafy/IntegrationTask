package com.kafafy.integrationFinal.exception;

public class SpecificException extends RuntimeException {
    private String code;

    public SpecificException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
