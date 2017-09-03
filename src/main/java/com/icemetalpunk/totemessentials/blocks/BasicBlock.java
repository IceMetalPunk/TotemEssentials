package com.icemetalpunk.totemessentials.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;

public class BasicBlock extends Block implements IBasicBlock {

	protected ItemBlock itemBlock = new ItemBlock(this);

	public BasicBlock(String modID, String name, Material mat, MapColor col, CreativeTabs tab) {
		super(mat, col);
		this.setRegistryName(modID, name).setUnlocalizedName(name).setCreativeTab(tab);
	}

	public BasicBlock(String modID, String name, Material mat, CreativeTabs tab) {
		super(mat);
		this.setRegistryName(modID, name).setUnlocalizedName(name).setCreativeTab(tab);
	}

	@Override
	public void registerModel() {
		ModelResourceLocation model = new ModelResourceLocation(this.getRegistryName(), "inventory");
		ModelLoader.registerItemVariants(this.itemBlock, model);
		ModelLoader.setCustomModelResourceLocation(this.itemBlock, 0, model);
	}

	@Override
	public Item getItemBlock() {
		return this.itemBlock;
	}

}
