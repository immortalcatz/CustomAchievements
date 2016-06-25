package io.github.mribby.customachievements.data;

public class Wildcard {
    public static final Wildcard INSTANCE = new Wildcard();

    private Wildcard() {}

    @Override
    public boolean equals(Object obj) {
        return true;
    }
}
