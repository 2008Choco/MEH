package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;

public class MEHClientEntityEvents {

    public static final Event<EntityAdd> ENTITY_ADD = EventFactory.createArrayBacked(EntityAdd.class,
            listeners -> entity -> {
                for (EntityAdd event : listeners) {
                    event.onEntityAdd(entity);
                }
            }
    );

    public static final Event<EntityRemove> ENTITY_REMOVE = EventFactory.createArrayBacked(EntityRemove.class,
            listeners -> (entity, reason) -> {
                for (EntityRemove event : listeners) {
                    event.onEntityRemove(entity, reason);
                }
            }
    );

    private MEHClientEntityEvents() { }

    @FunctionalInterface
    public static interface EntityAdd {

        public void onEntityAdd(Entity entity);

    }

    @FunctionalInterface
    public static interface EntityRemove {

        public void onEntityRemove(Entity entity, Entity.RemovalReason reason);

    }

}
