package com.github.duryang.penguintype;

import com.github.duryang.penguintype.state.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class SessionFactory {

    private static final Random RANDOM = new Random();

    public static Session fromSource(String path) throws IOException {
        List<String> allWords = Files.readAllLines(Path.of(path));

        int count = 30;
        var words = new String[count];

        for (int i = 0; i < count; i++) {
            words[i] = allWords.get(RANDOM.nextInt(allWords.size()));
        }

        return new Session(words);
    }
}
