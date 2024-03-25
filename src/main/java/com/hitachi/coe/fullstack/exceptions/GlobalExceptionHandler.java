package com.hitachi.coe.fullstack.exceptions;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.hitachi.coe.fullstack.model.common.ErrorLineModel;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class GlobalExceptionHandler is used to return a response when there is a MaxUploadSizeExceededException.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * This method handles the NoSuchMethodException, InvocationTargetException, InstantiationException and IllegalAccessException.
     *
     * @param re The ReflectiveOperationException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a BadRequest status code and a message.
     * @author tquangpham
     */
    @ExceptionHandler(value = {NoSuchMethodException.class, InvocationTargetException.class
            , InstantiationException.class, IllegalAccessException.class})
    public ResponseEntity<BaseResponse<String>> handleImportException(ReflectiveOperationException re) {
        return ResponseEntity.of(Optional.of(BaseResponse.badRequest(re.getMessage())));
    }

    /**
     * This method handles the IOException exception.
     *
     * @param me The IOException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a BadRequest status code and a message.
     * @author tquangpham
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<BaseResponse<String>> handleIOExceptionException(IOException me) {
        return ResponseEntity.of(Optional.of(BaseResponse.badRequest(ErrorConstant.MESSAGE_ERROR_INPUT)));
    }

    /**
     * This method handles the MaxUploadSizeExceededException exception.
     *
     * @param me The MaxUploadSizeExceededException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a BadRequest status code and a message.
     * @author tquangpham
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BaseResponse<String>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException me) {
        return ResponseEntity.of(Optional.of(BaseResponse.badRequest(ErrorConstant.MESSAGE_MAX_FILE_SIZE)));
    }

    /**
     * This method handles the BadCredentialsException exception.
     *
     * @param ex The BadCredentialsException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a unauthorized status code and a message.
     * @author tminhto
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<BaseResponse<?>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.of(Optional.of(BaseResponse.unAuthorized(ErrorConstant.MESSAGE_BAD_CREDENTIALS)));
    }

    /**
     * This method handles the JwtException exception.
     *
     * @param ex The JwtException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with an unauthorized status code and a message.
     * @author tminhto
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<BaseResponse<?>> handleJwtException(JwtException ex) {
        return ResponseEntity.of(Optional.of(BaseResponse.unAuthorized(ErrorConstant.MESSAGE_INVALID_TOKEN)));
    }

    /**
     * This method handles the CoEException exception.
     *
     * @param ce The CoEException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a BadRequest status code and a message
     * @author tquangpham
     */
    @ExceptionHandler(CoEException.class)
    public ResponseEntity<BaseResponse<String>> handleCoEException(CoEException ce, HttpServletRequest request) {
        log.error("===> Error method : {}, with URL : {}, and message is : {}", request.getMethod(), request.getRequestURL(), ce.getMessage());
        return ResponseEntity.of(Optional.of(BaseResponse.badRequest(ce.getMessage())));
    }

    /**
     * This method handles the RuntimeException exception.
     *
     * @param ex The RuntimeException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a BadRequest status code and a message
     * @author lamluong
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.of(Optional.of(BaseResponse.fail(ex.getMessage())));
    }

    /**
     * This method handles the MethodArgumentNotValidException.
     *
     * @param ex The MethodArgumentNotValidException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a Fail status code and a message
     * Example response:
     * <pre>
     * {@code
     *     "status": 400,
     *     "message": null,
     *     "data": [
     *         {
     *             "field": "password",
     *             "message": "Password is required"
     *         },
     *         {
     *             "field": "username",
     *             "message": "Username is required"
     *         }
     *     ]
     * }
     * </pre>
     * @author tquangpham
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<List<ErrorLineModel>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorLineModel> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorLineModel(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.of(Optional.of(BaseResponse.badRequest(errors)));

    }

    /**
     * This method handles the MissingServletRequestParameterException.
     *
     * @param ex The MissingServletRequestParameterException object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a Fail status code and a message
     * @author tquangpham
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.of(Optional.of(BaseResponse.badRequest(ex.getParameterName() + " is required")));
    }

    /**
     * This method handles the Exception.
     *
     * @param ex The Exception object that was thrown.
     * @return A ResponseEntity object containing a BaseResponse object with a Fail status code and a message
     * @author tquangpham
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleException(Exception ex) {
        return ResponseEntity.of(Optional.of(BaseResponse.fail(ErrorConstant.INTERNAL_SERVER_ERROR)));
    }
}

