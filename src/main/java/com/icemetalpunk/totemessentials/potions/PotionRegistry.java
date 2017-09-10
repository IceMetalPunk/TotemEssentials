package com.icemetalpunk.totemessentials.potions;

import java.awt.Color;
import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;

public class PotionRegistry {
	private final HashMap<String, Potion> registry = new HashMap<String, Potion>();

	public PotionRegistry() {
		registry.put("potion_solar", new TEPotion("potion_solar", true, Color.YELLOW.getRGB()));
	}

	public void put(String name, Potion value) {
		this.registry.put(name, value);
	}

	@Nullable
	public Potion get(String name) {
		return this.registry.get(name);
	}

	public void registerAll(RegistryEvent.Register<Potion> ev) {
		for (Potion potion : registry.values()) {
			ev.getRegistry().register(potion);
		}
	}
}
