package com.tshepo.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Cart;
import com.tshepo.persistence.CartItem;
import com.tshepo.persistence.Product;

@Repository
@Transactional(readOnly = true)
public interface ICartItemRepository extends CrudRepository<CartItem, Long>{
	
	List<CartItem> findByCart(Cart cart);
	
	Optional<CartItem> findByProduct(Product product);

}
