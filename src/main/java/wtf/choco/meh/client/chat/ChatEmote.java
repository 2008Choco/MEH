package wtf.choco.meh.client.chat;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import wtf.choco.meh.client.registry.MEHRegistries;
import wtf.choco.meh.client.util.Translatable;

/**
 *
 * @param requiredRankGifts the amount of rank gifts required to use this emote, or 0 if none are required
 * @param chatInput the string of text that the user must type in the chat box to use this emote, e.g. ":heart:"
 * @param display the actual emote text
 */
public record ChatEmote(int requiredRankGifts, String chatInput, Component display) implements Translatable {

    @Override
    public String getDescriptionKey() {
        Identifier key = MEHRegistries.CHAT_EMOTES.getKey(this);
        if (key == null) {
            throw new UnsupportedOperationException("Missing registry entry for " + this + ". Can't get description key.");
        }

        return "meh.hypixel.emote." + key.getPath();
    }

}
