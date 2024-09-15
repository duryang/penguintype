package com.github.duryang.penguintype.formatter;

import lombok.Getter;

@Getter
public enum AnsiColor {
    GREEN("\u001B[32m"),
    RED("\u001B[31m"),
    DARK_RED("\u001B[38;5;52m"),
    DEFAULT("\u001B[0m");

    private AnsiColor(String code) {
        this.code = code;
    }

    private final String code;
}
