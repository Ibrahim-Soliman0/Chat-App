package org.server.chatapp.dao.dao;

import java.util.List;

public interface Dao<T> {

    T get(long id);
    List<T> getAll();
    int update(T t);
    long insert(T t);
    int delete(T t);
}
