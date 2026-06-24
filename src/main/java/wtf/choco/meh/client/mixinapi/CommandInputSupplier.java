package wtf.choco.meh.client.mixinapi;

public interface CommandInputSupplier {

    public default boolean isCommandInBuffer() {
        return false;
    }

}
