# Miscellaneous Enhancements for Hypixel (MEH)
No seriously. Meh. This is a Fabric mod for Minecraft 1.21.1 that introduces some quality of life and convenience features when playing on the Hypixel server specifically. There's no true goal for this mod other than quality of life. This mod is maintained freely by [Choco](https://github.com/2008Choco) and includes features that just felt useful.

This project is licensed under the MIT license.

## Current Features

### Simple Chat Channels
An extremely primitive chat channel management system. Create and use channels to manage the various chat mediums on Hypixel, which by default includes Party chat. When direct messaged by a user on the network, a new channel will be automatically created.

**Controls:**
- **Switch to Next Channel:** Ctrl + Tab (with chat screen open)
- **Switch to Previous Channel:** Ctrl + Shift + Tab (with chat screen open)
- **Delete Current Channel:** Ctrl + Minus (with chat screen open)

### Mnemonics
Mnemonics are quick key sequences that perform some action. MEH provides some very simple mnemonics for commonly used messages while on Hypixel which, unlike auto-ggs or auto-gcs, makes sending these messages still feel genuine because they have to be manually performed. Chat mnemonics specifically alleviates the unnecessary movement of the left hand to press 'T' (to open chat) and the right hand from the mouse to press the 'Enter' key to send the message.

The following mnemonics are supported by MEH:
- **Good Game:** Writes "gg" in chat (keys: 'g', 'g', 10 second cooldown)
- **Good Catch:** Writes "gc" in chat (keys: 'g', 'c', 10 second cooldown)
  * Used by fishers to show good sportsmanship after a rare catch!

### Emote Selector
Hypixel's server has a server-sided text parser which will convert text such as "<3" or ":sloth:" into ASCII emotes and insert them into player chat messages. The only way to know what these emotes are is with the "/emotes" command, which is a bit out of the way and relies on the player's ability to remember these exact emote names or sequences.

The emote selector lists all of Hypixel's server emotes and allows the user to navigate and select the desired emote using arrow keys, then inserting it into their message. The UI is clean and allows the user to see exactly what emote they're using before they use it.

**Controls:**
- **Open Emote Selector:** Ctrl + E (with chat screen open)
- **Close Emote Selector:** Ctrl + E, or Escape
- **Navigate Selected Emote:** Left, Up, Right, Down Arrows
- **Insert Selected Emote:** Enter

### Main Lobby Fishing
An assortment of enhancements to the main lobby fishing experience are made by this mod, including custom textures for the variety of fishing rods.

![image](https://github.com/user-attachments/assets/3a919709-072d-4d96-9774-77bed5df51bd)

> [!IMPORTANT]
> This mod uses textures from the community-created [Fishers' Delight Resource Pack](https://hypixel.net/threads/5411511/) with permission from Apfelbastian! I do not claim copyright for ANY textures under the following directories:
> - `src/main/resources/assets/meh/textures/item/fishing_rod/`
>
> Please download and use their resource pack if you enjoy main lobby fishing on Hypixel but aren't willing to use this mod :)

## Dependencies & Integration
- Minecraft 1.21.1 (consequently, Java 21)
- Fabric Loader (version 0.16 or above)
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) (version 0.104.x or above)
- [Cloth Config API](https://modrinth.com/mod/cloth-config/) (v15 and above)
- _(Optional)_ [Mod Menu](https://modrinth.com/mod/modmenu/) (v11 and above)
  - Provides a configuration screen in the Mod Menu list

## Building/Distribution
This project has no precompiled binaries and likely will not ever have any precompiled binaries or releases. If you'd like to use this mod, you will have to compile it yourself. This project makes use of Gradle and can be easily compiled with the following command run in the project's root directory:
```
./gradlew build
```
A compiled .jar file can be found under the `build/libs/` directory and drag-and-dropped into the `<minecraft_install_dir>/mods/` directory.

## Contributing
Contributions are welcome! Before contributing a new feature, however, please consult me first! You can either open an issue or contact me on Discord `@choco_dev`.

Additionally, please note that this project makes use of data generators. If you're missing translations or other information, try running `./gradlew runDatagen` first. The `build` task will run the data generator automatically, so if you've built the project already, you shouldn't need to run it again unless you've edited the data generation.
