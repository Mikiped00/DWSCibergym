package com.example.Web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	     
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	     
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	model.addAttribute("text", "The page you are looking for cannot be found");
	        	model.addAttribute("status", statusCode);
	            return "customError";
	        } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	        	model.addAttribute("text", "Apologize us... our server is not working properly");
	        	model.addAttribute("status", statusCode);
	            return "customError";
	        } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
	        	model.addAttribute("text", "It seems you are trying to access a page without authorization");
	        	model.addAttribute("status", statusCode);
	            return "customError";
	        }
	    }
	    return "error";
	}
	
	public String getErrorPath() {
        return "/error";
    }

}
