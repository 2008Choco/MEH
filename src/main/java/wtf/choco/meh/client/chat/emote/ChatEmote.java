package wtf.choco.meh.client.chat.emote;

import net.minecraft.network.chat.Component;

/**
 * Represents an emote that can be sent in chat.
 */
public interface ChatEmote {

    /**
     * Get the text that can/will be inserted into the chat box when the emote is selected
     * by the user. This text should generally be unformatted.
     *
     * @return the input text inserted into the chat box
     */
    public String getInputText();

    /**
     * Get a {@link Component} displaying how this emote would be rendered in chat.
     * <p>
     * By default this is just a literal component of {@link #getInputText()}, but in the
     * case where a custom/modded server can interpret the emote and display it differently
     * with formatting, colours, or other icons, this should return what the user will see
     * after it has been processed by the server (if the client is aware of such processing).
     * For example, {@code getChatBoxText()} might be {@code ":hug:"}, but the emote display
     * component might be {@literal <(^-^)>}, serialized as {"text":"<(^-^)>", "color": "red"}.
     * <p>
     * The mod will render this display text in the emote selector GUI.
     *
     * @return the emote display text
     */
    public default Component getEmoteDisplayText() {
        return Component.literal(getInputText());
    }

    /**
     * Get the display name of this emote.
     *
     * @return the display name
     */
    public Component getDisplayName();

}
