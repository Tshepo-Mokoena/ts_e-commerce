package com.tshepo.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tshepo.persistence.Order;
import com.tshepo.service.IOrderService;
import com.tshepo.util.EmailTemplates;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private EmailTemplates emailTemplate;
	
	@Autowired
	private IOrderService orderService;
	
	@GetMapping("/home")
	public String homePage(Model model, @RequestParam Long orderId) {
		
		Order order = orderService.findById(orderId).orElseThrow(()-> new RuntimeException());
		emailTemplate.orderConfirm(order);
		emailTemplate.emailConfirm(order.getAccount(), "tshepo.com");
		emailTemplate.emailConfirmed(order.getAccount(), "tshepo.com");
		emailTemplate.passwordReset(order.getAccount(), "");
		emailTemplate.passwordUpdated(order.getAccount(), "");
		model.addAttribute("account", order.getAccount());
		model.addAttribute("order", order);
		model.addAttribute("orderItems", order.getOrderItems());
		return "orderTemplate";
	}

}
