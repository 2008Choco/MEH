package wtf.choco.meh.client.model;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.fishing.FishingRodType;

public final class MEHModelLoadingPlugin implements ModelLoadingPlugin {

    @Override
    public void initialize(Context context) {
        List<ResourceLocation> modelLocations = new ArrayList<>();

        // Fishing Rod models (RetexturedFishingRodsFeature)
        for (FishingRodType type : FishingRodType.values()) {
            modelLocations.add(type.getModelLocation());
        }
    }

}
