package wtf.choco.meh.client.config;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import net.minecraft.Util;

import wtf.choco.meh.client.MEHClient;

@Config(name = MEHClient.MOD_ID)
public final class MEHConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    private EnabledFeatures enabled_features = new EnabledFeatures();

    @ConfigEntry.Gui.Tooltip
    private boolean auto_switch_on_new_message = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    private List<KnownChannel> known_channels = Util.make(new ArrayList<>(), channels -> {
        channels.add(new KnownChannel("party", "Party", 0x84C5DB, "pc"));
        channels.add(new KnownChannel("guild", "Guild", 0xEB3A09, "gc"));
    });

    public boolean areChatChannelsEnabled() {
        return enabled_features.chat_channels;
    }

    public boolean isManualGGEnabled() {
        return enabled_features.manual_gg;
    }

    public boolean isEmoteSelectorEnabled() {
        return enabled_features.emote_selector;
    }

    public boolean isAutoSwitchOnNewMessage() {
        return auto_switch_on_new_message;
    }

    public List<KnownChannel> getKnownChannels() {
        return known_channels;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        for (KnownChannel channel : known_channels) {
            if (channel.command_prefix.strip().equals("/")) {
                throw new ValidationException("Command prefix must not be a single slash character!");
            }

            if (channel.command_prefix.isBlank()) {
                throw new ValidationException("Command prefix must not be empty!");
            }
        }
    }

    public static final class EnabledFeatures {

        @ConfigEntry.Gui.Tooltip
        private boolean chat_channels = true;

        @ConfigEntry.Gui.Tooltip
        private boolean emote_selector = true;

        @ConfigEntry.Gui.Tooltip
        private boolean manual_gg = true;

    }

    public static final class KnownChannel {

        private String id;
        private String name;

        @ConfigEntry.ColorPicker
        private int color;

        @ConfigEntry.Gui.Tooltip
        private String command_prefix;

        public KnownChannel(String id, String name, int color, String commandPrefix) {
            this.id = id;
            this.name = name;
            this.color = color;
            this.command_prefix = commandPrefix;
        }

        public KnownChannel() {
            this("unknown", "Unknown", 0, "");
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getColor() {
            return color;
        }

        public String getCommandPrefix() {
            return command_prefix;
        }

    }

}
