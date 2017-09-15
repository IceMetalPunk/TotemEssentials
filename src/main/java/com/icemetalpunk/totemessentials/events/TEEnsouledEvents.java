package com.icemetalpunk.totemessentials.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.icemetalpunk.totemessentials.TotemEssentials;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledAggressionTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledWisdomTotem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
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
	private static final HashSet<EntityPlayer> wisdomCloneMarker = new HashSet<EntityPlayer>();
	private HashMap<Class<? extends EntityLivingBase>, Integer> skullMap = new HashMap<>();

	public TEEnsouledEvents() {
		skullMap.put(EntitySkeleton.class, 0);
		skullMap.put(EntityWitherSkeleton.class, 1);
		skullMap.put(EntityZombie.class, 2);
		skullMap.put(EntityCreeper.class, 4);
		skullMap.put(EntityDragon.class, 5);
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
				if (!player.isSpectator()) {
					if (!player.isCreative()) {
						player.capabilities.allowFlying = false;
						player.capabilities.isFlying = false;
					}
					if (noNormal) {
						player.setNoGravity(false);
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

	// Ensouled Totem of Fireglaze
	@SubscribeEvent
	public void onFireDamage(LivingAttackEvent ev) {
		DamageSource source = ev.getSource();
		EntityLivingBase ent = ev.getEntityLiving();
		if (ent instanceof EntityPlayer && source.isFireDamage()) {
			EntityPlayer player = (EntityPlayer) ent;
			ItemStack totem = TEEvents.getStackInPlayerInv(player,
					new ItemStack(TotemEssentials.proxy.items.get("ensouled_fireglaze_totem"), 1));
			if (totem != null) {
				float amount = ev.getAmount();
				int intAmt = (int) Math.ceil(amount);
				int durabilityLeft = totem.getMaxDamage() - totem.getItemDamage();
				int damage = Math.min(durabilityLeft, intAmt);
				totem.damageItem(damage, player);
				if (totem.getItemDamage() >= totem.getMaxDamage()) {
					totem.damageItem(1, player);
				}
			}
		}
	}

	// Ensouled Totem of Aggression
	// Item#onUpdate happens too late/early!

	@SubscribeEvent
	public void onAttackTarget(LivingSetAttackTargetEvent ev) {
		EntityLivingBase target = ev.getTarget();
		if (target instanceof EntityPlayer) {
			ItemStack stack = TEEvents.getStackInPlayerInv((EntityPlayer) target,
					new ItemStack(TotemEssentials.proxy.items.get("ensouled_aggression_totem")));
			if (stack != ItemStack.EMPTY) {
				ItemEnsouledAggressionTotem.performEffect(stack, ev.getEntity().world, (EntityPlayer) target);
			}
		}
	}

	// Ensouled Totem of Wisdom
	@SubscribeEvent
	public void onDrops(PlayerDropsEvent ev) {
		EntityPlayer player = ev.getEntityPlayer();
		List<EntityItem> drops = ev.getDrops();

		if (player.experienceTotal > 0 && !player.getEntityWorld().getGameRules().getBoolean("keepInventory")) {
			Iterator<EntityItem> itr = drops.iterator();
			while (itr.hasNext()) {
				EntityItem item = itr.next();
				if (item.getItem().getItem() == TotemEssentials.proxy.items.get("ensouled_wisdom_totem")) {
					item.getItem().damageItem(ItemEnsouledWisdomTotem.DAMAGE_ON_USE, player);
					wisdomCloneMarker.add(player);
					if (item.getItem().getItemDamage() >= item.getItem().getMaxDamage()) {
						itr.remove();
					}

					break;
				}
			}
		}
	}

	@SubscribeEvent
	public void xpDrop(LivingExperienceDropEvent ev) {
		EntityLivingBase living = ev.getEntityLiving();
		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			if (wisdomCloneMarker.contains(player)) {
				ev.setDroppedExperience(0);
			}
		}

	}

	@SubscribeEvent
	public void onClone(Clone ev) {
		EntityPlayer original = ev.getOriginal();
		EntityPlayer newPlayer = ev.getEntityPlayer();
		boolean wasDeath = ev.isWasDeath();
		boolean hasEnsouled = wisdomCloneMarker.contains(original);

		if (hasEnsouled) {
			wisdomCloneMarker.remove(original);
			if (wasDeath) {
				if (newPlayer.experienceTotal <= 0) {
					newPlayer.addExperience(original.experienceTotal);
					float f = newPlayer.experienceLevel > 30 ? 1.0F : (float) newPlayer.experienceLevel / 30.0F;
					newPlayer.getEntityWorld().playSound(newPlayer, newPlayer.posX, newPlayer.posY, newPlayer.posZ,
							SoundEvents.ENTITY_PLAYER_LEVELUP, newPlayer.getSoundCategory(), f * 0.75F, 1.0F);
				}
			}
		}
	}

	// Ensouled Totem of Reaping head drops
	@SubscribeEvent
	public void ensouledReap(LivingDropsEvent ev) {
		Entity killer = ev.getSource().getTrueSource();
		EntityLivingBase living = ev.getEntityLiving();

		if (killer instanceof EntityPlayer) {

			World world = killer.getEntityWorld();

			// 33% chance
			int chance = world.rand.nextInt(100);
			if (chance > 33) {
				return;
			}

			EntityPlayer player = (EntityPlayer) killer;

			ItemStack drop = ItemStack.EMPTY;
			if (skullMap.containsKey(living.getClass())) {
				drop = new ItemStack(Items.SKULL, 1, skullMap.get(living.getClass()));
			} else if (living instanceof EntityPlayer) {
				drop = new ItemStack(Items.SKULL, 1, 3);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("SkullOwner", living.getDisplayName().getFormattedText());
				drop.setTagCompound(tag);
			} else {
				return; // Not a player or vanilla mob with head.
			}
			ItemStack totem = TEEvents.getStackInPlayerInv(player,
					new ItemStack(TotemEssentials.proxy.items.get("ensouled_reaping_totem")));
			if (totem != ItemStack.EMPTY) {
				totem.damageItem(10, player);
				ev.getDrops().add(new EntityItem(world, living.posX, living.posY, living.posZ, drop));
			}
		}
	}

	// Ensouled Undying Totem
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent ev) {
		EntityLivingBase living = ev.getEntityLiving();
		DamageSource source = ev.getSource();
		if (living.getEntityWorld().isRemote || !(living instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) living;
		ItemStack totem = TEEvents.getStackInPlayerInv(player,
				new ItemStack(TotemEssentials.proxy.items.get("ensouled_undying_totem")));
		if (totem != ItemStack.EMPTY) {
			player.setHealth(1.0F);
			player.clearActivePotions();
			player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
			player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
			player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 300, 0));

			player.fallDistance = 0.0f;
			if (source.isFireDamage()) {
				player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 300, 0));
			}
			if (source == DamageSource.OUT_OF_WORLD) {
				World world = player.getEntityWorld();
				int dimension = world.provider.getDimension();
				BlockPos spawnPos = player.getBedLocation(dimension);
				if (spawnPos == null) {
					// spawnPos = world.getSpawnPoint();
					spawnPos = player.getServer().getWorld(dimension).getSpawnCoordinate();
				}
				player.velocityChanged = true;
				player.setPositionAndUpdate(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
			}

			totem.damageItem(1, player);
			if (totem.getItemDamage() >= totem.getMaxDamage()) {
				totem.shrink(1);
			}

			player.world.setEntityState(player, (byte) 35);
			ev.setCanceled(true);
		}
	}
}
