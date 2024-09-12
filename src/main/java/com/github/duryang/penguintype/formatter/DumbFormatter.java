package com.github.duryang.penguintype.formatter;

import com.github.duryang.penguintype.state.Session;

public class DumbFormatter implements SessionFormatter {

    private final Session session;

    public DumbFormatter(Session session) {
        this.session = session;
    }

    @Override
    public String format() {

        var sb = new StringBuilder();
        for (String word : session.getWords()) {
            sb.append(word);
            sb.append(' ');
        }

        return sb.toString();
    }
}
