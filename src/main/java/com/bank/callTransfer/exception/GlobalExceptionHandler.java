package com.bank.callTransfer.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bank.callTransfer.dto.Result;
import com.bank.callTransfer.dto.Status;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Result globalExceptions(final Exception exception, final HttpServletRequest request) {
		
		Status status = new Status();
		status.setCode(HttpStatus.BAD_REQUEST.value());
		status.setMessage(exception.getMessage());
		
		Result result = new Result();
		result.setStatus(status);
		
        return result;
    }

	@ExceptionHandler(GenericException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Result handleExceptions(final GenericException exception, final HttpServletRequest request) {
		
		Status status = new Status();
		status.setCode(HttpStatus.BAD_REQUEST.value());
		status.setMessage(exception.getMessage());
		
		Result result = new Result();
		result.setStatus(status);
		
        return result;
    }
}
