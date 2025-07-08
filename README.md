# Miscellaneous Enhancements for Hypixel (MEH)
No seriously. Meh. This is a Fabric mod that introduces some quality of life and convenience features when playing on the Hypixel server specifically. There's no true goal for this mod other than quality of life. This mod is maintained freely by [Choco](https://github.com/2008Choco) and includes features that just felt useful.

This project is licensed under the MIT license.

> [!NOTE]
> Some features rely on Hypixel's chat messages due to Hypixel not sending packets to clients for specific events occurring. As a consequence, some of MEH's features only support Hypixel's English language. There are no plans to expand this support to other languages.

> [!IMPORTANT]
> This mod uses textures from the community-created [Fishers' Delight Resource Pack](https://hypixel.net/threads/5411511/) with permission from Apfelbastian! I do not claim copyright for ANY textures under the following directories:
> - `src/main/resources/assets/meh/textures/item/fishing_rod/`
>
> Please download and use their resource pack if you enjoy main lobby fishing on Hypixel but aren't willing to use this mod :)

## Dependencies & Integration
- Minecraft 1.21.6 (consequently, Java 21)
- Fabric Loader (version 0.16.14 or above)
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) (version 0.127.x or above)
- [Cloth Config API](https://modrinth.com/mod/cloth-config/)
- _(Optional)_ [Mod Menu](https://modrinth.com/mod/modmenu/)
  - Provides a configuration screen in the Mod Menu list
- _(Optional)_ [Hypixel Mod API](https://modrinth.com/mod/hypixel-mod-api)
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
