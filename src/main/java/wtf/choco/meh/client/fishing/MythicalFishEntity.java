package wtf.choco.meh.client.fishing;

import java.lang.ref.Reference;

import net.minecraft.world.entity.decoration.ArmorStand;

public final record MythicalFishEntity(Reference<ArmorStand> armorStand, MythicalFishType type) {

}
