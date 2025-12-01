package com.tagme.tagme_store_back.domain.exception;

public class BusinessException extends  RuntimeException{
    public BusinessException(String mensaje){
        super(mensaje);
    }
}