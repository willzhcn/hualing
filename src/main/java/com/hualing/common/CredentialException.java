package com.hualing.common;

import javax.servlet.ServletException;

/**
 * Created by dipengfei on 8/9/16.
 */
public class CredentialException extends RuntimeException {

    private static final long serialVersionUID = -8799659324455306881L;

    public CredentialException(long number, String message) {
        super(message);
    }

    public CredentialException(long number, String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
