package com.javachallenge.application.entity;

public class Result<T> {    
    private T result;

    private boolean success;
    
    public T getResult() {
        return result;
    }    
    public boolean isSuccess() {
        return success;
    }

    public Result(T result, boolean success) {
        this.result = result;
        this.success = success;
    }
}
