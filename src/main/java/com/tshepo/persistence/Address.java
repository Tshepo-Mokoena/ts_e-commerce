package com.tshepo.persistence;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String street;
	private String town;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	
	@OneToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
	
	public static Address setAddress(Account account) 
	{
		Address address = new Address();
		address.setAccount(account);
		return address;
	}
	
	public Address updateAddress(Address newAddress)
	{
		Address address = new Address();
		address.setStreet(newAddress.getStreet());
		address.setTown(address.getTown());
		address.setProvince(newAddress.getProvince());
		address.setCity(newAddress.getCity());
		address.setCountry(newAddress.getCountry());
		address.setPostalCode(newAddress.getPostalCode());
		return address;
	}

}
