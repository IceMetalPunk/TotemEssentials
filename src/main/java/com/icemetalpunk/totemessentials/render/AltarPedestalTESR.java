package com.icemetalpunk.totemessentials.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class AltarPedestalTESR extends TileEntitySpecialRenderer<TileEntityDropper> {

	RenderEntityItem rei = null;
	EntityItem entity = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0,
			new ItemStack(Items.TOTEM_OF_UNDYING, 1));

	public AltarPedestalTESR() {
		super();
		rei = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(),
				Minecraft.getMinecraft().getRenderItem()) {
			@Override
			public boolean shouldSpreadItems() {
				return false;
			}
		};
	}

	@Override
	public void render(TileEntityDropper te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		entity.setWorld(te.getWorld());

		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.disableLighting();
		rei.doRender(entity, x + 0.5, y + 1.0f, z + 0.5, 0, partialTicks);
		GlStateManager.enableLighting();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();

		super.render(te, x, y, z, partialTicks, destroyStage, alpha);

	}

}
