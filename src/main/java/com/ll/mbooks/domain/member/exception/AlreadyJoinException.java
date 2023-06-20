package com.ll.mbooks.domain.member.exception;

public class AlreadyJoinException extends RuntimeException {
    public AlreadyJoinException(String msg) {
        super(msg);
    }
}