package com.github.duryang.penguintype.state;

import com.github.duryang.penguintype.formatter.Observer;

public interface Observable {
    void add(Observer observer);
    void remove(Observer observer);
    void notifyObservers();
}
