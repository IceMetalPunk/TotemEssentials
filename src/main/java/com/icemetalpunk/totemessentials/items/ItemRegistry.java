package com.icemetalpunk.totemessentials.items;

import java.util.HashMap;

import com.icemetalpunk.totemessentials.IOreDicted;
import com.icemetalpunk.totemessentials.ModeledObject;
import com.icemetalpunk.totemessentials.config.TEConfig;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceAggression;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceAim;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceDaunting;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceExchange;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceFeatherfoot;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceFireglaze;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceFlamebody;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceGluttony;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceLactic;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceReaper;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceReplication;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceStorage;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceTraveling;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceUndying;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceVampiric;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceVexatious;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceWisdom;
import com.icemetalpunk.totemessentials.items.totems.ItemAggressionTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemAimingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemCuringTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemDauntingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemExchangeTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemFeatherfootTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemFireglazeTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemFlamebodyTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemGluttonyTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemPhasingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemReapingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemReplicationTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemStorageTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemTotemShell;
import com.icemetalpunk.totemessentials.items.totems.ItemTravelingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemVampireTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemWisdomTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledAggressionTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledAimingTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledCuringTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledDauntingTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledExchangeTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledFeatherfootTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledFireglazeTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledFlamebodyTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledGluttonyTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledPhasingTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledReapingTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledReplicationTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledStorageTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledTravelingTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledUndyingTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledVampireTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledWisdomTotem;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ItemRegistry {
	public static HashMap<String, ModeledObject> registry = new HashMap<String, ModeledObject>();

	static {

		registry.put("exchange_packet", new ItemExchangePacket("exchange_packet"));

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
		registry.put("aiming_totem", new ItemAimingTotem("aiming_totem"));
		registry.put("aggression_totem", new ItemAggressionTotem("aggression_totem"));
		registry.put("wisdom_totem", new ItemWisdomTotem("wisdom_totem"));
		registry.put("gluttony_totem", new ItemGluttonyTotem("gluttony_totem"));
		registry.put("flamebody_totem", new ItemFlamebodyTotem("flamebody_totem"));
		registry.put("daunting_totem", new ItemDauntingTotem("daunting_totem"));

		// Ensouled Totems
		registry.put("ensouled_phasing_totem", new ItemEnsouledPhasingTotem("ensouled_phasing_totem"));
		registry.put("ensouled_traveling_totem", new ItemEnsouledTravelingTotem("ensouled_traveling_totem"));
		registry.put("ensouled_replication_totem", new ItemEnsouledReplicationTotem("ensouled_replication_totem"));
		registry.put("ensouled_vampire_totem", new ItemEnsouledVampireTotem("ensouled_vampire_totem"));
		registry.put("ensouled_storage_totem", new ItemEnsouledStorageTotem("ensouled_storage_totem"));
		registry.put("ensouled_aiming_totem", new ItemEnsouledAimingTotem("ensouled_aiming_totem"));
		registry.put("ensouled_featherfoot_totem", new ItemEnsouledFeatherfootTotem("ensouled_featherfoot_totem"));
		registry.put("ensouled_fireglaze_totem", new ItemEnsouledFireglazeTotem("ensouled_fireglaze_totem"));
		registry.put("ensouled_curing_totem", new ItemEnsouledCuringTotem("ensouled_curing_totem"));
		registry.put("ensouled_aggression_totem", new ItemEnsouledAggressionTotem("ensouled_aggression_totem"));
		registry.put("ensouled_wisdom_totem", new ItemEnsouledWisdomTotem("ensouled_wisdom_totem"));
		registry.put("ensouled_reaping_totem", new ItemEnsouledReapingTotem("ensouled_reaping_totem"));
		registry.put("ensouled_gluttony_totem", new ItemEnsouledGluttonyTotem("ensouled_gluttony_totem"));
		registry.put("ensouled_undying_totem", new ItemEnsouledUndyingTotem("ensouled_undying_totem"));
		registry.put("ensouled_flamebody_totem", new ItemEnsouledFlamebodyTotem("ensouled_flamebody_totem"));
		registry.put("ensouled_daunting_totem", new ItemEnsouledDauntingTotem("ensouled_daunting_totem"));
		registry.put("ensouled_exchange_totem", new ItemEnsouledExchangeTotem("ensouled_exchange_totem"));

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
		registry.put("essence_aim", new ItemEssenceAim("essence_aim"));
		registry.put("essence_aggro", new ItemEssenceAggression("essence_aggro"));
		registry.put("essence_wisdom", new ItemEssenceWisdom("essence_wisdom"));
		registry.put("essence_gluttony", new ItemEssenceGluttony("essence_gluttony"));
		registry.put("essence_flamebody", new ItemEssenceFlamebody("essence_flamebody"));
		registry.put("essence_daunting", new ItemEssenceDaunting("essence_daunting"));
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

		// Load durabilities from configuration
		TEConfig.UpdateDurabilities();

	}

	public void registerModels() {
		for (ModeledObject item : registry.values()) {
			item.registerModel();
		}
	}

}
