package com.tshepo.service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Address;

public interface IAddressService {
	
	Address findByAccount(Account account);
	
	Address updateAddress(Address address);

}
