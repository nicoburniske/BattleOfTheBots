package com.burnyarosh.dto.out;

public class Success extends AbstractResponseDTO {
    public Success () {
        super();
    }

    @Override
    protected String getStatus() {
        return "success";
    }

}
