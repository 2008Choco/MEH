package wtf.choco.meh.client.chat;

/**
 * Represents a type of chat channel.
 */
public enum ChatChannelType {

    /**
     * The global chat channel, which shows all messages and does not have any special command
     * prefix associated with it.
     */
    GLOBAL,
    /**
     * A built-in, known channel, configured by the client. This channel can never be removed.
     */
    BUILT_IN,
    /**
     * An automatically generated channel created when sending or receiving a private message from
     * another player on the Hypixel network.
     */
    PRIVATE_MESSAGE(true);

    private final boolean removeable;

    private ChatChannelType(boolean removeable) {
        this.removeable = removeable;
    }

    private ChatChannelType() {
        this(false);
    }

    /**
     * Check whether or not this chat channel type is allowed to be deleted by the client.
     *
     * @return true if removeable, false otherwise
     */
    public boolean isRemovable() {
        return removeable;
    }

}
