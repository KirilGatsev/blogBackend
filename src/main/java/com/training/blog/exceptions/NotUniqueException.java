package com.training.blog.exceptions;

public class NotUniqueException extends RuntimeException{
    public NotUniqueException(String field){
        super(field);
    }
}
