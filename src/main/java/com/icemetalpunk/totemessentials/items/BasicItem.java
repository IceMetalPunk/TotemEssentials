package com.icemetalpunk.totemessentials.items;

import com.icemetalpunk.totemessentials.ModeledObject;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BasicItem extends Item implements ModeledObject {

	public BasicItem(String modID, String name, CreativeTabs tab) {
		super();
		this.setRegistryName(modID, name).setUnlocalizedName(name).setCreativeTab(tab);
	}

	@Override
	public void registerModel() {
		ModelResourceLocation model = new ModelResourceLocation(this.getRegistryName(), "inventory");
		ModelLoader.registerItemVariants(this, model);
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}

}
