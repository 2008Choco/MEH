package wtf.choco.meh.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup.Provider;

import wtf.choco.meh.client.MEHKeybinds;
import wtf.choco.meh.client.chat.emote.HypixelChatEmote;
import wtf.choco.meh.client.mnemonic.Mnemonic;
import wtf.choco.meh.client.mnemonic.Mnemonics;
import wtf.choco.meh.client.server.HypixelServerType;

class MEHLanguageProvider extends FabricLanguageProvider {

    protected MEHLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(Provider registryLookup, TranslationBuilder translation) {
        translation.add(MEHKeybinds.CATEGORY_MEH, dataOutput.getModContainer().getMetadata().getName());

        // Hypixel Emotes
        this.add(translation, HypixelChatEmote.HEART, "Heart");
        this.add(translation, HypixelChatEmote.STAR, "Star");
        this.add(translation, HypixelChatEmote.YES, "Yes");
        this.add(translation, HypixelChatEmote.NO, "No");
        this.add(translation, HypixelChatEmote.JAVA, "Java");
        this.add(translation, HypixelChatEmote.ARROW, "Arrow");
        this.add(translation, HypixelChatEmote.SHRUG, "Shrug");
        this.add(translation, HypixelChatEmote.TABLE_FLIP, "Table Flip");
        this.add(translation, HypixelChatEmote.WAVE, "Wave");
        this.add(translation, HypixelChatEmote.COUNTING, "Counting");
        this.add(translation, HypixelChatEmote.TOTEM, "Totem");
        this.add(translation, HypixelChatEmote.TYPING, "Typing...");
        this.add(translation, HypixelChatEmote.MATHS, "Maths");
        this.add(translation, HypixelChatEmote.SNAIL, "Snail");
        this.add(translation, HypixelChatEmote.THINKING, "Thinking");
        this.add(translation, HypixelChatEmote.GIMME, "Gimme");
        this.add(translation, HypixelChatEmote.WIZARD, "Wizard");
        this.add(translation, HypixelChatEmote.PVP, "PvP");
        this.add(translation, HypixelChatEmote.PEACE, "Peace");
        this.add(translation, HypixelChatEmote.OOF, "OOF");
        this.add(translation, HypixelChatEmote.PUFFER, "Puffer");
        this.add(translation, HypixelChatEmote.GLEE_LOWER, "Glee (Lower)");
        this.add(translation, HypixelChatEmote.GLEE_UPPER, "Glee (Upper)");
        this.add(translation, HypixelChatEmote.CUTE, "Cute");
        this.add(translation, HypixelChatEmote.DAB, "Dab");
        this.add(translation, HypixelChatEmote.YEY, "yey");
        this.add(translation, HypixelChatEmote.DJ, "DJ");
        this.add(translation, HypixelChatEmote.DOG, "Dog");
        this.add(translation, HypixelChatEmote.CAT, "Cat");
        this.add(translation, HypixelChatEmote.EXCITED_WAVE, "Excited Wave");
        this.add(translation, HypixelChatEmote.SLOTH, "Sloth");
        this.add(translation, HypixelChatEmote.SNOW, "Snow");

        // Hypixel Server Types
        this.add(translation, HypixelServerType.MAIN_LOBBY, "Main Lobby");
        this.add(translation, HypixelServerType.ARCADE, "Arcade Games");
        this.add(translation, HypixelServerType.BEDWARS, "Bedwars");
        this.add(translation, HypixelServerType.BLITZ_SURVIVAL_GAMES, "Blitz Survival Games");
        this.add(translation, HypixelServerType.BUILD_BATTLE, "Build Battle");
        this.add(translation, HypixelServerType.CLASSIC_GAMES, "Classic Games");
        this.add(translation, HypixelServerType.COPS_AND_CRIMS, "Cops and Crims");
        this.add(translation, HypixelServerType.DUELS, "Duels");
        this.add(translation, HypixelServerType.HOUSING, "Housing");
        this.add(translation, HypixelServerType.MEGA_WALLS, "Mega Walls");
        this.add(translation, HypixelServerType.MURDER_MYSTERY, "Murder Mystery");
        this.add(translation, HypixelServerType.PIT, "The Pit");
        this.add(translation, HypixelServerType.PROTOTYPE, "Prototype");
        this.add(translation, HypixelServerType.SKYBLOCK, "SkyBlock");
        this.add(translation, HypixelServerType.SKYWARS, "SkyWars");
        this.add(translation, HypixelServerType.SMASH_HEROES, "Smash Heroes");
        this.add(translation, HypixelServerType.SMP, "SMP");
        this.add(translation, HypixelServerType.TNT_GAMES, "The TNT Games");
        this.add(translation, HypixelServerType.UHC, "UHC Champions");
        this.add(translation, HypixelServerType.WARLORDS, "Warlords");
        this.add(translation, HypixelServerType.WOOL_GAMES, "Wool Games");
        this.add(translation, HypixelServerType.UNKNOWN, "Unknown");

        // Mnemonics
        this.add(translation, Mnemonics.GG, "GG");

        translation.add("text.autoconfig.meh.title", dataOutput.getModContainer().getMetadata().getName() + " Options");
        this.addSetting(translation, "enabled_features", "Enabled Features");
        this.addSetting(translation, "enabled_features.chat_channels", "Chat Channels", "Organize your chat better with simple chat channels.");
        this.addSetting(translation, "enabled_features.manual_gg", "Manual GG", "Write \"gg\" in chat after pressing the G key twice.\nOpening chat is not necessary!");
        this.addSetting(translation, "enabled_features.emote_selector", "Emote Selector", "A visual selection widget for Hypixel's emote system.");
        this.addSetting(translation, "enabled_features.main_lobby_fishing", "Main Lobby Fishing");
        this.addSetting(translation, "enabled_features.main_lobby_fishing.retextured_fishing_rods", "Retextured Fishing Rods", "Fishing rods used in main lobby fishing will use custom textures.");
        this.addSetting(translation, "auto_switch_on_new_message", "Auto Switch on New Message", "When receiving a new private message, automatically switch\nto the newly created channel (if your chat is closed).");
        this.addSetting(translation, "known_channels", "Known Channels", "A list of channels loaded into the client by default. Channels listed here are not removeable.");
        this.addSetting(translation, "KnownChannel", "Known Channel");
        this.addSetting(translation, "KnownChannel.id", "Channel Id");
        this.addSetting(translation, "KnownChannel.name", "Channel Name");
        this.addSetting(translation, "KnownChannel.color", "Channel Color");
        this.addSetting(translation, "KnownChannel.command_prefix", "Command Prefix", "The command prefix to use in order to chat in this channel. '/' is optional.");

        // Loose translations
        translation.add("meh.channel.switch", "Chat channel switched to %s");
        translation.add("meh.channel.new.msg", "Creating new chat channel for %s.");
        translation.add("meh.channel.all.name", "All");
    }

    private void add(TranslationBuilder translation, HypixelChatEmote emote, String name) {
        translation.add(emote.getDescriptionKey(), name);
    }

    private void add(TranslationBuilder translation, HypixelServerType server, String name) {
        translation.add(server.getDescriptionKey(), name);
    }

    private void add(TranslationBuilder translation, Mnemonic mnemonic, String name) {
        translation.add(mnemonic.getDescriptionKey(), name);
    }

    private void addSetting(TranslationBuilder translation, String setting, String name) {
        translation.add("text.autoconfig.meh.option." + setting, name);
    }

    private void addSetting(TranslationBuilder translation, String setting, String name, String tooltip) {
        this.addSetting(translation, setting, name);
        this.addSetting(translation, setting + ".@Tooltip", tooltip);
    }

}
