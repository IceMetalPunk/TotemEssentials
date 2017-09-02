package com.icemetalpunk.totemessentials.sounds;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TESound extends SoundEvent {
	public TESound(String name) {
		super(new ResourceLocation(TotemEssentials.MODID, name));
		this.setRegistryName(new ResourceLocation(TotemEssentials.MODID, name));
	}
}
