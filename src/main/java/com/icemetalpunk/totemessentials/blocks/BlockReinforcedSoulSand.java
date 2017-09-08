package com.icemetalpunk.totemessentials.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockReinforcedSoulSand extends TEBlock {

	public BlockReinforcedSoulSand(String name) {
		super(name, Material.SAND, MapColor.BROWN);
		this.setSoundType(SoundType.SAND);
	}

}
