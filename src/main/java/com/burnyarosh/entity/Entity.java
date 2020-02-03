package com.burnyarosh.entity;

import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.UUID;

public abstract class Entity {
    protected String name;
    protected String id;

    public static String generateGUID() {
        return UUID.randomUUID().toString();
    }

    public Entity(String name, String id) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(id);
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public abstract JsonObject toJson();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
