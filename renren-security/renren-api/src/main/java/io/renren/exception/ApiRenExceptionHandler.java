/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.exception;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.exception.RenExceptionHandler;
import io.renren.common.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@RestControllerAdvice
public class ApiRenExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RenExceptionHandler.class);

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RenException.class)
	public Result handleRenException(RenException ex){
		return Result.fail(ex.getMsg());
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Result handleDuplicateKeyException(DuplicateKeyException ex){
		return Result.fail(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<Void> handleDuplicateKeyException(MethodArgumentNotValidException ex){
		BindingResult bindingResult = ex.getBindingResult();
		Result<Void> result = new Result<>();
		result.setCode(ErrorCode.ARGUMENT_NOT_VALID);
		result.setMsg(bindingResult.getFieldError().getDefaultMessage());
		return result;
	}

	@ExceptionHandler(Exception.class)
	public Result handleException(Exception ex){
		logger.error(ex.getMessage(), ex);
		return Result.fail(ex.getMessage());
	}
}