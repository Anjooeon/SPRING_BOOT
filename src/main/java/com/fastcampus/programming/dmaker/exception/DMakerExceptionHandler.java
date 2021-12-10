package com.fastcampus.programming.dmaker.exception;

import com.fastcampus.programming.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.INTERNAL_SERVER_ERROR;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.INVALID_REQUEST;

/*
에러를 핸들링 하는 이유 :
    스프링이 버전이 바뀌거나 이럴 경우 에러를 핸들링하지 못하면 화면 쪽에서 장애가 발생할 가능성이 높아서.
    따라서 Client쪽에서 처리하기 편하게 하기위해 정의를 내리는것
    SQL 쿼리가 노출될 수도 있음-> Injection 위험
    불필요하게 상세한 에러는 보안적인 문제가 발생한다.
*/
@Slf4j
@RestControllerAdvice //각 Controller에 advice해주는 class임을 명시
public class DMakerExceptionHandler {
    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e, HttpServletRequest request){
        log.error("errorCode : {}, url: {}, message : {}", e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getMessage())
                .build();
    }
    //잡을 수 없는(Controller 내부에 진입전에 발생) Exception 처리
    //1. POST MAPPING, GET MAPPING의 HTTP MAPPING이 안맞을 경우
    //@Valid 처리를 했을때 문제가 발생한다.
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(Exception e, HttpServletRequest request){
        log.error("url: {}, message : {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    //어쩔수 없을 때..
    // 상세한 에러메세지를 보내주는게 좋기때문에 이런 코드는 지양해야함..
    //되도록 상세한 상황을 찾아서 리턴해줘야함
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleRequest(Exception e, HttpServletRequest request){
        log.error("url: {}, message : {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
