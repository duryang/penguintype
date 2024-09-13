package com.github.duryang.penguintype.formatter.colored;

import com.github.duryang.penguintype.formatter.AnsiColor;
import com.github.duryang.penguintype.state.Word;

public class CurrentWordBuilder extends ProgressBuilder {

    @Override
    protected void appendOmittedChars(Word word) {
        int typedLength = word.getTyped().size();

        if (typedLength < word.getWord().length()) {
            resetColor();

            char currentChar = word.getWord().charAt(typedLength);

            builder.append("\u001B[4m");
            builder.append(currentChar);
            builder.append("\u001B[0m");

            String rest = word.getWord().substring(word.getTyped().size() + 1);
            if (!rest.isEmpty()) {
                builder.append(rest);
            }
        }

    }

    protected void resetColor() {
        currentColor = AnsiColor.DEFAULT;
        builder.append(currentColor.getCode());
    }
}
