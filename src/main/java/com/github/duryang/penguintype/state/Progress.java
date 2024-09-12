package com.github.duryang.penguintype.state;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Progress {

    private final List<Word> typedWords = new LinkedList<>();

    public void add(Word word) {
        typedWords.add(word);
    }
}
