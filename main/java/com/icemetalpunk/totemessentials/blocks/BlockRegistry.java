package com.icemetalpunk.totemessentials.blocks;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class BlockRegistry {
	public static final HashMap<String, ITEBlock> registry = new HashMap<String, ITEBlock>();

	static {
	}

	public BlockRegistry() {
	}

	public void registerAll(RegistryEvent.Register<Block> ev) {
		for (ITEBlock block : registry.values()) {
			block.getItemBlock().setRegistryName(((Block) block).getRegistryName());
			ev.getRegistry().register((Block) block);
		}
	}

	public void registerItemBlocks(RegistryEvent.Register<Item> ev) {
		for (ITEBlock block : registry.values()) {
			ev.getRegistry().register(block.getItemBlock());
		}
	}

	public static Block get(String name) {
		return (Block) registry.get(name);
	}

	public void registerModels() {
		for (ITEBlock block : registry.values()) {
			block.registerModel();
		}
	}
}
