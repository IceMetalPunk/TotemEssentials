package com.icemetalpunk.totemessentials.events;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TERegistryEvents {
	
	@SubscribeEvent
	public void modelHandler(ModelRegistryEvent ev) {
		TotemEssentials.proxy.blocks.registerModels();
		TotemEssentials.proxy.items.registerModels();
	}

	@SubscribeEvent
	public void itemHandler(RegistryEvent.Register<Item> ev) {
		TotemEssentials.proxy.items.registerAll(ev);
		TotemEssentials.proxy.blocks.registerItemBlocks(ev);
	}

	@SubscribeEvent
	public void blockHandler(RegistryEvent.Register<Block> ev) {
		TotemEssentials.proxy.blocks.registerAll(ev);
	}

	@SubscribeEvent
	public void soundHandler(RegistryEvent.Register<SoundEvent> ev) {
		TotemEssentials.proxy.sounds.registerAll(ev);
	}
}
