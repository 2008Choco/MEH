package wtf.choco.meh.client.config;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

import net.minecraft.Util;

public final class ConfigChatChannels implements Enableable {

    private boolean enabled = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    private boolean auto_switch_on_new_message = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    private List<KnownChannel> known_channels = Util.make(new ArrayList<>(), channels -> {
        channels.add(new KnownChannel("party", "Party", 0x84C5DB, "pc", FilterType.STARTS_WITH, "Party >"));
        channels.add(new KnownChannel("guild", "Guild", 0xEB3A09, "gc", FilterType.STARTS_WITH, "Guild >"));
    });

    ConfigChatChannels() { }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAutoSwitchOnNewMessage() {
        return auto_switch_on_new_message;
    }

    public List<KnownChannel> getKnownChannels() {
        return known_channels;
    }

}
