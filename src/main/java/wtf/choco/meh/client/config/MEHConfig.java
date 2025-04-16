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

    @ConfigEntry.Gui.Tooltip(count = 2)
    private boolean quiet_thunder = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigChatChannels chat_channels = new ConfigChatChannels();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigEmoteSelector emote_selector = new ConfigEmoteSelector();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigHousing housing = new ConfigHousing();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigMainLobbyFishing main_lobby_fishing = new ConfigMainLobbyFishing();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigMnemonics mnemonics = new ConfigMnemonics();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigMurderMystery murder_mystery = new ConfigMurderMystery();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private ConfigPartyManager party_manager = new ConfigPartyManager();

    public int getMaxRememberedChatHistory() {
        return max_remembered_chat_history;
    }

    public boolean isQuietThunder() {
        return quiet_thunder;
    }

    public ConfigChatChannels getChatChannelsConfig() {
        return chat_channels;
    }

    public ConfigEmoteSelector getEmoteSelectorConfig() {
        return emote_selector;
    }

    public ConfigHousing getHousingConfig() {
        return housing;
    }

    public ConfigMainLobbyFishing getMainLobbyFishingConfig() {
        return main_lobby_fishing;
    }

    public ConfigMnemonics getMnemonicsConfig() {
        return mnemonics;
    }

    public ConfigMurderMystery getMurderMysteryConfig() {
        return murder_mystery;
    }

    public ConfigPartyManager getPartyManager() {
        return party_manager;
    }

}
