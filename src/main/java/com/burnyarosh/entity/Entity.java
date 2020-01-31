package com.burnyarosh.entity;

import java.util.Objects;
import java.util.UUID;

public abstract class Entity {
    protected String name;
    protected String id;

    public static String generateGUID() {
        return UUID.randomUUID().toString();
    }

    public Entity(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(name, entity.name) &&
                Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
