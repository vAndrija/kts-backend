package com.kti.restaurant.service.contract;

import java.util.List;

public interface IService<T> {
    List<T> findAll();
    T findById(Integer id);
    T create(T entity);
    T update(T entity) throws Exception;
    void delete(Integer id);
}
