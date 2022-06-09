package com.tshepo.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Order;

@Repository
@Transactional(readOnly = true)
public interface IOrderRepository extends CrudRepository<Order, Long>{

}
