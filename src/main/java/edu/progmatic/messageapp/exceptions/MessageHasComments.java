package edu.progmatic.messageapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.PRECONDITION_FAILED, reason="Message cannot be deleted, because it has comments.")  // 404
public class MessageHasComments extends RuntimeException {
}
