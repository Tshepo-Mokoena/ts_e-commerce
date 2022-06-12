package com.tshepo.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Order;
import com.tshepo.persistence.OrderItem;

@Repository
@Transactional(readOnly = true)
public interface IOrderItemRepository extends CrudRepository<OrderItem, Long>{

	List<OrderItem> findByOrder(Order order);

}
