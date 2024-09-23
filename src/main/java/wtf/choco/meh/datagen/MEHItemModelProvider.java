package wtf.choco.meh.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;

import wtf.choco.meh.client.fishing.FishingRodType;

final class MEHItemModelProvider extends FabricModelProvider {

    public MEHItemModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        for (FishingRodType type : FishingRodType.values()) {
            this.generate(generator, type);
        }
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {}

    private void generate(ItemModelGenerators generator, FishingRodType type) {
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(type.getModelLocation(), TextureMapping.layer0(type.getTextureLocation()), generator.output);
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(type.getCastModelLocation(), TextureMapping.layer0(type.getCastTextureLocation()), generator.output);
    }

}
