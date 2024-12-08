package wtf.choco.meh.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;

public final class MEHDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        Pack pack = generator.createPack();
        pack.addProvider(MEHItemModelProvider::new);
        pack.addProvider(MEHLanguageProvider::new);
    }

}
