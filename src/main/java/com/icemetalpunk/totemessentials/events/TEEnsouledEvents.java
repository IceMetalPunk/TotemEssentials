package com.icemetalpunk.totemessentials.events;

import java.util.HashMap;

import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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

	// Phasing if holding the Ensouled Phasing Totem
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

	// Ensouled Vampiric Totem
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
			ItemStack vampireTotem = new ItemStack(TotemEssentials.proxy.items.get("ensouled_vampire_totem"));
			ItemStack match = TEEvents.getStackInPlayerInv(player, vampireTotem);
			if (match != ItemStack.EMPTY) {
				int totalDurability = match.getMaxDamage();
				int currentDamage = match.getItemDamage();
				if (currentDamage + intAmount >= totalDurability + 1) {
					healAmount = totalDurability - currentDamage + 1;
				}

				float maxHealth = player.getMaxHealth();
				float currentHealth = player.getHealth();
				player.heal(healAmount * 2.0f);
				victim.addPotionEffect(new PotionEffect(TotemEssentials.proxy.potions.get("potion_solar"), 160));

				if (currentHealth + healAmount > maxHealth) {
					match.damageItem((int) Math.ceil(maxHealth - currentHealth), player);
				} else {
					match.damageItem((int) Math.ceil(healAmount), player);
				}

			}
		}
	}

	// The "Solar" effect for the Ensouled Vampiric Totem
	@SubscribeEvent
	public void onPotionEffects(LivingEvent.LivingUpdateEvent ev) {
		EntityLivingBase ent = ev.getEntityLiving();
		World world = ent.getEntityWorld();
		BlockPos pos = ent.getPosition();
		if (world.isDaytime() && world.canSeeSky(pos)
				&& ent.isPotionActive(TotemEssentials.proxy.potions.get("potion_solar"))) {
			ent.setFire(1);
		}
	}

	// FIXME: Add glowing to entities you aim at with a bow if you have the Ensouled
	// Totem of Aiming
	@SubscribeEvent
	public void onPlayerUsingBow(PlayerTickEvent ev) {
		if (ev.phase == TickEvent.Phase.END) {
			EntityPlayer player = ev.player;
			ItemStack usingItem = player.getActiveItemStack();
			int isInUse = player.getItemInUseCount();
			ItemStack totem = TEEvents.getStackInPlayerInv(player,
					new ItemStack(TotemEssentials.proxy.items.get("ensouled_aiming_totem"), 1));
			if (totem != ItemStack.EMPTY && usingItem.getItem() instanceof ItemBow && isInUse > 0) {
				// RayTraceResult trace =
				// Minecraft.getMinecraft().objectMouseOver;
				RayTraceResult trace = player.rayTrace(80.0d, 1.0f);
				System.out.println("Type: " + trace.typeOfHit);
				System.out.println("Entity: " + trace.entityHit);
				System.out.println("Is Living?: " + (trace.entityHit instanceof EntityLivingBase));
				if (trace.typeOfHit == RayTraceResult.Type.ENTITY && trace.entityHit != null
						&& trace.entityHit instanceof EntityLivingBase) {
					EntityLivingBase livingEnt = (EntityLivingBase) trace.entityHit;
					livingEnt.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 20));
					System.out.println("Added glowing to " + livingEnt);
				}
			}
		}
	}
}
