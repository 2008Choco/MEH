package wtf.choco.meh.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;

import wtf.choco.meh.client.fishing.FishingRodType;

public final class MEHItemModelProvider extends FabricModelProvider {

    public MEHItemModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) { }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        for (FishingRodType type : FishingRodType.values()) {
            this.generateItemModel(generator, type);
        }
    }

    private void generateItemModel(ItemModelGenerators generator, FishingRodType type) {
        // Can't generate item model files without a registered Item :( Those still have to be generated automatically
        // But we can generate the individual model files, which is at least something!
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(type.getModelLocation(), TextureMapping.layer0(type.getTextureLocation()), generator.modelOutput);
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(type.getCastModelLocation(), TextureMapping.layer0(type.getCastTextureLocation()), generator.modelOutput);
    }

}
