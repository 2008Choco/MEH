package wtf.choco.meh.client.config;

import java.util.regex.Pattern;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry.Translatable;

import org.jetbrains.annotations.NotNull;

import wtf.choco.meh.client.chat.filter.ChatMessageFilter;

/**
 * A type of chat message filter that may be configured by the client.
 */
public enum FilterType implements Translatable {

    /**
     * A filter that may match a regular expression.
     *
     * @see ChatMessageFilter#regex(Pattern, boolean)
     */
    REGEX,

    /**
     * A filter that must match a regular expression in its entirety.
     *
     * @see ChatMessageFilter#regex(Pattern, boolean)
     */
    REGEX_EXACT,

    /**
     * A filter that requires a string of text at the start of the message.
     *
     * @see ChatMessageFilter#startsWith(String)
     */
    STARTS_WITH;

    @NotNull
    @Override
    public String getKey() {
        return "meh.channel.focus.type." + name().toLowerCase();
    }

}
