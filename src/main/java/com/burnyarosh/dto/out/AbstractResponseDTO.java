package com.burnyarosh.dto.out;

import com.burnyarosh.dto.IDTO;

public abstract class AbstractResponseDTO implements IDTO {

    String status;

    public AbstractResponseDTO(String status) {
        this.status = status;
    }

   public String getStatus() {
        return this.status;
   }

   public void setStatus(String status) {
        this.status = status;
   }
}
