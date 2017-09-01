package com.icemetalpunk.totemessentials.items;

import java.util.HashMap;

import com.icemetalpunk.totemessentials.items.essences.ItemEssenceReaper;
import com.icemetalpunk.totemessentials.items.totems.ItemPhasingTotem;
import com.icemetalpunk.totemessentials.items.totems.ItemTotemShell;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ItemRegistry {
	public static HashMap<String, ITEItem> registry = new HashMap<String, ITEItem>();

	static {
		// Totems
		registry.put("phasing_totem", new ItemPhasingTotem("phasing_totem"));
		registry.put("totem_shell", new ItemTotemShell("totem_shell"));

		// Essences
		registry.put("essence_reaper", new ItemEssenceReaper("essence_reaper"));
	}

	public void put(String name, ITEItem val) {
		registry.put(name, val);
	}

	public ITEItem get(String name) {
		return registry.get(name);
	}

	public void registerAll(RegistryEvent.Register<Item> ev) {
		for (ITEItem item : registry.values()) {
			ev.getRegistry().register((Item) item);
		}
	}

	public void registerModels() {
		for (ITEItem item : registry.values()) {
			item.registerModel();
		}
	}

}
