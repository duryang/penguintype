package com.github.duryang.penguintype.words;

import com.github.duryang.penguintype.CommandLineOptions;
import com.github.duryang.penguintype.state.Session;

import java.util.List;
import java.util.Random;

public class SessionFactory {

    private static final Random RANDOM = new Random();

    public static Session build(List<String> allWords) {
        int count = CommandLineOptions.getWordCount();
        var words = new String[count];

        for (int i = 0; i < count; i++) {
            words[i] = allWords.get(RANDOM.nextInt(allWords.size()));
        }

        return new Session(words);
    }
}
