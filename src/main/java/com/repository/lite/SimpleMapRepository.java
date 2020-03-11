package com.repository.lite;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class SimpleMapRepository<T, ID> implements MapRepository<T, ID> {

    private Map<ID, T> values = new HashMap<>();

    public SimpleMapRepository() {
    }

    public <S extends T> S save(S entity, ID id) {

        Assert.notNull(entity, "Entity must not be null!");

        values.put(id, entity);

        return entity;
    }

    @Override
    public T find(ID id) {
        return values.get(id);
    }
}
