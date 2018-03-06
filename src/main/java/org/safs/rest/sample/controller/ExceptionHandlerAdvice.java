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
package org.safs.rest.sample.controller;

import org.safs.rest.sample.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Lei Wang
 *
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler
	public ResponseEntity<String> handleException(Exception e){
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if(e instanceof RestException){
			HttpStatus tempStatus = ((RestException)e).getHttpStatus();
			if(tempStatus!=null) status = tempStatus;
		}

		return ResponseEntity.status(status).body(e.getMessage());
	}
}
