package com.github.duryang.penguintype.formatter.colored;

import com.github.duryang.penguintype.formatter.AnsiColor;
import com.github.duryang.penguintype.formatter.LineBreakingTextBuilder;
import com.github.duryang.penguintype.state.Word;

import java.util.List;

public class ProgressFormatter {

    protected final LineBreakingTextBuilder builder;

    protected AnsiColor currentColor = AnsiColor.DEFAULT;

    public ProgressFormatter(int width) {
        this.builder = new LineBreakingTextBuilder(width);
    }

    public ProgressFormatter(LineBreakingTextBuilder builder) {
        this.builder = builder;
    }

    public void append(Word word) {
        builder.reserve(word.getLength());

        appendTypedChars(word);
        appendOmittedChars(word);
        appendExtraChars(word);
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
            changeColor(AnsiColor.DARK_RED);
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

    protected void changeColor(AnsiColor color) {
        currentColor = color;
        builder.append(currentColor.getCode());
    }

    public LineBreakingTextBuilder getBuilder() {
        return new LineBreakingTextBuilder(builder);
    }
}
