package com.icemetalpunk.totemessentials.potions;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TEPotion extends Potion {
	public final ResourceLocation icon;

	public TEPotion(String name, boolean isBad, int color) {
		super(isBad, color);
		this.setRegistryName(new ResourceLocation(TotemEssentials.MODID, name));
		this.setPotionName("effect." + name);
		this.setIconIndex(0, 0);
		this.icon = new ResourceLocation(TotemEssentials.MODID, "textures/potions/" + name + ".png");
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
		mc.getTextureManager().bindTexture(this.icon);
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		if (mc.currentScreen != null) {
			mc.getTextureManager().bindTexture(this.icon);
			Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
		}
	}
}
