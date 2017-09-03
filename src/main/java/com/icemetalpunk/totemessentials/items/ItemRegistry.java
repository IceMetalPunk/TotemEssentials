package com.icemetalpunk.totemessentials.items;

import java.util.HashMap;

import com.icemetalpunk.totemessentials.ModeledObject;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceReaper;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceUndying;
import com.icemetalpunk.totemessentials.items.essences.ItemEssenceVexatious;
import com.icemetalpunk.totemessentials.items.totems.ItemPhasingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemReapingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemTotemShell;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ItemRegistry {
	public static HashMap<String, ModeledObject> registry = new HashMap<String, ModeledObject>();

	static {
		// Totems
		registry.put("phasing_totem", new ItemPhasingTotem("phasing_totem"));
		registry.put("reaping_totem", new ItemReapingTotem("reaping_totem"));
		registry.put("totem_shell", new ItemTotemShell("totem_shell"));

		// Essences
		registry.put("essence_reaper", new ItemEssenceReaper("essence_reaper"));
		registry.put("essence_vexatious", new ItemEssenceVexatious("essence_vexatious"));
		registry.put("essence_undying", new ItemEssenceUndying("essence_undying"));
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
		}
	}

	public void registerModels() {
		for (ModeledObject item : registry.values()) {
			item.registerModel();
		}
	}

}
