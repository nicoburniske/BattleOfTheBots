package com.burnyarosh.dto.in;

public abstract class AbstractRequestDTO {
    String type;

    public AbstractRequestDTO(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract boolean isValidRequest();
}
