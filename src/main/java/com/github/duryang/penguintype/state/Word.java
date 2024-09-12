package com.github.duryang.penguintype.state;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Word {
    private final String word;
    private final List<Boolean> typed;
    private final List<Character> extra;

    public Word(String word) {
        this.word = word;
        this.typed = new LinkedList<>();
        this.extra = new LinkedList<>();
    }

    public void type(char c) {
        if (typed.size() == word.length()) {
            extra.add(c);
        }
        else {
            typed.add(word.charAt(typed.size()) == c);
        }
    }
}
