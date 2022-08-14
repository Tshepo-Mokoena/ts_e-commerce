package com.tshepo.service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Payment;

public interface IPaymentService {
	
	Payment findByAccount(Account account);

	Payment updatePayment(Payment payment);

}
