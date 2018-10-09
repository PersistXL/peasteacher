package com.peas.common.base;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.querydsl.QPageRequest;

public class BaseServiceImpl<E> implements BaseService<E> {

    private BaseRepository<E> baseRepository;


    public void setRepository(BaseRepository<E> baseRepository){
        this.baseRepository = baseRepository;
    }

    @Override
    public Page<E> findAll(E e, Integer pageNum, Integer pageSize, Specification<E> spec) {
        return baseRepository.findAll(spec, new QPageRequest(pageNum,pageSize));
    }

    @Override
    public void save(E e) {
        baseRepository.saveAndFlush(e);
    }

    @Override
    public void delete(String id) {
        baseRepository.deleteById(id);
    }

    @Override
    public E findOne(String id) {
        return  baseRepository.findById(id).get();
    }




}
