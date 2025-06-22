# Miscellaneous Enhancements for Hypixel (MEH)
No seriously. Meh. This is a Fabric mod for Minecraft 1.21.4 that introduces some quality of life and convenience features when playing on the Hypixel server specifically. There's no true goal for this mod other than quality of life. This mod is maintained freely by [Choco](https://github.com/2008Choco) and includes features that just felt useful.

This project is licensed under the MIT license.

> [!NOTE]
> Some features rely on Hypixel's chat messages due to Hypixel not sending packets to clients for specific events occurring. As a consequence, some of MEH's features only support Hypixel's English language. There are no plans to expand this support to other languages.

## Current Features

### Simple Chat Channels
An extremely primitive chat channel management system. Create and use channels to manage the various chat mediums on Hypixel, which by default includes Party chat. When direct messaged by a user on the network, a new channel will be automatically created.

**Controls:**
- **Switch to Next Channel:** Ctrl + Tab (with chat screen open)
- **Switch to Previous Channel:** Ctrl + Shift + Tab (with chat screen open)
- **Delete Current Channel:** Ctrl + Minus (with chat screen open)
- **Toggle Focus Mode:** Ctrl + F (with chat screen open)

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

### Auto-Disable Flight (Housing)
Those with permission, when joining a Housing, will have their flight automatically enabled. This can be problematic for houses with parkours in which flight will cancel a run. Often you have to run `/fly` when joining _every single house_ to ensure you don't accidentally fly while doing a parkour. This feature will automatically run the `/fly` command when joining a Housing.

This feature is disabled by default because its usefulness is limited.

### Auto Night Vision (Housing)
Many houses in Housing will provide commands to enable night vision, but they often vary in name. It can be a bit frustrating joining a server and trying a bunch of commands to enable night vision. This feature will search commands that the client is aware of and automatically execute ones that are generally associated with enabling night vision.

This feature is disabled by default because its usefulness is limited.

### Party List
When joining a party, it can often feel overwhelming to know who your party mates are without frequently checking Hypixel's party list command (`/pl`). MEH will poll the server for party information and outline the list of players in your party in one of the top corners of the screen so you always know who's in your party and what role they have. Everything is handled automatically. It will automatically recognize you've joined a party, and automatically recognize when you've left (or if it has disbanded).

> [!NOTE]
> This feature requires Hypixel Mod API to be installed. See Dependencies & Integration for more information!

## Dependencies & Integration
- Minecraft 1.21.4 (consequently, Java 21)
- Fabric Loader (version 0.16 or above)
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) (version 0.110.x or above)
- [Cloth Config API](https://modrinth.com/mod/cloth-config/) (v17 and above)
- _(Optional)_ [Mod Menu](https://modrinth.com/mod/modmenu/) (v13 and above)
  - Provides a configuration screen in the Mod Menu list
- _(Optional)_ [Hypixel Mod API](https://modrinth.com/mod/hypixel-mod-api) (v1.0.1 and above)
  - Provides more accurate information about the client's current state
  - If not installed, MEH will do its best, but some features may not work as described

## Building/Distribution
This project only has precompiled binaries through GitHub Actions. If you’d like to use this mod, you can download the precompiled binaries from the actions section. This project likely will not ever have any releases. However, if you prefer to compile it yourself, this project makes use of Gradle and can be easily compiled with the following command run in the project’s root directory:
```
./gradlew build
```
A compiled .jar file can be found under the `build/libs/` directory and drag-and-dropped into the `<minecraft_install_dir>/mods/` directory if you would like to ever use the version you compiled yourself.

## Contributing
Contributions are welcome! Before contributing a new feature, however, please consult me first! You can either open an issue or contact me on Discord `@choco_dev`.
