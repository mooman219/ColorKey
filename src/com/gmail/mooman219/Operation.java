package com.gmail.mooman219;

/**
 * @author Joseph Cumbo (mooman219)
 */
public final class Operation {

    public static enum Type {

        ENABLE,
        DISABLE;
    }

    private final Type type;
    private final String window;

    public Type getType() {
        return type;
    }

    public String getWindow() {
        return window;
    }

    public Operation(Type type, String window) {
        this.type = type;
        this.window = window;
    }
}
