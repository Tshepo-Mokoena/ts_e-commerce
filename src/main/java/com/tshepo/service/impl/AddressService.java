package com.tshepo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Address;
import com.tshepo.persistence.repository.IAddressRepository;
import com.tshepo.service.IAddressService;

@Service
public class AddressService implements IAddressService{
	
	private IAddressRepository addressRepository;
	
	@Autowired
	private void setAddressService(IAddressRepository addressRepository) { this.addressRepository = addressRepository; }
	
	@Override
	public Address findByAccount(Account account) { return addressRepository.findByAccount(account); }

	@Override
	public Address updateAddress(Address address) { return addressRepository.save(address); }
;
}
