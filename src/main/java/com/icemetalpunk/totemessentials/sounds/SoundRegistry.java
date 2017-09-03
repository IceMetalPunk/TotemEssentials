package com.icemetalpunk.totemessentials.sounds;

import java.util.HashMap;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

public class SoundRegistry {
	public static final HashMap<String, TESound> registry = new HashMap<String, TESound>();

	static {
	}

	public SoundRegistry() {
	}

	public void registerAll(RegistryEvent.Register<SoundEvent> ev) {
		for (TESound sound : registry.values()) {
			ev.getRegistry().register((SoundEvent) sound);
		}
	}

	public static SoundEvent get(String name) {
		return (SoundEvent) registry.get(name);
	}
}