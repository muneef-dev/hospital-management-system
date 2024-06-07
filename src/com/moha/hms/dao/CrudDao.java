package com.moha.hms.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T,ID> extends SuperDao {
    boolean create(T t) throws SQLException, ClassNotFoundException;
    List<T> search(ID id) throws SQLException, ClassNotFoundException;
    boolean delete(ID id) throws SQLException, ClassNotFoundException;
    boolean update(T t) throws SQLException, ClassNotFoundException;
    List<T> loadAll() throws SQLException, ClassNotFoundException;
}
