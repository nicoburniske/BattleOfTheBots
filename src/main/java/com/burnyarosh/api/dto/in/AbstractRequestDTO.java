package com.burnyarosh.api.dto.in;

public abstract class AbstractRequestDTO {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}