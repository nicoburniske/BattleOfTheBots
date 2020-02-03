package com.burnyarosh.dto;

public class Success extends AbstractDTO {
    public Success () {
        super();
    }

    @Override
    protected String getStatus() {
        return "success";
    }

}
