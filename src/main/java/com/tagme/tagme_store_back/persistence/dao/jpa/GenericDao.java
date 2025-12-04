package com.tagme.tagme_store_back.persistence.dao.jpa;

import java.util.Optional;

public interface GenericDao<T> {
    Optional<T> findById(Long id);
    T save(T entity);
    void deleteById(Long id);
}
