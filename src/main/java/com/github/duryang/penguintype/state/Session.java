package com.github.duryang.penguintype.state;

import com.github.duryang.penguintype.formatter.Observer;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Session implements Observable {

    private final String[] words;
    private final Progress progress;

    private int currentIndex = 0;
    private Word currentWord;

    private final Scoring scoring;

    private final List<Observer> obs = new LinkedList<>();

    public Session(String[] words) {
        this.words = words;
        this.progress = new Progress();
        this.scoring = new Scoring();

        currentWord = new Word(words[0]);
    }

    /**
     * Moves to the next word if there is one.
     *
     * @return false if there is no next.
     */
    public boolean next() {
        scoring.registerStartTime();
        scoring.count(currentWord);

        progress.add(currentWord);

        currentIndex++;
        if (currentIndex == words.length) {
            return false;
        }

        currentWord = new Word(words[currentIndex]);
        notifyObservers();

        return true;
    }

    public void type(char c) {
        scoring.registerStartTime();
        currentWord.type(c);
    }

    public void undo() {
        currentWord.undo();
    }

    @Override
    public void add(Observer observer) {
        obs.add(observer);
    }

    @Override
    public void remove(Observer observer) {
        obs.remove(observer);
    }

    @Override
    public void notifyObservers() {
        obs.forEach(Observer::update);
    }

    // TODO: support undo for the current word (go back 1 letter).

    // TODO: add some sort of scoring system. the simplest is probable count the number of correct letters in the current word when doing a next()
    // TODO: also could just measure the time that took to complete the test
}
