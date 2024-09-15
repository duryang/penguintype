package com.github.duryang.penguintype.formatter.colored;

import com.github.duryang.penguintype.formatter.AnsiColor;
import com.github.duryang.penguintype.formatter.LineBreakingTextBuilder;
import com.github.duryang.penguintype.formatter.Observer;
import com.github.duryang.penguintype.formatter.SessionFormatter;
import com.github.duryang.penguintype.state.Session;
import com.github.duryang.penguintype.state.Word;

public class ColoredFormatter implements SessionFormatter, Observer {

    private final Session session;

    private final ProgressFormatter progressFormatter;

    public ColoredFormatter(Session session, int terminalWidth) {
        this.session = session;
        this.progressFormatter = new ProgressFormatter(terminalWidth);

        session.add(this);
    }

    @Override
    public String format() {

        // The progress builder is ready at this point. We just need to get a copy of it.
        LineBreakingTextBuilder builder = withCurrentWord(progressFormatter.getBuilder());

        builder.append(AnsiColor.DEFAULT.getCode());
        for (int i = session.getProgress().getTypedWords().size() + 1; i < session.getWords().length; i++) {
            builder.appendWhole(session.getWords()[i]);
        }

        // hide the cursor
        builder.append("\u001B[?25l");

        return builder.toString();
    }

    private LineBreakingTextBuilder withCurrentWord(LineBreakingTextBuilder builder) {
        var currentWordFormatter = new CurrentWordFormatter(builder);
        currentWordFormatter.append(session.getCurrentWord());

        return builder;
    }

    /**
     * Should be called when a new word is added to the progress of the session
     */
    @Override
    public void update() {
        Word newlyAddedWord = session.getProgress().getTypedWords().getLast();
        progressFormatter.append(newlyAddedWord);
    }
}
