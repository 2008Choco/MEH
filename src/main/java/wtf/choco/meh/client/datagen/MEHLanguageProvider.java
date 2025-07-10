package wtf.choco.meh.client.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.HolderLookup.Provider;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.emote.HypixelChatEmote;
import wtf.choco.meh.client.config.BooleanOperator;
import wtf.choco.meh.client.config.FilterType;
import wtf.choco.meh.client.fishing.CatchType;
import wtf.choco.meh.client.fishing.Creature;
import wtf.choco.meh.client.fishing.Fish;
import wtf.choco.meh.client.fishing.FishRarity;
import wtf.choco.meh.client.fishing.FishingEnvironment;
import wtf.choco.meh.client.fishing.Junk;
import wtf.choco.meh.client.fishing.MythicalFishType;
import wtf.choco.meh.client.fishing.Plant;
import wtf.choco.meh.client.fishing.QuantifiedTreasureType;
import wtf.choco.meh.client.fishing.Treasure;
import wtf.choco.meh.client.gui.PartyListHudElement;
import wtf.choco.meh.client.keybind.MEHKeybinds;
import wtf.choco.meh.client.mnemonic.Mnemonics;
import wtf.choco.meh.client.server.HypixelServerType;
import wtf.choco.meh.client.util.Translatable;

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
        builder.add("gui.meh.custom_status.not_in_lobby", "You can only open the Custom Status screen when in a lobby!");
        builder.add("gui.meh.fishing_stats.caught", "%s Caught:");
        builder.add("gui.meh.fishing_stats.dock_master", "Dock Master");
        builder.add("gui.meh.fishing_stats.header", "Fishing Stats");
        builder.add("gui.meh.fishing_stats.incomplete", "Speak to the %s to get your stats!");

        // Keybinds
        builder.add(MEHKeybinds.CATEGORY_MEH, "MEH");
        this.add(builder, MEHKeybinds.OPEN_CUSTOM_STATUS_SCREEN, "Open Custom Status Screen");

        // Constants
        this.add(builder, CatchType.CREATURES, "Creatures");
        this.add(builder, CatchType.FISH, "Fish");
        this.add(builder, CatchType.JUNK, "Junk");
        this.add(builder, CatchType.MYTHICAL_FISH, "Mythical Fish");
        this.add(builder, CatchType.PLANTS, "Plants");
        this.add(builder, CatchType.SPECIAL_FISH, "Special Fish");
        this.add(builder, CatchType.TREASURE, "Treasure");
        this.add(builder, Creature.BLAZE, "Blaze");
        this.add(builder, Creature.CAVE_SPIDER, "Cave Spider");
        this.add(builder, Creature.CHICKEN, "Chicken");
        this.add(builder, Creature.COW, "Cow");
        this.add(builder, Creature.CREEPER, "Creeper");
        this.add(builder, Creature.MAGMA_CUBE, "Magma Cube");
        this.add(builder, Creature.PIG, "Pig");
        this.add(builder, Creature.PIG_ZOMBIE, "Pig Zombie");
        this.add(builder, Creature.SHEEP, "Sheep");
        this.add(builder, Creature.SKELETON, "Skeleton");
        this.add(builder, Creature.SLIME, "Slime");
        this.add(builder, Creature.SPIDER, "Spider");
        this.add(builder, Creature.SQUID, "Squid");
        this.add(builder, Creature.ZOMBIE, "Zombie");
        this.add(builder, Fish.CHARRED_PUFFERFISH, "Charred Pufferfish");
        this.add(builder, Fish.CLOWNFISH, "Clownfish");
        this.add(builder, Fish.COD, "Cod");
        this.add(builder, Fish.COOKED_COD, "Cooked Cod");
        this.add(builder, Fish.COOKED_SALMON, "Cooked Salmon");
        this.add(builder, Fish.PERCH, "Perch");
        this.add(builder, Fish.PIKE, "Pike");
        this.add(builder, Fish.PUFFERFISH, "Pufferfish");
        this.add(builder, Fish.SALMON, "Salmon");
        this.add(builder, Fish.SECRET_FISH, "Secret Fish");
        this.add(builder, Fish.TROUT, "Trout");
        this.add(builder, FishingEnvironment.ICE, "Ice");
        this.add(builder, FishingEnvironment.LAVA, "Lava");
        this.add(builder, FishingEnvironment.WATER, "Water");
        this.add(builder, FishRarity.COMMON, "Common");
        this.add(builder, FishRarity.RARE, "Rare");
        this.add(builder, FishRarity.ULTRA_RARE, "Ultra Rare");
        this.add(builder, FishRarity.UNCOMMON, "Uncommon");
        this.add(builder, Junk.BONE, "Bone");
        this.add(builder, Junk.BOWL, "Bowl");
        this.add(builder, Junk.BROKEN_FISHING_ROD, "Broken Fishing Rod");
        this.add(builder, Junk.BURNED_FLESH, "Burned Flesh");
        this.add(builder, Junk.CHARCOAL, "Charcoal");
        this.add(builder, Junk.CLUMP_OF_LEAVES, "Clump of Leaves");
        this.add(builder, Junk.COAL, "Coal");
        this.add(builder, Junk.FERMENTED_SPIDER_EYE, "Fermented Spider Eye");
        this.add(builder, Junk.FROZEN_FLESH, "Frozen Flesh");
        this.add(builder, Junk.ICE_SHARD, "Ice Shard");
        this.add(builder, Junk.INK_SAC, "Ink Sac");
        this.add(builder, Junk.LAVA_BUCKET, "Lava Bucket");
        this.add(builder, Junk.LEATHER, "Leather");
        this.add(builder, Junk.LEATHER_BOOTS, "Leather Boots");
        this.add(builder, Junk.LILY_PAD, "Lily Pad");
        this.add(builder, Junk.NETHER_BRICK, "Nether Brick");
        this.add(builder, Junk.RABBIT_HIDE, "Rabbit Hide");
        this.add(builder, Junk.ROTTEN_FLESH, "Rotten Flesh");
        this.add(builder, Junk.SNOWBALL, "Snowball");
        this.add(builder, Junk.SOGGY_PIECE_OF_PAPER, "Soggy Piece of Paper");
        this.add(builder, Junk.STEAK, "Steak");
        this.add(builder, Junk.STICK, "Stick");
        this.add(builder, Junk.STRING, "String");
        this.add(builder, Junk.TRIPWIRE_HOOK, "Tripwire Hook");
        this.add(builder, Junk.WATER_BOTTLE, "Water Bottle");
        this.add(builder, MythicalFishType.AUTOMATON_OF_DAEDALUS, "Automaton of Daedalus");
        this.add(builder, MythicalFishType.DUST_OF_SELENE, "Dust of Selene");
        this.add(builder, MythicalFishType.EMBER_OF_HELIOS, "Ember of Helios");
        this.add(builder, MythicalFishType.HEART_OF_APHRODITE, "Heart of Aphrodite");
        this.add(builder, MythicalFishType.SHADOW_OF_NYX, "Shadow of Nyx");
        this.add(builder, MythicalFishType.SPARK_OF_ZEUS, "Spark of Zeus");
        this.add(builder, MythicalFishType.SPIRIT_OF_DEMETER, "Spirit of Demeter");
        this.add(builder, MythicalFishType.WRATH_OF_HADES, "Wrath of Hades");
        this.add(builder, Plant.BAKED_POTATO, "Baked Potato");
        this.add(builder, Plant.BAMBOO, "Bamboo");
        this.add(builder, Plant.CHARRED_BERRIES, "Charred Berries");
        this.add(builder, Plant.DRIED_KELP, "Dried Kelp");
        this.add(builder, Plant.FROZEN_KELP, "Frozen Kelp");
        this.add(builder, Plant.GLISTERING_MELON, "Glistering Melon");
        this.add(builder, Plant.GLOW_BERRIES, "Glow Berries");
        this.add(builder, Plant.KELP, "Kelp");
        this.add(builder, Plant.MELON, "Melon");
        this.add(builder, Plant.NETHER_WART, "Nether Wart");
        this.add(builder, Plant.POTATO, "Potato");
        this.add(builder, Plant.SWEET_BERRIES, "Sweet Berries");
        this.add(builder, Plant.WARPED_ROOTS, "Warped Roots");
        this.add(builder, Plant.WHEAT, "Wheat");
        this.add(builder, QuantifiedTreasureType.ARCADE_GAMES_COINS, "Arcade Games Coins");
        this.add(builder, QuantifiedTreasureType.EVENT_EXPERIENCE, "Event Experience");
        this.add(builder, QuantifiedTreasureType.GUILD_EXPERIENCE, "Guild Experience");
        this.add(builder, QuantifiedTreasureType.HYPIXEL_EXPERIENCE, "Hypixel Experience");
        this.add(builder, Treasure.BLAZE_POWDER, "Blaze Powder");
        this.add(builder, Treasure.BLAZE_ROD, "Blaze Rod");
        this.add(builder, Treasure.CHAINMAIL_CHESTPLATE, "Chainmail Chestplate");
        this.add(builder, Treasure.COMPASS, "Compass");
        this.add(builder, Treasure.DIAMOND, "Diamond");
        this.add(builder, Treasure.DIAMOND_SWORD, "Diamond Sword");
        this.add(builder, Treasure.EMERALD, "Emerald");
        this.add(builder, Treasure.ENCHANTED_BOOK, "Enchanted Book");
        this.add(builder, Treasure.ENCHANTED_FISHING_ROD, "Enchanted Fishing Rod");
        this.add(builder, Treasure.EYE_OF_ENDER, "Eye of Ender");
        this.add(builder, Treasure.GOLD_PICKAXE, "Gold Pickaxe");
        this.add(builder, Treasure.GOLD_SWORD, "Gold Sword");
        this.add(builder, Treasure.IRON_SWORD, "Iron Sword");
        this.add(builder, Treasure.MAGMA_CREAM, "Magma Cream");
        this.add(builder, Treasure.MOLTEN_GOLD, "Molten Gold");
        this.add(builder, Treasure.NAME_TAG, "Name Tag");
        this.add(builder, Treasure.NAUTILUS_SHELL, "Nautilus Shell");
        this.add(builder, Treasure.SADDLE, "Saddle");
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
        this.add(builder, BooleanOperator.AND, "AND");
        this.add(builder, BooleanOperator.OR, "OR");
        this.add(builder, PartyListHudElement.Position.TOP_LEFT, "Top Left");
        this.add(builder, PartyListHudElement.Position.TOP_RIGHT, "Top Right");
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
        this.addOption(builder, "main_lobby_fishing.fishing_stat_overlay", "Stat Overlay",
            "Display fishing stats on your screen when fishing in the main lobby.",
            "You must speak to the Dockmaster before you can see your stats!"
        );
        this.addOption(builder, "main_lobby_fishing.fishing_stat_overlay.when_fishing", "When Fishing", "Display the stat overlay when Hypixel considers you 'fishing'.");
        this.addOption(builder, "main_lobby_fishing.fishing_stat_overlay.when_holding_rod", "When Holding Rod", "Display the stat overlay when holding a fishing rod.");
        this.addOption(builder, "main_lobby_fishing.fishing_stat_overlay.condition_operator", "Condition Operator",
            "The boolean operator to apply to the above conditions.",
            "AND: All above conditions must be met.",
            "OR: One or more of the above conditions must be met."
        );
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
        this.addOption(builder, "skyblock", "SkyBlock", "Enhancements to Hypixel SkyBlock.");
        this.addOption(builder, "skyblock.pretty_hud", "Pretty HUD", "Improve SkyBlock's various HUD elements with pretty client-sided elements.");
        builder.add(AUTOCONFIG + ".title", "MEH Options");
    }

    private void add(TranslationBuilder builder, KeyMapping keybind, String name) {
        builder.add(keybind.getName(), name);
    }

    private void add(TranslationBuilder builder, Translatable translatable, String name) {
        builder.add(translatable.getDescriptionKey(), name);
    }

    private void add(TranslationBuilder builder, me.shedaniel.clothconfig2.gui.entries.SelectionListEntry.Translatable translatable, String name) {
        builder.add(translatable.getKey(), name);
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
