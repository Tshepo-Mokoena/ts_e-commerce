package com.tshepo.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Order;
import com.tshepo.persistence.auth.OrderStatus;

@Repository
@Transactional(readOnly = true)
public interface IOrderRepository extends PagingAndSortingRepository<Order, Long>{

	Page<Order> findByAccount(Account account, Pageable pageable);

	Page<Order> findByOrderStatus(OrderStatus orderStatus, Pageable pageandSize);

}
