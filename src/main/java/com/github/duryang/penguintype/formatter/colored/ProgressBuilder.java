package com.github.duryang.penguintype.formatter.colored;

import com.github.duryang.penguintype.formatter.AnsiColor;
import com.github.duryang.penguintype.state.Word;

import java.util.List;

public class ProgressBuilder {

    protected final StringBuilder builder = new StringBuilder();

    protected AnsiColor currentColor = AnsiColor.DEFAULT;

    public void append(Word word) {
        // TODO: check if new line is needed, append it if so

        appendTypedChars(word);

        appendOmittedChars(word);

        appendExtraChars(word);

        builder.append(' ');
    }

    private void appendTypedChars(Word word) {
        List<Boolean> typedChars = word.getTyped();
        for (int i = 0; i < typedChars.size(); i++) {
            AnsiColor desiredColor = typedChars.get(i)
                    ? AnsiColor.GREEN
                    : AnsiColor.RED;

            checkAndChangeColor(desiredColor);

            builder.append(word.getWord().charAt(i));
        }
    }

    protected void appendOmittedChars(Word word) {
        String omitted = word.getWord().substring(word.getTyped().size());
        if (!omitted.isEmpty()) {
            checkAndChangeColor(AnsiColor.DEFAULT);
            builder.append(omitted);
        }
    }

    private void appendExtraChars(Word word) {
        if (!word.getExtra().isEmpty()) {
            checkAndChangeColor(AnsiColor.RED);
        }
        for (Character extraChar : word.getExtra()) {
            builder.append(extraChar);
        }
    }

    protected void checkAndChangeColor(AnsiColor desiredColor) {
        if (currentColor != desiredColor) {
            currentColor = desiredColor;
            builder.append(currentColor.getCode());
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
