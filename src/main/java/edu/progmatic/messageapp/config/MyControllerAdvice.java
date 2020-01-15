package edu.progmatic.messageapp.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String handleErrors(Exception ex, Model model){
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("trace", ExceptionUtils.getStackTrace(ex));
        return "error";
    }

}
