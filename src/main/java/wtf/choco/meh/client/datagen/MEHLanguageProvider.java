package wtf.choco.meh.client.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.HolderLookup.Provider;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.emote.HypixelChatEmote;
import wtf.choco.meh.client.config.FilterType;
import wtf.choco.meh.client.keybind.MEHKeybinds;
import wtf.choco.meh.client.mnemonic.Mnemonic;
import wtf.choco.meh.client.mnemonic.Mnemonics;
import wtf.choco.meh.client.screen.widgets.PartyListWidget;
import wtf.choco.meh.client.server.HypixelServerType;

public final class MEHLanguageProvider extends FabricLanguageProvider {

    private static final String AUTOCONFIG = "text.autoconfig." + MEHClient.MOD_ID;

    protected MEHLanguageProvider(FabricDataOutput output, CompletableFuture<Provider> registryLookup) {
        super(output, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(Provider registryLookup, TranslationBuilder builder) {
        // Literals
        builder.add("meh.channel.all.name", "All");
        builder.add("meh.channel.focus.tooltip", "Focus mode enabled. Press %s to disable.");
        builder.add("meh.channel.new.msg", "Creating new chat channel for %s.");
        builder.add("meh.channel.switch", "Chat channel switched to %s.");
        builder.add("meh.housing.auto_night_vision.success", "Found a night vision command (%s) and executed it. Enjoy night vision!");
        builder.add("meh.party_manager.invitation.decline", "Declined party invitation from %s.");
        builder.add("meh.party_manager.invitation.empty", "You do not have any pending party invitations.");

        builder.add("gui.meh.custom_status.title", "Your Custom Statuses");
        builder.add("gui.meh.custom_status.info", """
                What's this?
                
                You've stumbled across a tool used by a Hypixel Staff member! Hypixel Staff are able to use custom lobby statuses, and this is a handy editor/dictionary for those statuses.
                
                This is likely of no use to you. Unless you're a staff member, in which case, you're in luck!""".translateEscapes());
        builder.add("gui.meh.custom_status.empty.header", "No custom statuses saved :(");
        builder.add("gui.meh.custom_status.empty.footer", "Why not add some?");
        builder.add("gui.meh.custom_status.button.add_status.narration", "Add Status");
        builder.add("gui.meh.custom_status.button.add_status.tooltip", "Create and add a new status.");
        builder.add("gui.meh.custom_status.button.delete_status.narration", "Delete Status");
        builder.add("gui.meh.custom_status.button.delete_status.tooltip", "Delete the selected status.");
        builder.add("gui.meh.custom_status.button.edit_status.narration", "Edit Status");
        builder.add("gui.meh.custom_status.button.edit_status.tooltip", "Edit the selected status.");
        builder.add("gui.meh.custom_status.button.apply_status.narration", "Apply Status");
        builder.add("gui.meh.custom_status.button.apply_status.tooltip", "Apply the selected status.");
        builder.add("gui.meh.custom_status.edit_box.status_text.narration", "Status Text");
        builder.add("gui.meh.custom_status.edit_box.status_text.hint", "Status text...");

        // Keybinds
        builder.add(MEHKeybinds.CATEGORY_MEH, "MEH");
        this.add(builder, MEHKeybinds.PARTY_INVITE_ACCEPT, "Accept Party Invite");
        this.add(builder, MEHKeybinds.PARTY_INVITE_DECLINE, "Decline Party Invite");
        /*
        // AMECS keybinds
        this.add(builder, MEHKeybinds.DELETE_CHANNEL, "Delete Channel", "Delete the current chat channel (while chat is open).\nCtrl is highly recommended for this keybind!");
        this.add(builder, MEHKeybinds.EMOTE_SELECTOR, "Emote Selector", "Open or close the emote selector (while chat is open).\nCtrl is highly recommended for this keybind!");
        this.add(builder, MEHKeybinds.SWITCH_CHANNEL, "Switch Channel (Next)", "Switch to the next chat channel (while chat is open).\nCtrl is highly recommended for this keybind!");
        this.add(builder, MEHKeybinds.SWITCH_CHANNEL_PREVIOUS, "Switch Channel (Previous)", "Switch to the previous chat channel (while chat is open).\nCtrl is highly recommended for this keybind!");
        this.add(builder, MEHKeybinds.TOGGLE_FOCUS_MODE, "Toggle Focus Mode", "Toggle the chat channel focus mode (while chat is open).\nCtrl is highly recommended for this keybind!");
        */

        // Constants
        this.add(builder, HypixelChatEmote.ARROW, "Arrow");
        this.add(builder, HypixelChatEmote.CAT, "Cat");
        this.add(builder, HypixelChatEmote.COUNTING, "Counting");
        this.add(builder, HypixelChatEmote.CUTE, "Cute");
        this.add(builder, HypixelChatEmote.DAB, "Dab");
        this.add(builder, HypixelChatEmote.DJ, "DJ");
        this.add(builder, HypixelChatEmote.DOG, "Dog");
        this.add(builder, HypixelChatEmote.EXCITED_WAVE, "Excited Wave");
        this.add(builder, HypixelChatEmote.GIMME, "Gimme");
        this.add(builder, HypixelChatEmote.GLEE_LOWER, "Glee (Lower)");
        this.add(builder, HypixelChatEmote.GLEE_UPPER, "Glee (Upper)");
        this.add(builder, HypixelChatEmote.HEART, "Heart");
        this.add(builder, HypixelChatEmote.JAVA, "Java");
        this.add(builder, HypixelChatEmote.MATHS, "Maths");
        this.add(builder, HypixelChatEmote.NO, "No");
        this.add(builder, HypixelChatEmote.OOF, "OOF");
        this.add(builder, HypixelChatEmote.PEACE, "Peace");
        this.add(builder, HypixelChatEmote.PUFFER, "Puffer");
        this.add(builder, HypixelChatEmote.PVP, "PvP");
        this.add(builder, HypixelChatEmote.SHRUG, "Shrug");
        this.add(builder, HypixelChatEmote.SLOTH, "Sloth");
        this.add(builder, HypixelChatEmote.SNAIL, "Snail");
        this.add(builder, HypixelChatEmote.SNOW, "Snow");
        this.add(builder, HypixelChatEmote.STAR, "Star");
        this.add(builder, HypixelChatEmote.TABLE_FLIP, "Table Flip");
        this.add(builder, HypixelChatEmote.THINKING, "Thinking");
        this.add(builder, HypixelChatEmote.TOTEM, "Totem");
        this.add(builder, HypixelChatEmote.TYPING, "Typing...");
        this.add(builder, HypixelChatEmote.WAVE, "Wave");
        this.add(builder, HypixelChatEmote.WIZARD, "Wizard");
        this.add(builder, HypixelChatEmote.YES, "Yes");
        this.add(builder, HypixelChatEmote.YEY, "yey");
        this.add(builder, HypixelServerType.ARCADE, "Arcade Games");
        this.add(builder, HypixelServerType.BEDWARS, "Bedwars");
        this.add(builder, HypixelServerType.BLITZ_SURVIVAL_GAMES, "Blitz Survival Games");
        this.add(builder, HypixelServerType.BUILD_BATTLE, "Build Battle");
        this.add(builder, HypixelServerType.CLASSIC_GAMES, "Classic Games");
        this.add(builder, HypixelServerType.COPS_AND_CRIMS, "Cops and Crims");
        this.add(builder, HypixelServerType.DUELS, "Duels");
        this.add(builder, HypixelServerType.HOUSING, "Housing");
        this.add(builder, HypixelServerType.MAIN_LOBBY, "Main Lobby");
        this.add(builder, HypixelServerType.MEGA_WALLS, "Mega Walls");
        this.add(builder, HypixelServerType.MURDER_MYSTERY, "Murder Mystery");
        this.add(builder, HypixelServerType.PAINTBALL, "Paintball");
        this.add(builder, HypixelServerType.PIT, "The Pit");
        this.add(builder, HypixelServerType.PROTOTYPE, "Prototype");
        this.add(builder, HypixelServerType.QUAKECRAFT, "Quakecraft");
        this.add(builder, HypixelServerType.REPLAY, "Replay");
        this.add(builder, HypixelServerType.SKYBLOCK, "SkyBlock");
        this.add(builder, HypixelServerType.SKYWARS, "SkyWars");
        this.add(builder, HypixelServerType.SMASH_HEROES, "Smash Heroes");
        this.add(builder, HypixelServerType.SMP, "SMP");
        this.add(builder, HypixelServerType.SPEED_UHC, "Speed UHC");
        this.add(builder, HypixelServerType.TOURNAMENT_LOBBY, "Tournament Hall");
        this.add(builder, HypixelServerType.TNT_GAMES, "The TNT Games");
        this.add(builder, HypixelServerType.UHC, "UHC Champions");
        this.add(builder, HypixelServerType.VAMPIREZ, "VampireZ");
        this.add(builder, HypixelServerType.WALLS, "Walls");
        this.add(builder, HypixelServerType.UNKNOWN, "Unknown");
        this.add(builder, HypixelServerType.WARLORDS, "Warlords");
        this.add(builder, HypixelServerType.WOOL_GAMES, "Wool Games");
        this.add(builder, FilterType.REGEX, "RegEx");
        this.add(builder, FilterType.REGEX_EXACT, "RegEx (Exact)");
        this.add(builder, FilterType.STARTS_WITH, "Starts With");
        this.add(builder, PartyListWidget.Position.TOP_LEFT, "Top Left");
        this.add(builder, PartyListWidget.Position.TOP_RIGHT, "Top Right");
        this.add(builder, Mnemonics.GC, "GC");
        this.add(builder, Mnemonics.GG, "GG");

        // Configuration options
        this.addPrefix(builder, "max_remembered_chat_history", "General Options");
        this.addPrefix(builder, "chat_channels", "Feature Options");

        this.addOption(builder, "max_remembered_chat_history", "Max Remembered Chat History",
            "The amount of chat messages Minecraft will remember.",
            "More in-memory messages means more focused messages.",
            "Higher numbers consume more memory! Be careful!",
            "By default, Minecraft remembers only 100 messages."
        );
        this.addOption(builder, "quiet_thunder", "Quiet Thunder",
            "Hypixel uses lots of lightning, but it's so loud!",
            "When enabled, reduces the volume of thunder sounds to a reasonable level."
        );
        this.addOption(builder, "chat_channels", "Chat Channels", "Organize your chat better with simple chat channels.");
        this.addOption(builder, "chat_channels.auto_switch_on_new_message", "Auto Switch on New Message",
            "When receiving a new private message, automatically switch to the newly created channel.",
            "The channel will only change if your chat is closed."
        );
        this.addOption(builder, "chat_channels.enabled", "Enabled");
        this.addOption(builder, "chat_channels.known_channels", "Known Channels", "A list of channels loaded into the client by default. Channels listed here are not removable.");
        this.addOption(builder, "emote_selector", "Emote Selector", "A visual selection widget for Hypixel's emote system.");
        this.addOption(builder, "emote_selector.enabled", "Enabled");
        this.addOption(builder, "housing", "Housing", "Enhancements to Hypixel's Housing servers.");
        this.addOption(builder, "housing.auto_disable_flight", "Auto-Disable Flight", "When enabled, automatically runs /fly to disable flight when joining a House.");
        this.addOption(builder, "housing.auto_night_vision", "Auto Night Vision",
            "When enabled, try to execute well-known commands to enable Night Vision.",
            "It will only try to execute commands that the client is aware of!",
            "If a housing doesn't send commands to the client, this will not work!"
        );
        this.addOption(builder, "KnownChannel", "Known Channel");
        this.addOption(builder, "KnownChannel.color", "Channel Color");
        this.addOption(builder, "KnownChannel.command_prefix", "Command Prefix", "The command prefix to use in order to chat int his channel. '/' is optional.");
        this.addOption(builder, "KnownChannel.focus_filter", "Focus Filter Settings", "The filter settings to use for this channel while in focus mode.");
        this.addOption(builder, "KnownChannel.focus_filter.filter", "Filter", "The filter text to apply given the above rule set.");
        this.addOption(builder, "KnownChannel.focus_filter.filter_type", "Filter Type",
            "The way the provided filter will apply to text:",
            "RegEx: A Regular Expression that must match anywhere in the text.",
            "RegEx (Exact): A Regular Expression that must match the whole text exactly.",
            "Starts With: The message must start with the given text."
        );
        this.addOption(builder, "KnownChannel.id", "Channel Id");
        this.addOption(builder, "KnownChannel.name", "Channel Name");
        this.addOption(builder, "main_lobby_fishing", "Main Lobby Fishing", "Enhancements to Hypixel's main lobby fishing experience.");
        this.addOption(builder, "main_lobby_fishing.retextured_fishing_rods", "Retextured Fishing Rods", "Fishing rods used in main lobby fishing will use custom textures.");
        this.addOption(builder, "mnemonics", "Mnemonics", "Quick, short-hand key combinations that perform some action.");
        this.addOption(builder, "mnemonics.gc", "Good Catch! (gc)",
            "Press 'g' and 'c' in quick succession to write \"gc\" in chat!",
            "Used by fishers to show good sportsmanship after a rare catch!"
        );
        this.addOption(builder, "mnemonics.gg", "Good Game! (gg)", "Press 'g' and 'g' in quick succession to write \"gg\" in chat!");
        this.addOption(builder, "murder_mystery", "Murder Mystery", "Enhancements to Hypixel's Murder Mystery minigame.");
        this.addOption(builder, "murder_mystery.retextured_murderer_knives", "Retextured Murderer Knives", "The cosmetic murderer knives will use custom textures.");
        this.addOption(builder, "party_manager", "Party Manager", "Options to customize the appearance and behavior of Party Manager features.");
        this.addOption(builder, "party_manager.enabled", "Enabled");
        this.addOption(builder, "party_manager.party_list_position", "Party List Position");
        builder.add(AUTOCONFIG + ".title", "MEH Options");
    }

    /*
    // Unused until AMECS updates their API
    private void add(TranslationBuilder builder, KeyMapping keybind, String name, String amecsDescription) {
        this.add(builder, keybind, name);
        builder.add(keybind.getName() + ".amecsapi.description", amecsDescription);
    }
    */

    private void add(TranslationBuilder builder, KeyMapping keybind, String name) {
        builder.add(keybind.getName(), name);
    }

    private void add(TranslationBuilder builder, HypixelChatEmote emote, String name) {
        builder.add(emote.getDescriptionKey(), name);
    }

    private void add(TranslationBuilder builder, HypixelServerType serverType, String name) {
        builder.add(serverType.getDescriptionKey(), name);
    }

    private void add(TranslationBuilder builder, PartyListWidget.Position position, String name) {
        builder.add(position.getKey(), name);
    }

    private void add(TranslationBuilder builder, FilterType filterType, String name) {
        builder.add(filterType.getKey(), name);
    }

    private void add(TranslationBuilder builder, Mnemonic mnemonic, String name) {
        builder.add(mnemonic.getDescriptionKey(), name);
    }

    private void addOption(TranslationBuilder builder, String path, String name, String... tooltip) {
        this.addOption(builder, path, name);

        if (tooltip.length == 1) {
            builder.add(AUTOCONFIG + ".option." + path + ".@Tooltip", tooltip[0]);
        } else if (tooltip.length > 1) {
            for (int i = 0; i < tooltip.length; i++) {
                builder.add(AUTOCONFIG + ".option." + path + ".@Tooltip[" + i + "]", tooltip[i]);
            }
        }
    }

    private void addOption(TranslationBuilder builder, String path, String name) {
        builder.add(AUTOCONFIG + ".option." + path, name);
    }

    private void addPrefix(TranslationBuilder builder, String path, String name) {
        builder.add(AUTOCONFIG + ".option." + path + ".@PrefixText", name);
    }

}
