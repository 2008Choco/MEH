package wtf.choco.meh.client.chat;

public enum ChatChannelType {

    GLOBAL,
    BUILT_IN,
    PRIVATE_MESSAGE;

    public boolean isRemovable() {
        return this == PRIVATE_MESSAGE;
    }

}
