package com.icemetalpunk.totemessentials.events;

import java.util.HashMap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * This class is for handling events specific to Ensouled Totems, to avoid
 * cluttering everything into the TEEvents class
 */
public class TEEnsouledEvents {
	public static final HashMap<Item, ItemStack> ensouledReturns = new HashMap<Item, ItemStack>();

	public TEEnsouledEvents() {
	}

	/**
	 * The map is <Item, ItemStack>, which represents <Item_Crafted,
	 * ItemStack_Given_To_Player>
	 */
	@SubscribeEvent
	public void onCraft(PlayerEvent.ItemCraftedEvent ev) {
		ItemStack stack = ev.crafting;
		EntityPlayer player = ev.player;
		Item item = stack.getItem();
		if (ensouledReturns.containsKey(item)) {
			ItemStack give = ensouledReturns.get(item).copy();
			for (int i = 0; i < stack.getCount(); ++i) {
				if (!player.addItemStackToInventory(give)) {
					EntityItem entity = player.dropItem(give, false);
					if (entity != null) {
						entity.setNoPickupDelay();
					}
				}
			}
		}
	}
	
	// TODO: Ensould Curing Totem effects!
}
