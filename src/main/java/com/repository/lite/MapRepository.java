package com.repository.lite;

public interface MapRepository<T, ID> {

    <S extends T> S save(S entity, ID id);

    T find(ID id);
}
