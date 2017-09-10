package com.icemetalpunk.totemessentials.items;

import java.util.HashMap;

import com.icemetalpunk.totemessentials.IOreDicted;
import com.icemetalpunk.totemessentials.ModeledObject;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceExchange;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceFeatherfoot;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceFireglaze;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceLactic;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceReaper;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceReplication;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceStorage;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceTraveling;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceUndying;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceVampiric;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceVexatious;
import com.icemetalpunk.totemessentials.items.totems.ItemCuringTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemExchangeTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemFeatherfootTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemFireglazeTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemPhasingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemReapingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemReplicationTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemStorageTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemTotemShell;
import com.icemetalpunk.totemessentials.items.totems.ItemTravelingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemVampireTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledPhasingTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledReplicationTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledStorageTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledTravelingTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledVampireTotem;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ItemRegistry {
	public static HashMap<String, ModeledObject> registry = new HashMap<String, ModeledObject>();

	static {
		// Totems
		registry.put("totem_shell", new ItemTotemShell("totem_shell"));
		registry.put("phasing_totem", new ItemPhasingTotem("phasing_totem"));
		registry.put("reaping_totem", new ItemReapingTotem("reaping_totem"));
		registry.put("curing_totem", new ItemCuringTotem("curing_totem"));
		registry.put("featherfoot_totem", new ItemFeatherfootTotem("featherfoot_totem"));
		registry.put("vampire_totem", new ItemVampireTotem("vampire_totem"));
		registry.put("traveling_totem", new ItemTravelingTotem("traveling_totem"));
		registry.put("replication_totem", new ItemReplicationTotem("replication_totem"));
		registry.put("exchange_totem", new ItemExchangeTotem("exchange_totem"));
		registry.put("fireglaze_totem", new ItemFireglazeTotem("fireglaze_totem"));
		registry.put("storage_totem", new ItemStorageTotem("storage_totem"));

		// Ensouled Totems
		registry.put("ensouled_phasing_totem", new ItemEnsouledPhasingTotem("ensouled_phasing_totem"));
		registry.put("ensouled_traveling_totem", new ItemEnsouledTravelingTotem("ensouled_traveling_totem"));
		registry.put("ensouled_replication_totem", new ItemEnsouledReplicationTotem("ensouled_replication_totem"));
		registry.put("ensouled_vampire_totem", new ItemEnsouledVampireTotem("ensouled_vampire_totem"));
		registry.put("ensouled_storage_totem", new ItemEnsouledStorageTotem("ensouled_storage_totem"));

		// Essences
		registry.put("essence_reaper", new ItemEssenceReaper("essence_reaper"));
		registry.put("essence_vexatious", new ItemEssenceVexatious("essence_vexatious"));
		registry.put("essence_undying", new ItemEssenceUndying("essence_undying"));
		registry.put("essence_lactic", new ItemEssenceLactic("essence_lactic"));
		registry.put("essence_featherfoot", new ItemEssenceFeatherfoot("essence_featherfoot"));
		registry.put("essence_vampiric", new ItemEssenceVampiric("essence_vampiric"));
		registry.put("essence_traveling", new ItemEssenceTraveling("essence_traveling"));
		registry.put("essence_replication", new ItemEssenceReplication("essence_replication"));
		registry.put("essence_exchange", new ItemEssenceExchange("essence_exchange"));
		registry.put("essence_fireglaze", new ItemEssenceFireglaze("essence_fireglaze"));
		registry.put("essence_storage", new ItemEssenceStorage("essence_storage"));
	}

	public void put(String name, ModeledObject val) {
		registry.put(name, val);
	}

	public Item get(String name) {
		return (Item) registry.get(name);
	}

	public void registerAll(RegistryEvent.Register<Item> ev) {
		for (ModeledObject item : registry.values()) {
			ev.getRegistry().register((Item) item);
			if (item instanceof IOreDicted) {
				IOreDicted oreDict = (IOreDicted) item;
				oreDict.registerOreDict();
			}
		}
	}

	public void registerModels() {
		for (ModeledObject item : registry.values()) {
			item.registerModel();
		}
	}

}
