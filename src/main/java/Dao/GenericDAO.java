package Dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    T findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
    int insert(T t) throws SQLException;
    boolean update(T t) throws SQLException;
    boolean delete(int id) throws SQLException;
}
