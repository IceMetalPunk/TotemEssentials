package com.icemetalpunk.totemessentials.proxies;

import com.icemetalpunk.totemessentials.TotemEssentials;
import com.icemetalpunk.totemessentials.blocks.BlockRegistry;
import com.icemetalpunk.totemessentials.items.ItemRegistry;
import com.icemetalpunk.totemessentials.sounds.SoundRegistry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TECommonProxy {

	public BlockRegistry blocks;
	public ItemRegistry items;
	public SoundRegistry sounds;

	public void preInit(FMLPreInitializationEvent e) {
		blocks = new BlockRegistry();
		items = new ItemRegistry();
		sounds = new SoundRegistry();
		loadLootTables();
	}

	public void init(FMLInitializationEvent e) {

	}

	public void postInit(FMLPostInitializationEvent e) {
	}

	protected void loadLootTables() {
		LootTableList.register(new ResourceLocation(TotemEssentials.MODID, "injected_loot/vindicator"));
	}

}
