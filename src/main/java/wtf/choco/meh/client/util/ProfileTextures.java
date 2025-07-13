package wtf.choco.meh.client.util;

public final class ProfileTextures {

    private static final String MC_TEXTURE_URL = "http://textures.minecraft.net/texture/%s";

    public static final String TEXTURE_URL_HELIOS = minecraftTexture("c0102be6756274719b7f625830ea7ef5051c7d95dc01fe8359b4186378a0c263");
    public static final String TEXTURE_URL_SELENE = minecraftTexture("64a1fd9df8ad1d0e216ac347a39b47e797f4a3de7de4df073b065cb69f705baf");
    public static final String TEXTURE_URL_NYX = minecraftTexture("d56123b334c5c18a4df9c1d6aff25046f5e06a7ea8f60b80b91ae48ac7f9830d");
    public static final String TEXTURE_URL_APHRODITE = minecraftTexture("fc084765c62c03f3479e759208ca1e7fa99f674d0c8be78a3f10f5b1e866ca24");
    public static final String TEXTURE_URL_ZEUS = minecraftTexture("bb42db182471da05bc2e3d04ea08b7069004f5c066c0aacca1f18c40ee3049cf");
    public static final String TEXTURE_URL_DEMETER = minecraftTexture(""); // TODO: What's the texture? We don't know yet! :(
    public static final String TEXTURE_URL_DAEDALUS = minecraftTexture("a92dca1e8218b18b0759fc5baedc7e054067b1ec2f97b10c8c3fca8f923a0a6a");
    public static final String TEXTURE_URL_HADES = minecraftTexture("a46fa2c5492722bd510cf546cde1b6b6c689e7640a99606ed49930fe54def0d");

    private ProfileTextures() { }

    private static String minecraftTexture(String texturePath) {
        return String.format(MC_TEXTURE_URL, texturePath);
    }

}
