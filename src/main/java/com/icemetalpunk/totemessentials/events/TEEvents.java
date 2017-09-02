package com.icemetalpunk.totemessentials.events;

import java.util.HashMap;
import java.util.List;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TEEvents {
	private HashMap<Class<? extends Entity>, HashMap<Item, ItemStack>> dropReplacements = new HashMap<Class<? extends Entity>, HashMap<Item, ItemStack>>();
	private HashMap<Class<? extends Entity>, Item> essenceMap = new HashMap<Class<? extends Entity>, Item>();

	public TEEvents() {
		// Populate drop replacements map
		HashMap<Item, ItemStack> tempMap = new HashMap<Item, ItemStack>();
		tempMap.put(Items.TOTEM_OF_UNDYING, new ItemStack((Item) TotemEssentials.proxy.items.get("totem_shell"), 1));
		dropReplacements.put(EntityEvoker.class, tempMap);

		// Populate essence drop map
		essenceMap.put(EntityVex.class, TotemEssentials.proxy.items.get("essence_vexatious"));
		essenceMap.put(EntityZombie.class, TotemEssentials.proxy.items.get("essence_undying"));
	}

	// Phasing if holding the Phasing Totem
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent ev) {
		if (ev.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ev.getEntityLiving();
			if (player.getHeldItemMainhand().getItem() == TotemEssentials.proxy.items.get("phasing_totem")) {
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
			if (player.getHeldItemMainhand().getItem() != TotemEssentials.proxy.items.get("phasing_totem")) {
				player.noClip = false;
				player.setNoGravity(false);
			}
		}
	}

	// Reaping if the Totem of Reaping is in your inventory when killing a
	// compatible mob
	@SubscribeEvent
	public void onReap(LivingDropsEvent ev) {
		EntityLivingBase mob = ev.getEntityLiving();
		DamageSource damage = ev.getSource();
		Entity killer = damage.getTrueSource();

		if (killer instanceof EntityPlayer && essenceMap.containsKey(mob.getClass())) {
			EntityPlayer player = (EntityPlayer) killer;
			InventoryPlayer inv = player.inventory;
			ItemStack stack = ItemStack.EMPTY;
			ItemStack reaper = new ItemStack(TotemEssentials.proxy.items.get("reaping_totem"), 1);
			int slotID = inv.getSlotFor(reaper); // Doesn't include offhand!
			if (slotID < 0) {
				ItemStack inOffhand = player.getHeldItemOffhand();
				if (inOffhand.getItem() == reaper.getItem()
						&& (!inOffhand.getHasSubtypes() || inOffhand.getMetadata() == reaper.getMetadata())
						&& ItemStack.areItemStackTagsEqual(inOffhand, reaper)) {
					stack = inOffhand;
				}
			} else {
				stack = inv.getStackInSlot(slotID);
			}
			if (stack != ItemStack.EMPTY) {
				stack.damageItem(1, player);
				int looting = ev.getLootingLevel();
				ItemStack essenceType = new ItemStack(essenceMap.get(mob.getClass()), 1 + looting);
				EntityItem essenceDrop = new EntityItem(mob.getEntityWorld(), mob.getPosition().getX(),
						mob.getPosition().getY(), mob.getPosition().getZ(), essenceType);
				ev.getDrops().add(essenceDrop);
			}
		}
	}

	// Basic mob drop replacement (for non-loot-table things)
	@SubscribeEvent
	public void onDropReplacement(LivingDropsEvent ev) {
		List<EntityItem> dropList = ev.getDrops();
		HashMap<Item, ItemStack> replacements = dropReplacements.get(ev.getEntity().getClass());
		if (replacements != null) {
			for (EntityItem item : dropList) {
				if (replacements.containsKey(item.getItem().getItem())) {
					item.setItem(replacements.get(item.getItem().getItem()).copy());
				}
			}
		}
	}
}
