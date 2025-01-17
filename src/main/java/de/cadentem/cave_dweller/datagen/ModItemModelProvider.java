package de.cadentem.cave_dweller.datagen;

import de.cadentem.cave_dweller.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(final PackOutput packOutput, final String modId, final ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ModItems.CAVE_DWELLER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}