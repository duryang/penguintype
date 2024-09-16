package com.github.duryang.penguintype.state;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Word {

    @Getter(AccessLevel.NONE)
    private final int EXTRA_LIMIT = 10;

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
            if (extra.size() < EXTRA_LIMIT) {
                extra.add(c);
            }
        }
        else {
            typed.add(word.charAt(typed.size()) == c);
        }
    }

    public void undo() {
        if (!extra.isEmpty()) {
            extra.removeLast();
        } else if (!typed.isEmpty()) {
            typed.removeLast();
        }
    }

    public int getWidth() {
        return Math.max(typed.size() + extra.size(), word.length());
    }

    public boolean isCorrect() {
        return extra.isEmpty() &&
                typed.size() == word.length() &&
                typed.stream().allMatch(Boolean::booleanValue);
    }
}
