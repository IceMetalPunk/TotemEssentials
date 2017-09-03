package com.icemetalpunk.totemessentials.blocks;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class TEBlock extends BasicBlock {
	public TEBlock(String name, Material mat, MapColor col) {
		super(TotemEssentials.MODID, name, mat, col, TotemEssentials.tab);
	}

	public TEBlock(String name, Material mat) {
		super(TotemEssentials.MODID, name, mat, TotemEssentials.tab);
	}

	// Rock by default, because why not?
	public TEBlock(String name) {
		super(TotemEssentials.MODID, name, Material.ROCK, TotemEssentials.tab);
	}
}
