package com.iscas.quickframe.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
*@auhor:zhuquanwen
*@date:2016年12月28日
*@desc: 测试spring boot的校验
*/
@Controller
@Validated
public class TestValidController {
	@RequestMapping("/test101")
	@ResponseBody
	public String test101( @Validated TestE e, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {  
			
            return "error";  
        }else{
        	return "111";
        }
	}
	
	@RequestMapping("/test102")
	@ResponseBody
	
	public String test102( @RequestParam(value="aaa",defaultValue="") @Size(min = 1, max = 3)
	 String aaa){
		return "11122";
 		
	}
	
	
}


class TestE {
	@NotNull
	private String aaa;
	
	public String getAaa() {
		return aaa;
	}

	public void setAaa(String aaa) {
		this.aaa = aaa;
	}
	
}