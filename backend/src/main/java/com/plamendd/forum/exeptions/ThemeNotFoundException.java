package com.plamendd.forum.exeptions;

public class ThemeNotFoundException extends RuntimeException {
    public ThemeNotFoundException(String m) {
        super(m);
    }
}
