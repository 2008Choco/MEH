# Miscellaneous Enhancements for Hypixel
No seriously. Meh. This is a Fabric mod for Minecraft 1.21 that introduces some quality of life and convenience features when playing on the Hypixel server specifically. There's no true goal for this mod other than quality of life. This mod is maintained freely by [Choco](https://github.com/2008Choco) and includes features that just felt useful.

This project is licensed under the MIT license.

## Current Features

### Simple Chat Channels
An extremely primitive chat channel management system. Create and use channels to manage the various chat mediums on Hypixel, which by default includes Party chat. When direct messaged by a user on the network, a new channel will be automatically created.

**Controls:**
- **Switch to Next Channel:** Ctrl + Tab (with chat screen open)
- **Switch to Previous Channel:** Ctrl + Shift + Tab (with chat screen open)
- **Delete Current Channel:** Ctrl + Minus (with chat screen open)

### Manual GG
A feature for those that feel "auto gg" are disingenuous or too robotic, this feature still allows you to write "gg" in chat after a game by simply pressing the 'G' key twice.

Why? In order to send a "gg" normally, you need to open your chat (with 'T'), type "gg", then press Enter to send the message, which for right-handed people means taking your hand off of the mouse. This just seems like unnecessary gymnastics, so let's skip the 'T' and 'Enter' keys and type gg instead!

### Emote Selector
Hypixel's server has a server-sided text parser which will convert text such as "<3" or ":sloth:" into ASCII emotes and insert them into player chat messages. The only way to know what these emotes are is with the "/emotes" command, which is a bit out of the way and relies on the player's ability to remember these exact emote names or sequences.

The emote selector lists all of Hypixel's server emotes and allows the user to navigate and select the desired emote using arrow keys, then inserting it into their message. The UI is clean and allows the user to see exactly what emote they're using before they use it.

**Controls:**
- **Open Emote Selector:** Ctrl + E (with chat screen open)
- **Close Emote Selector:** Ctrl + E, or Escape
- **Navigate Selected Emote:** Left, Up, Right, Down Arrows
- **Insert Selected Emote:** Enter

## Dependencies & Integration
- Minecraft 1.21 (consequently, Java 21)
- Fabric Loader (version 0.15.x or above)
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) (version 0.100.x or above)
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
