package com.icemetalpunk.totemessentials.proxies;

import com.icemetalpunk.totemessentials.render.AltarPedestalTESR;

import net.minecraft.tileentity.TileEntityDropper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TEClientProxy extends TECommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDropper.class, new AltarPedestalTESR());
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
}
