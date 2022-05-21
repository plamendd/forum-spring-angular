package com.plamendd.forum.exeptions;

import org.springframework.mail.MailException;

public class ForumException extends RuntimeException {
    public ForumException(String s, MailException e) {
        super(s, e);
    }

    public ForumException(String s) {
        super(s);
    }
}
