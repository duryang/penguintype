package com.github.duryang.penguintype.formatter;

public class LineBreakingTextBuilder {

    private final StringBuilder builder;
    private final int maxLineLength;
    private int currentLineLength;

    public LineBreakingTextBuilder(int maxLineLength) {
        this.builder = new StringBuilder();
        this.maxLineLength = maxLineLength;
        this.currentLineLength = 0;
    }

    public LineBreakingTextBuilder(LineBreakingTextBuilder other) {
        this.builder = new StringBuilder(other.builder);
        this.maxLineLength = other.maxLineLength;
        this.currentLineLength = other.currentLineLength;
    }

    public LineBreakingTextBuilder appendWhole(String s) {
        appendSeparatorIfNeeded(s.length());

        builder.append(s);
        currentLineLength += s.length();

        return this;
    }

    public LineBreakingTextBuilder reserve(int length) {
        appendSeparatorIfNeeded(length);
        currentLineLength += length;
        return this;
    }

    public LineBreakingTextBuilder append(String part) {
        builder.append(part);
        return this;
    }

    public LineBreakingTextBuilder append(char part) {
        builder.append(part);
        return this;
    }

    private void appendSeparatorIfNeeded(int addingLength) {
        if (currentLineLength == 0) {
            return;
        }

        if (currentLineLength + addingLength + 1 > maxLineLength) {
            builder.append(System.lineSeparator());
            currentLineLength = 0;
        } else {
            builder.append(' ');
            currentLineLength++;
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
