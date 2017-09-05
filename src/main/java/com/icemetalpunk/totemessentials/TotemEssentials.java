package com.icemetalpunk.totemessentials;

import com.icemetalpunk.totemessentials.events.TEEvents;
import com.icemetalpunk.totemessentials.events.TERegistryEvents;
import com.icemetalpunk.totemessentials.proxies.TECommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TotemEssentials.MODID, version = TotemEssentials.VERSION)
public class TotemEssentials {
	public static final String MODID = "totemessentials";
	public static final String VERSION = "0.1";

	@Instance
	public static TotemEssentials instance = new TotemEssentials();

	@SidedProxy(clientSide = "com.icemetalpunk.totemessentials.proxies.TEClientProxy", serverSide = "com.icemetalpunk.totemessentials.proxies.TEServerProxy")
	public static TECommonProxy proxy;

	// Tab
	public static final CreativeTabs tab = new CreativeTabs("totemessentials") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(proxy.items.get("totem_shell"));
		}

	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		MinecraftForge.EVENT_BUS.register(new TERegistryEvents());
		MinecraftForge.EVENT_BUS.register(new TEEvents());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
