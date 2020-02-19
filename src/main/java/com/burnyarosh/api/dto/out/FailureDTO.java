package com.burnyarosh.api.dto.out;

public class FailureDTO extends AbstractResponseDTO {
    private final String message;

    public FailureDTO() {
        this("Failure");
    }
    public FailureDTO(String message) {
        super("Failure");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}