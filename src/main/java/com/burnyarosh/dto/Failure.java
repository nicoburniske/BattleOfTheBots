package com.burnyarosh.dto;

public class Failure extends AbstractDTO {
    public Failure() {
        super();
        this.put("reason", "none");
    }

    public Failure(String reason) {
        super();
        this.put("reason", reason);
    }

    @Override
    protected String getType() {
        return "error";
    }
}
