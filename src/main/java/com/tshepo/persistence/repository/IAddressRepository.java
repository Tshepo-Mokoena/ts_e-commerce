package com.tshepo.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Address;

@Repository
@Transactional(readOnly =true)
public interface IAddressRepository extends CrudRepository<Address, Long>{
	
	Address findByAccount(Account account);
	
}
