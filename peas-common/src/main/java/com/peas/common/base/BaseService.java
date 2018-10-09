package com.peas.common.base;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;


public interface BaseService<E> {

    Page<E> findAll(E e, Integer pageNum, Integer pageSize, Specification<E> spec);

    @Transactional
    void save(E e);

    @Transactional
    void delete(String id);

    E findOne(String id);
}
