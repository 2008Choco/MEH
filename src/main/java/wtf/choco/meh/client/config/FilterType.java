package wtf.choco.meh.client.config;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry.Translatable;

import org.jetbrains.annotations.NotNull;

public enum FilterType implements Translatable {

    REGEX,
    REGEX_EXACT,
    STARTS_WITH;

    @NotNull
    @Override
    public String getKey() {
        return "meh.channel.focus.type." + name().toLowerCase();
    }

}
