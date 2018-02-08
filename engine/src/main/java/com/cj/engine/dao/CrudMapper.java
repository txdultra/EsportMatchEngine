package com.cj.engine.dao;

public interface CrudMapper<T,PrimaryKey> {
    T get(PrimaryKey id);

    long insert(T model);

    long update(T model);
}
