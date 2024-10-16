package com.github.duryang.penguintype;

import com.github.duryang.penguintype.exception.NoMatchingWordsException;
import com.github.duryang.penguintype.state.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

public class SessionFactory {

    private static final Random RANDOM = new Random();

    public static Session fromFile(String path) throws IOException, NoMatchingWordsException {
        List<String> allWords = Files.readAllLines(Path.of(path));

        return build(allWords);
    }

    public static Session fromInternalResource(String fileName) throws IOException, NoMatchingWordsException {
        List<String> words = new LinkedList<>();

        InputStream inputStream = SessionFactory.class.getClassLoader().getResourceAsStream(fileName);

        if (Objects.nonNull(inputStream)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    words.add(line.trim());
                }
            }
        } else {
            throw new IOException("Resource file not found.");
        }

        return build(words);
    }

    private static Session build(List<String> allWords) throws NoMatchingWordsException {
        Pattern pattern = CommandLineOptions.getPattern();
        if (pattern != null) {
            allWords = allWords.stream()
                    .filter(word -> pattern.matcher(word).find())
                    .toList();
        }

        if (allWords.isEmpty()) {
            throw new NoMatchingWordsException();
        }

        int count = CommandLineOptions.getWordCount();
        var words = new String[count];

        for (int i = 0; i < count; i++) {
            words[i] = allWords.get(RANDOM.nextInt(allWords.size()));
        }

        return new Session(words);
    }
}
