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
