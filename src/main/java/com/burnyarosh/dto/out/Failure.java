package com.burnyarosh.dto.out;

public class Failure extends AbstractResponseDTO {
    public Failure() {
        super();
        this.put("reason", "none");
    }

    public Failure(String reason) {
        super();
        this.put("reason", reason);
    }

    @Override
    protected String getStatus() {
        return "error";
    }
}
