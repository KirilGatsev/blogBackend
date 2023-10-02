package com.training.blog.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id){
        super("User with id:" + id + " not found.");
    }

}
