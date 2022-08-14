package com.tshepo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Payment;
import com.tshepo.persistence.repository.IPaymentRepository;
import com.tshepo.service.IPaymentService;

@Service
public class PaymentService implements IPaymentService{
	
	private IPaymentRepository paymentRepository;
	
	@Autowired
	private void setPaymentService(IPaymentRepository paymentRepository) {this.paymentRepository = paymentRepository;}
	
	@Override
	public Payment findByAccount(Account account) {return paymentRepository.findByAccount(account);}
	
	public Payment updatePayment(Payment payment) { return paymentRepository.save(payment); }

}
