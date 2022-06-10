package com.tshepo.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tshepo.service.IEmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService implements IEmailService{
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private JavaMailSender mailSender;	
	
	@Override
	@Async
	public void sendEmail(String to, String subject, String emailMessage) 
	{
		try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(environment.getProperty("support.email"));
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.warn("failed to send email", e);
        }
	}

}
