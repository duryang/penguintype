package com.github.duryang.penguintype.formatter.colored;

import com.github.duryang.penguintype.formatter.AnsiColor;
import com.github.duryang.penguintype.formatter.Observer;
import com.github.duryang.penguintype.formatter.SessionFormatter;
import com.github.duryang.penguintype.state.Session;
import com.github.duryang.penguintype.state.Word;

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

        String progressString = progressBuilder.toString();

        var currentWordBuilder = new CurrentWordBuilder();
        currentWordBuilder.append(session.getCurrentWord());

        StringBuilder builder = new StringBuilder(progressString);
        builder.append(currentWordBuilder);

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

    /**
     * Should be called when a new word is added to the progress of the session
     */
    @Override
    public void update() {
        Word newlyAddedWord = session.getProgress().getTypedWords().getLast();
        progressBuilder.append(newlyAddedWord);
    }
}
