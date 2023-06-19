package com.ll.mbooks.app.member.exception;

public class AlreadyJoinException extends RuntimeException {
    public AlreadyJoinException(String msg) {
        super(msg);
    }
}