package com.eventi.left.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberController {

	@RequestMapping(value = "/qualification")
	public String qualification() {
		return "content/member/qualification";
	}
	
	@RequestMapping(value = "/normalSignIn")
	public String normalSignInPage() {
		return "content/member/normalSignIn";
	}
	
	@RequestMapping(value = "/busiSignIn")
	public String busiSignInPage() {
		return "content/member/busiSignIn";
	}

}
