package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;

public final class GuiEvents {

    public static final Event<AddHudDrawLayer> ADD_HUD_DRAW_LAYER = EventFactory.createArrayBacked(AddHudDrawLayer.class,
            listeners -> (minecraft, layers) -> {
                for (AddHudDrawLayer listener : listeners) {
                    listener.onAddHudDrawLayer(minecraft, layers);
                }
            }
    );

    private GuiEvents() { }

    public static interface AddHudDrawLayer {

        public void onAddHudDrawLayer(Minecraft minecraft, LayeredDraw... layers);

    }

}
