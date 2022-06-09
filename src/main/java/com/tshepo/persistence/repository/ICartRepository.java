package com.tshepo.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Cart;
import com.tshepo.persistence.Account;

@Repository
@Transactional(readOnly = true)
public interface ICartRepository extends CrudRepository<Cart, Long>{
	
	Cart findByAccount(Account account);

}
