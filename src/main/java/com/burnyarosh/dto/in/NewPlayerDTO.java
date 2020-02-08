package com.burnyarosh.dto.in;

import com.burnyarosh.dto.IDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class NewPlayerDTO implements IDTO {
    private String username;

    public NewPlayerDTO(@JsonProperty("username") String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewPlayerDTO that = (NewPlayerDTO) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
