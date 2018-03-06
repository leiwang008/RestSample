/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-06    (Lei Wang) Initial release.
 */
package org.safs.rest.sample.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Lei Wang
 *
 */
public class RestException extends RuntimeException{
	private static final long serialVersionUID = 6771321872886496151L;
	private HttpStatus httpStatus = null;

	public RestException(){
		super();
	}
	public RestException(String message){
		super(message);
	}
	public RestException(String message, HttpStatus httpStatus){
		super(message);
		this.httpStatus = httpStatus;
	}
	public RestException(String message, Throwable cause){
		super(message, cause);
	}
	public RestException(Throwable cause){
		super(cause);
	}

	protected RestException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
