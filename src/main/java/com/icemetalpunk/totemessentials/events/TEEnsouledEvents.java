package com.icemetalpunk.totemessentials.events;

import java.util.HashMap;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

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

	// TODO: Ensouled Phasing Totem effects!
	// Phasing if holding the Phasing Totem
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent ev) {
		if (ev.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ev.getEntityLiving();
			Item mainHand = player.getHeldItemMainhand().getItem();
			Item offHand = player.getHeldItemOffhand().getItem();
			Item totem = TotemEssentials.proxy.items.get("ensouled_phasing_totem");
			if (mainHand == totem || offHand == totem) {
				player.capabilities.allowFlying = true;
				player.capabilities.isFlying = true;
				player.setNoGravity(true);
				player.onGround = false;
				player.noClip = false;
				if (player.isEntityInsideOpaqueBlock()) {
					player.getHeldItemMainhand().damageItem(1, player);
				}
				player.noClip = true;
			}
		}
	}

	// Reset phasing status at end of tick
	@SubscribeEvent
	public void onPlayerPostTick(PlayerTickEvent ev) {
		if (ev.phase == TickEvent.Phase.END) {
			EntityPlayer player = ev.player;
			Item mainHand = player.getHeldItemMainhand().getItem();
			Item offHand = player.getHeldItemOffhand().getItem();
			Item totem = TotemEssentials.proxy.items.get("ensouled_phasing_totem");
			Item normalTotem = TotemEssentials.proxy.items.get("phasing_totem");

			// If you don't check for the non-ensouled totem of phasing, they
			// interfere!
			// And then this stops the noclip from the normal totem! :(
			// So check for both here!
			boolean noEnsouled = (mainHand != totem && offHand != totem);
			boolean noNormal = (mainHand != normalTotem && offHand != normalTotem);
			if (noEnsouled) {
				if (!player.isCreative()) {
					player.capabilities.allowFlying = false;
					player.capabilities.isFlying = false;
				}
				if (noNormal) {
					player.setNoGravity(false);
					if (!player.isSpectator()) {
						player.noClip = false;
					}
				}
			}
		}
	}
}
