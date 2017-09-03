package com.icemetalpunk.totemessentials.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
		essenceMap.put(EntityCow.class, TotemEssentials.proxy.items.get("essence_lactic"));
		essenceMap.put(EntityChicken.class, TotemEssentials.proxy.items.get("essence_featherfoot"));
		essenceMap.put(EntityBat.class, TotemEssentials.proxy.items.get("essence_vampiric"));
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

	// Helper method to get a matching stack from a player's inventory,
	// regardless of which part of the inventory it's in.
	public ItemStack getStackInPlayerInv(EntityPlayer player, ItemStack compareTo) {
		InventoryPlayer inv = player.inventory;
		ItemStack stack = ItemStack.EMPTY;
		int slotID = inv.getSlotFor(compareTo); // Doesn't include offhand!
		if (slotID < 0) {
			ItemStack inOffhand = player.getHeldItemOffhand();
			if (inOffhand.getItem() == compareTo.getItem()
					&& (!inOffhand.getHasSubtypes() || inOffhand.getMetadata() == compareTo.getMetadata())
					&& ItemStack.areItemStackTagsEqual(inOffhand, compareTo)) {
				stack = inOffhand;
			} else {
				for (ItemStack armorStack : player.getArmorInventoryList()) {
					if (armorStack.getItem() == compareTo.getItem()
							&& (!armorStack.getHasSubtypes() || armorStack.getMetadata() == compareTo.getMetadata())
							&& ItemStack.areItemStackTagsEqual(armorStack, compareTo)) {
						stack = armorStack;
						break;
					}
				}
			}
		} else {
			stack = inv.getStackInSlot(slotID);
		}
		return stack;
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
			ItemStack reaper = new ItemStack(TotemEssentials.proxy.items.get("reaping_totem"), 1);
			ItemStack stack = getStackInPlayerInv(player, reaper);
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

	// Totem of Curing handler
	@SubscribeEvent
	public void cureNegativeEffects(LivingEvent.LivingUpdateEvent ev) {
		EntityLivingBase ent = ev.getEntityLiving();
		// Copy the effects to a new list to avoid Concurrent Modification
		// exceptions!
		List<PotionEffect> effects = new ArrayList<PotionEffect>(ent.getActivePotionEffects());
		if (ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ent;

			// If they have a Totem of Curing in their inventory...
			ItemStack curingTotem = new ItemStack(TotemEssentials.proxy.items.get("curing_totem"), 1);
			ItemStack match = getStackInPlayerInv(player, curingTotem);
			if (match != ItemStack.EMPTY) {
				for (PotionEffect effect : effects) {

					// Clear bad effects and damage the totem, stopping if it
					// breaks.
					Potion actualPotion = effect.getPotion();
					if (actualPotion.isBadEffect()) {
						int level = effect.getAmplifier();
						match.damageItem(level + 1, player);
						player.removeActivePotionEffect(actualPotion);
						if (match.isEmpty()) {
							break;
						}
					}
				}
			}
		}
	}

	// Totem of Featherfoot handler
	@SubscribeEvent
	public void onFallDamage(LivingHurtEvent ev) {
		DamageSource source = ev.getSource();
		EntityLivingBase ent = ev.getEntityLiving();
		int intAmount = (int) Math.ceil(ev.getAmount());
		if (source.damageType == "fall" && ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ent;
			ItemStack featherfootTotem = new ItemStack(TotemEssentials.proxy.items.get("featherfoot_totem"));
			ItemStack match = getStackInPlayerInv(player, featherfootTotem);
			if (match != ItemStack.EMPTY) {
				int totalDurability = match.getMaxDamage();
				int currentDamage = match.getItemDamage();
				if (currentDamage + intAmount >= totalDurability + 1) {
					match.damageItem(totalDurability - currentDamage + 1, player);
					intAmount -= totalDurability - currentDamage;
					ev.setAmount(intAmount);
				} else {
					match.damageItem(intAmount, player);
					ev.setCanceled(true);
				}
			}
		}
	}

	// Totem of Vampirism handler
	@SubscribeEvent
	public void onAttack(LivingHurtEvent ev) {
		DamageSource source = ev.getSource();
		Entity hitter = source.getTrueSource();
		EntityLivingBase victim = ev.getEntityLiving();

		float damagedAmount = ev.getAmount();
		damagedAmount = Math.min(damagedAmount, victim.getHealth());
		int intAmount = (int) Math.ceil(damagedAmount);

		float healAmount = damagedAmount;
		if (hitter instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) hitter;
			ItemStack vampireTotem = new ItemStack(TotemEssentials.proxy.items.get("vampire_totem"));
			ItemStack match = getStackInPlayerInv(player, vampireTotem);
			if (match != ItemStack.EMPTY) {
				int totalDurability = match.getMaxDamage();
				int currentDamage = match.getItemDamage();
				if (currentDamage + intAmount >= totalDurability + 1) {
					healAmount = totalDurability - currentDamage + 1;
				}

				float maxHealth = player.getMaxHealth();
				float currentHealth = player.getHealth();
				player.heal(healAmount);

				if (currentHealth + healAmount > maxHealth) {
					match.damageItem((int) Math.ceil(maxHealth - currentHealth), player);
				} else {
					match.damageItem((int) Math.ceil(healAmount), player);
				}

			}
		}
	}
}
