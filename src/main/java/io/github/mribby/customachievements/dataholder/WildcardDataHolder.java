package io.github.mribby.customachievements.dataholder;

public class WildcardDataHolder extends DataHolder {
    public static final WildcardDataHolder INSTANCE = new WildcardDataHolder();

    private WildcardDataHolder() {}

    @Override
    public boolean isEqual(Object obj) {
        return true;
    }
}
