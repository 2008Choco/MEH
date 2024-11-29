package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import wtf.choco.meh.client.MEHClient;

@Config(name = MEHClient.MOD_ID)
public final class MEHConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip(count = 4)
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(min = 100, max = 1000)
    private int max_remembered_chat_history = 250;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigAutoDisableHousingFlight auto_disable_housing_flight = new ConfigAutoDisableHousingFlight();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigChatChannels chat_channels = new ConfigChatChannels();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigEmoteSelector emote_selector = new ConfigEmoteSelector();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigMainLobbyFishing main_lobby_fishing = new ConfigMainLobbyFishing();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigMnemonics mnemonics = new ConfigMnemonics();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigPartyManager party_manager = new ConfigPartyManager();

    public int getMaxRememberedChatHistory() {
        return max_remembered_chat_history;
    }

    public ConfigAutoDisableHousingFlight getAutoDisableHousingFlightConfig() {
        return auto_disable_housing_flight;
    }

    public ConfigChatChannels getChatChannelsConfig() {
        return chat_channels;
    }

    public ConfigEmoteSelector getEmoteSelectorConfig() {
        return emote_selector;
    }

    public ConfigMainLobbyFishing getMainLobbyFishingConfig() {
        return main_lobby_fishing;
    }

    public ConfigMnemonics getMnemonicsConfig() {
        return mnemonics;
    }

    public ConfigPartyManager getPartyManager() {
        return party_manager;
    }

}
