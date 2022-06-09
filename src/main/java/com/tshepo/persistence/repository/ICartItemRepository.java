package com.tshepo.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.CartItem;

@Repository
@Transactional(readOnly = true)
public interface ICartItemRepository extends CrudRepository<CartItem, Long>{

}
