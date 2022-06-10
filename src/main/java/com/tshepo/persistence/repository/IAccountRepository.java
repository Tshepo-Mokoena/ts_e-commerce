package com.tshepo.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Account;

@Repository
@Transactional(readOnly = true)
public interface IAccountRepository extends CrudRepository<Account, Long>{
	
	Optional<Account> findByEmail(String email);
	
	@Transactional
	@Modifying
	@Query("update Account set active = :active where email = :email")
	void enableAccount(@Param("email") String email, @Param("active") Boolean active);

}
