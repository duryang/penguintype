package com.github.duryang.penguintype.state;

public class Scoring {
    private long startTime = 0L;
    private int correctChars = 0;
    private int rawCorrectChars = 0;
    private int correctWords = 0;
    private int incorrectWords = 0;

    public void registerStartTime() {
        if (startTime == 0L) {
            startTime = System.currentTimeMillis();
        }
    }

    public void count(Word word) {
        if (word.isCorrect()) {
            correctChars += word.getWord().length() + 1;
            correctWords++;
        } else {
            incorrectWords++;
        }

        rawCorrectChars += (int) word.getTyped().stream().filter(Boolean::booleanValue).count() + 1;
    }

    public double wpmClean() {
        return wpm(correctChars);
    }

    public double wpmRaw() {
        return wpm(rawCorrectChars);
    }

    private double wpm(int correctChars) {
        if (startTime == 0L) {
            return 0;
        }

        long timeElapsedMillis = System.currentTimeMillis() - startTime;

        // score / (5 * (timeElapsedMillis / 60000))
        return (double) (correctChars * 12000) / timeElapsedMillis;
    }

    public String stats() {
        return String.format("Correct: %s\nIncorrect: %s\nWPM: %s\nRaw: %s",
                correctWords,
                incorrectWords,
                Math.round(wpmClean()),
                Math.round(wpmRaw()));
    }
}
