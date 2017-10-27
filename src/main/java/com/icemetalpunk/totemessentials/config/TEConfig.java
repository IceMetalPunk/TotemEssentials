package com.icemetalpunk.totemessentials.config;

import java.util.HashMap;
import java.util.Map.Entry;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = TotemEssentials.MODID)
@Config.LangKey("totemessentials.config.title")
public class TEConfig {
	@Config.Name("Recipes")
	@Config.LangKey("totemessentials.config.recipes")
	public static HashMap<String, Boolean> recipeList = new HashMap<>();

	@Config.Name("Normal Durabilities")
	@Config.LangKey("totemessentials.config.durabilityOne")
	public static HashMap<String, Integer> durabilityOneList = new HashMap<>();

	@Config.Name("Ensouled Durabilities")
	@Config.LangKey("totemessentials.config.durabilityTwo")
	public static HashMap<String, Integer> durabilityEnsouledList = new HashMap<>();

	static {
		recipeList.put("Aggression Totem", true);
		recipeList.put("Aiming Totem", true);
		recipeList.put("Curing Totem", true);
		recipeList.put("Daunting Totem", true);
		recipeList.put("Exchange Totem", true);
		recipeList.put("Featherfoot Totem", true);
		recipeList.put("Fireglaze Totem", true);
		recipeList.put("Flamebody Totem", true);
		recipeList.put("Gluttony Totem", true);
		recipeList.put("Phasing Totem", true);
		recipeList.put("Reaping Totem", true);
		recipeList.put("Replication Totem", true);
		recipeList.put("Storage Totem", true);
		recipeList.put("Traveling Totem", true);
		recipeList.put("Vampire Totem", true);
		recipeList.put("Wisdom Totem", true);

		durabilityOneList.put("Aggression Totem", 50);
		durabilityOneList.put("Aiming Totem", 100);
		durabilityOneList.put("Curing Totem", 10);
		durabilityOneList.put("Daunting Totem", 35);
		durabilityOneList.put("Exchange Totem", 25);
		durabilityOneList.put("Featherfoot Totem", 100);
		durabilityOneList.put("Fireglaze Totem", 2);
		durabilityOneList.put("Flamebody Totem", 100);
		durabilityOneList.put("Gluttony Totem", 100);
		durabilityOneList.put("Phasing Totem", 600);
		durabilityOneList.put("Reaping Totem", 8);
		durabilityOneList.put("Replication Totem", 50);
		durabilityOneList.put("Traveling Totem", 250);
		durabilityOneList.put("Vampire Totem", 40);
		durabilityOneList.put("Wisdom Totem", 750);

		durabilityEnsouledList.put("Ensouled Aggression Totem", 150);
		durabilityEnsouledList.put("Ensouled Aiming Totem", 200);
		durabilityEnsouledList.put("Ensouled Curing Totem", 30);
		durabilityEnsouledList.put("Ensouled Daunting Totem", 75);
		durabilityEnsouledList.put("Ensouled Exchange Totem", 50);
		durabilityEnsouledList.put("Ensouled Featherfoot Totem", 200);
		durabilityEnsouledList.put("Ensouled Fireglaze Totem", 60000);
		durabilityEnsouledList.put("Ensouled Flamebody Totem", 200);
		durabilityEnsouledList.put("Ensouled Gluttony Totem", 500);
		durabilityEnsouledList.put("Ensouled Phasing Totem", 1200);
		durabilityEnsouledList.put("Ensouled Reaping Totem", 40);
		durabilityEnsouledList.put("Ensouled Replication Totem", 75);
		durabilityEnsouledList.put("Ensouled Traveling Totem", 500);
		durabilityEnsouledList.put("Ensouled Undying Totem", 10);
		durabilityEnsouledList.put("Ensouled Vampire Totem", 40);
		durabilityEnsouledList.put("Ensouled Wisdom Totem", 1500);
	}

	@Mod.EventBusSubscriber(modid = TotemEssentials.MODID)
	private static class ConfigEventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent ev) {
			if (ev.getModID().equals(TotemEssentials.MODID)) {
				ConfigManager.sync(TotemEssentials.MODID, Config.Type.INSTANCE);
				UpdateDurabilities();
			}
		}
	}

	public static void UpdateDurabilities() {
		String key = "";
		int val = 0;
		for (Entry<String, Integer> entry : durabilityOneList.entrySet()) {
			key = entry.getKey();
			val = entry.getValue();
			TotemEssentials.proxy.items.get(key.toLowerCase().replaceAll(" ", "_")).setMaxDamage(val);
		}
		for (Entry<String, Integer> entry : durabilityEnsouledList.entrySet()) {
			key = entry.getKey();
			val = entry.getValue();
			TotemEssentials.proxy.items.get(key.toLowerCase().replaceAll(" ", "_")).setMaxDamage(val);
		}
	}
}
