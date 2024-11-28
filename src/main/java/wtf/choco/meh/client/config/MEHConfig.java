package wtf.choco.meh.client.config;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;

import net.minecraft.Util;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.filter.ChatMessageFilter;
import wtf.choco.meh.client.screen.widgets.PartyListWidget;

@Config(name = MEHClient.MOD_ID)
public final class MEHConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    private EnabledFeatures enabled_features = new EnabledFeatures();

    @ConfigEntry.Gui.Tooltip
    private boolean auto_switch_on_new_message = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 100, max = 1000)
    private int max_remembered_chat_history = 250;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    private PartyManager party_manager = new PartyManager();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    private List<KnownChannel> known_channels = Util.make(new ArrayList<>(), channels -> {
        channels.add(new KnownChannel("party", "Party", 0x84C5DB, "pc", FilterType.STARTS_WITH, "Party >"));
        channels.add(new KnownChannel("guild", "Guild", 0xEB3A09, "gc", FilterType.STARTS_WITH, "Guild >"));
    });

    public boolean areChatChannelsEnabled() {
        return enabled_features.chat_channels;
    }

    public boolean isGGMnemonicEnabled() {
        return enabled_features.mnemonics.gg;
    }

    public boolean isGCMnemonicEnabled() {
        return enabled_features.mnemonics.gc;
    }

    public boolean isEmoteSelectorEnabled() {
        return enabled_features.emote_selector;
    }

    public boolean isRetexturedFishingRodsEnabled() {
        return enabled_features.main_lobby_fishing.retextured_fishing_rods;
    }

    public boolean isPartyManagerEnabled() {
        return enabled_features.party_manager;
    }

    public boolean isAutoDisableHousingFlightEnabled() {
        return enabled_features.auto_disable_housing_flight;
    }

    public boolean isAutoSwitchOnNewMessage() {
        return auto_switch_on_new_message;
    }

    public int getMaxRememberedChatHistory() {
        return max_remembered_chat_history;
    }

    public PartyManager getPartyManager() {
        return party_manager;
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
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private Mnemonics mnemonics = new Mnemonics();

        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private MainLobbyFishing main_lobby_fishing = new MainLobbyFishing();

        @ConfigEntry.Gui.Tooltip
        private boolean auto_disable_housing_flight = false;

        @ConfigEntry.Gui.Tooltip
        private boolean party_manager = true;

    }

    public static final class KnownChannel {

        @ConfigEntry.Gui.RequiresRestart
        private String id;
        @ConfigEntry.Gui.RequiresRestart
        private String name;

        @ConfigEntry.ColorPicker
        @ConfigEntry.Gui.RequiresRestart
        private int color;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        private String command_prefix;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.CollapsibleObject
        private FocusFilter focus_filter;

        KnownChannel(String id, String name, int color, String commandPrefix, FilterType filterType, String filter) {
            this.id = id;
            this.name = name;
            this.color = color;
            this.command_prefix = commandPrefix;
            this.focus_filter = new FocusFilter(filterType, filter);
        }

        KnownChannel() {
            this("unknown", "Unknown", 0, "", FilterType.STARTS_WITH, "");
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

        public FocusFilter getFocusFilter() {
            return focus_filter;
        }

    }

    public static final class FocusFilter {

        @ConfigEntry.Gui.Tooltip
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

    public static final class Mnemonics {

        @ConfigEntry.Gui.Tooltip
        private boolean gg = true;

        @ConfigEntry.Gui.Tooltip
        private boolean gc = true;

    }

    public static final class MainLobbyFishing {

        @ConfigEntry.Gui.Tooltip
        private boolean retextured_fishing_rods = true;

    }

    public static final class PartyManager {

        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
        private PartyListWidget.Position party_list_position = PartyListWidget.Position.TOP_LEFT;

        public PartyListWidget.Position getPartyListPosition() {
            return party_list_position;
        }

    }

}
