package com.icemetalpunk.totemessentials.events;

import java.util.HashMap;

import com.icemetalpunk.totemessentials.TotemEssentials;
import com.icemetalpunk.totemessentials.items.EntityItemFireproof;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class TERegistryEvents {

	/* Loot table additions: for loot that's not simply a replacement */
	public HashMap<String, ResourceLocation> lootAdditions = new HashMap<String, ResourceLocation>();
	protected int entityID = 0;

	public TERegistryEvents() {
		lootAdditions.put("minecraft:entities/vindication_illager",
				new ResourceLocation(TotemEssentials.MODID, "injected_loot/vindicator"));

		EntityRegistry.registerModEntity(new ResourceLocation(TotemEssentials.MODID, "fireproof_item"),
				EntityItemFireproof.class, "fireproof_item", entityID++, TotemEssentials.instance, 5, 1, true);
	}

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

	/* Loot table additions: for loot that's not simply a replacement */
	@SubscribeEvent
	public void addToLootTables(LootTableLoadEvent ev) {
		if (lootAdditions.containsKey(ev.getName().toString())) {
			LootEntry entry = new LootEntryTable(lootAdditions.get(ev.getName().toString()), 100, 1, null,
					"injected_loot");
			LootPool pool = new LootPool(new LootEntry[] { entry }, new LootCondition[0],
					new RandomValueRange(1.0f, 1.0f), new RandomValueRange(0.0f, 0.0f), "injected_loot");
			ev.getTable().addPool(pool);
		}
	}
}
