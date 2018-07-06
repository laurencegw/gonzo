package com.binarymonks.gonzo.web.controllers

import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.common.ValidationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class ExceptionResponseHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(NotAuthorized::class)
    fun handleNotAuthorized(ex: NotAuthorized, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = "Permissions not met"
        return handleExceptionInternal(ex, bodyOfResponse,
                HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidation(ex: ValidationException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, ex.validationMessages,
                HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request)
    }

}