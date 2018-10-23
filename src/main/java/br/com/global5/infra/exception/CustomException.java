package br.com.global5.infra.exception;

import javax.ejb.ApplicationException;

/**
 * Created by j r zielinski on 11/1/14.
 */
@ApplicationException(rollback = true)
public class CustomException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7023858669493658253L;

	public CustomException(String msg) {
        super(msg);
    }
}
