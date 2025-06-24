package wtf.choco.meh.client.game.skyblock;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

public enum SkyBlockSkillType {

    COMBAT("Combat", 0xE30925),
    MINING("Mining", 0xF7B32B),
    FARMING("Farming", 0x3FA34D),
    FORAGING("Foraging", 0x007F5C),
    FISHING("Fishing", 0x1E6FBA),
    ENCHANTING("Enchanting", 0x7F3FBF),
    ALCHEMY("Alchemy", 0xD726A4),
    TAMING("Taming", 0xFFDC00),
    DUNGEONEERING("Dungeoneering", 0x6B48FF),
    CARPENTRY("Carpentry", 0x8B5E3C),
    SOCIAL("Social", 0xE73C7E),
    RUNECRAFTING("Runecrafting", 0x19C7C7),
    HUNTING("Hunting", 0xF5BD6E);

    private static final Map<String, SkyBlockSkillType> BY_NAME = new HashMap<>();

    static {
        for (SkyBlockSkillType skill : values()) {
            BY_NAME.put(skill.skillName.toLowerCase(), skill);
        }
    }

    private final String skillName; // Used by SkyBlock
    private final int experienceBarColor;

    private SkyBlockSkillType(String skillName, int experienceBarColor) {
        this.skillName = skillName;
        this.experienceBarColor = experienceBarColor;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getExperienceBarColor() {
        return experienceBarColor;
    }

    @Nullable
    public static SkyBlockSkillType getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
