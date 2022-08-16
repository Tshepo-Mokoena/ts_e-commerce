package com.tshepo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.Order;
import com.tshepo.service.IEmailService;

@Component
public class EmailTemplates {
		
	private TemplateEngine templateEngine;
	
	private IEmailService emailService;
	
	@Autowired
	private void setEmailTemplates(IEmailService emailService, TemplateEngine templateEngine)
	{
		this.emailService = emailService;
		this.templateEngine = templateEngine;
	}
	
	public void orderConfirm(Order order) 
	{
		Context context = new Context();
		context.setVariable("account", order.getAccount());
		context.setVariable("order", order);
		context.setVariable("orderItems", order.getOrderItems());
		String text = templateEngine.process("orderTemplate", context);	
		emailService.sendEmail(order.getAccount().getEmail(), "Order Confirm", text);
	}
	
	public void emailConfirm(Account account, String link) 
	{
		Context context = new Context();
		context.setVariable("account", account);
		context.setVariable("link", link);
		String text = templateEngine.process("emailConfirmation", context);	
		emailService.sendEmail(account.getEmail(), "Confirm Email", text);
	}
	
	public void emailConfirmed(Account account, String link) 
	{
		Context context = new Context();
		context.setVariable("account", account);
		context.setVariable("link", link);
		String text = templateEngine.process("welcome", context);	
		emailService.sendEmail(account.getEmail(), "Email Confirmed", text);
	}
	
	public void passwordReset(Account account, String link) 
	{
		Context context = new Context();
		context.setVariable("account", account);
		context.setVariable("link", link);
		String text = templateEngine.process("passwordReset", context);	
		emailService.sendEmail(account.getEmail(), "Password Reseted", text);
	}
	
	public void passwordUpdated(Account account, String link) 
	{
		Context context = new Context();
		context.setVariable("account", account);
		context.setVariable("link", link);
		String text = templateEngine.process("passwordChanged", context);	
		emailService.sendEmail(account.getEmail(), "Password Updated", text);
	}
	
}
