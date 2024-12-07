package wtf.choco.meh.client.model.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.server.HypixelServerState;
import wtf.choco.meh.client.server.HypixelServerType;

/**
 * @param serverType
 *
 * @deprecated unused, but might be useful later!
 */
@Deprecated
public record ItemModelPropertyHypixelServerType(HypixelServerType serverType) implements ConditionalItemModelProperty {

    public static final MapCodec<ItemModelPropertyHypixelServerType> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(HypixelServerType.CODEC.fieldOf("server_type").forGetter(ItemModelPropertyHypixelServerType::serverType))
            .apply(instance, ItemModelPropertyHypixelServerType::new)
    );

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int i, ItemDisplayContext context) {
        HypixelServerState serverState = MEHClient.getInstance().getHypixelServerState();
        if (!serverState.isConnectedToHypixel()) {
            return false;
        }

        Optional<HypixelServerType> serverType = serverState.getServerLocationProvider().getServerType();
        return serverType.isPresent() && serverType.get() == serverType();
    }

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

}
