package com.icemetalpunk.totemessentials.items.essences;

import com.icemetalpunk.totemessentials.TotemEssentials;
import com.icemetalpunk.totemessentials.items.ITEItem;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemEssenceBase extends Item implements ITEItem {

	public ItemEssenceBase(String name) {
		super();
		this.setRegistryName(TotemEssentials.MODID, name).setUnlocalizedName(name).setCreativeTab(TotemEssentials.tab);
	}

	@Override
	public void registerModel() {
		ModelResourceLocation model = new ModelResourceLocation(this.getRegistryName(), "inventory");
		ModelLoader.registerItemVariants(this, model);
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
}
