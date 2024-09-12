package com.github.duryang.penguintype.formatter;

import com.github.duryang.penguintype.state.Session;
import com.github.duryang.penguintype.state.Word;

import java.util.List;

public class ColoredFormatter implements SessionFormatter, Observer {

    private final Session session;
    // TODO: use this to add a new line based on terminal width
    private final int terminalWidth;

    private final ProgressBuilder progressBuilder = new ProgressBuilder();

    public ColoredFormatter(Session session, int terminalWidth) {
        this.session = session;
        this.terminalWidth = terminalWidth;

        session.add(this);
    }

    @Override
    public String format() {

        String progressString = progressBuilder.withCurrentWord();

        StringBuilder builder = new StringBuilder(progressString);

        builder.append(AnsiColor.DEFAULT.getCode());
        for (int i = session.getProgress().getTypedWords().size() + 1; i < session.getWords().length; i++) {
            appendNotTypedWord(session.getWords()[i], builder);
        }

        // hide the cursor
        builder.append("\u001B[?25l");

        return builder.toString();
    }

    private void appendNotTypedWord(String word, StringBuilder builder) {
        builder.append(word);
        builder.append(' ');
    }

    @Override
    public void update() {
        Word newlyAddedWord = session.getProgress().getTypedWords().getLast();
        progressBuilder.append(newlyAddedWord);
    }

    private class ProgressBuilder {
        private final StringBuilder builder = new StringBuilder();

        private AnsiColor lastColor = AnsiColor.DEFAULT;

        private void append(Word word) {
            lastColor = append(word, builder);
        }

        private AnsiColor append(Word word, StringBuilder builder) {
            // TODO: check if new line is needed, append it if so

            AnsiColor currentColor = lastColor;

            List<Boolean> typedChars = word.getTyped();
                for (int i = 0; i < typedChars.size(); i++) {
                    AnsiColor desiredColor = typedChars.get(i)
                            ? AnsiColor.GREEN
                            : AnsiColor.RED;

                currentColor = changeColorIfNeeded(currentColor, desiredColor, builder);

                builder.append(word.getWord().charAt(i));
            }

            // omitted characters
            String omitted = word.getWord().substring(typedChars.size());
            if (!omitted.isEmpty()) {
                currentColor = changeColorIfNeeded(currentColor, AnsiColor.DEFAULT, builder);
                builder.append(omitted);
            }

            if (!word.getExtra().isEmpty()) {
                currentColor = changeColorIfNeeded(currentColor, AnsiColor.RED, builder);
            }

            for (Character extraChar : word.getExtra()) {
                builder.append(extraChar);
            }

            builder.append(' ');

            return currentColor;
        }

        public String withCurrentWord() {
            var newBuilder = new StringBuilder(builder);

            append(session.getCurrentWord(), newBuilder);

            return newBuilder.toString();
        }

        private AnsiColor changeColorIfNeeded(AnsiColor currentColor, AnsiColor desiredColor, StringBuilder builder) {
            if (currentColor != desiredColor) {
                builder.append(desiredColor.getCode());
                return desiredColor;
            }

            return currentColor;
        }
    }
}
