package com.github.duryang.penguintype.words;

import com.github.duryang.penguintype.CommandLineOptions;
import com.github.duryang.penguintype.exception.NoMatchingWordsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class WordsContainer {

    private static final String DEFAULT_FILE_NAME = "words.txt";

    public static final WordsContainer instance = new WordsContainer();
    private List<String> words;

    private WordsContainer() {
    }

    public void load() throws IOException, NoMatchingWordsException {

        List<String> allWords;
        if (CommandLineOptions.getFilePath() == null) {
            allWords = loadFromInternalResource(DEFAULT_FILE_NAME);
        } else {
            allWords = loadFromFile(CommandLineOptions.getFilePath());
        }

        words = filter(allWords);

        if (words.isEmpty()) {
            throw new NoMatchingWordsException();
        }
    }

    private static List<String> loadFromInternalResource(String fileName) throws IOException {
        List<String> words = new ArrayList<>();

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

        return words;
    }

    private static List<String> loadFromFile(String path) throws IOException {
        return Files.readAllLines(Path.of(path));
    }

    private List<String> filter(List<String> allWords) {
        Pattern pattern = CommandLineOptions.getPattern();

        if (pattern == null) {
            return allWords;
        }

        return allWords.stream()
                .filter(word -> pattern.matcher(word).find())
                .toList();
    }

    public List<String> getWords() {
        return Objects.requireNonNull(words, "Load the words first before try to access it.");
    }
}
