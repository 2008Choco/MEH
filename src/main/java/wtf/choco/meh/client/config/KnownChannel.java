package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public final class KnownChannel {

    @ConfigEntry.Gui.RequiresRestart
    private String id;

    @ConfigEntry.Gui.RequiresRestart
    private String name;

    @ConfigEntry.ColorPicker
    @ConfigEntry.Gui.RequiresRestart
    private int color;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    private String command_prefix;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    private FocusFilter focus_filter;

    KnownChannel(String id, String name, int color, String commandPrefix, FilterType filterType, String filter) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.command_prefix = commandPrefix;
        this.focus_filter = new FocusFilter(filterType, filter);
    }

    KnownChannel() {
        this("unknown", "Unknown", 0, "", FilterType.STARTS_WITH, "");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public String getCommandPrefix() {
        return command_prefix;
    }

    public FocusFilter getFocusFilter() {
        return focus_filter;
    }

}
