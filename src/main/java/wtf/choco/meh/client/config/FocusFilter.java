package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.chat.filter.ChatMessageFilter;

public final class FocusFilter {

    @ConfigEntry.Gui.Tooltip(count = 4)
    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    private FilterType filter_type;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    private String filter;

    FocusFilter(FilterType filterType, String filter) {
        this.filter_type = filterType;
        this.filter = filter;
    }

    @Nullable
    public ChatMessageFilter toChatMessageFilter() {
        if (filter == null || filter.isEmpty()) {
            return null;
        }

        if (filter_type == FilterType.REGEX) {
            return ChatMessageFilter.regex(filter, false);
        } else if (filter_type == FilterType.REGEX_EXACT) {
            return ChatMessageFilter.regex(filter, true);
        } else if (filter_type == FilterType.STARTS_WITH) {
            return ChatMessageFilter.startsWith(filter);
        }

        throw new UnsupportedOperationException("Unknown filter implementation for FilterType." + filter_type.name());
    }

}
