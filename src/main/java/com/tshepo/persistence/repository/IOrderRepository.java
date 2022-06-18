package com.tshepo.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Order;

@Repository
@Transactional(readOnly = true)
public interface IOrderRepository extends CrudRepository<Order, Long>{

	List<Order> findByAccount(Account account);

}
