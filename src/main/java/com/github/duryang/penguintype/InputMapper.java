package com.github.duryang.penguintype;

public class InputMapper {
    private InputMapper() {
    }

    public static Action toAction(int input) {
        if (input == 32) {
            return Action.NEXT;
        } else if (input == 8) {
            return Action.UNDO;
        } else if (input > 32 && input < 127) {
            return Action.TYPE;
        } else {
            return Action.NOTHING;
        }
    }
}
