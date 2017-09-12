package com.icemetalpunk.totemessentials.events;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.icemetalpunk.totemessentials.TotemEssentials;
import com.icemetalpunk.totemessentials.items.EntityItemFireproof;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.WoodlandMansion;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class TEEvents {
	private HashMap<Class<? extends Entity>, HashMap<Item, ItemStack>> dropReplacements = new HashMap<Class<? extends Entity>, HashMap<Item, ItemStack>>();
	private HashMap<Class<? extends Entity>, Item> essenceMap = new HashMap<Class<? extends Entity>, Item>();

	public final SpawnListEntry illusionerSpawn = new SpawnListEntry(EntityIllusionIllager.class, 100, 1, 1);

	// Woodland Mansion Generator is private; need to reflect into it?
	private static final Field mansionField = ReflectionHelper.findField(ChunkGeneratorOverworld.class,
			"woodlandMansionGenerator", "field_191060_C");
	private static final Method initializeMansionMethod = ReflectionHelper.findMethod(MapGenStructure.class,
			"initializeStructureData", "func_143027_a", World.class);
	private static final Field structMapField = ReflectionHelper.findField(MapGenStructure.class, "structureMap",
			"field_75053_d");

	// Reflection for EntityItem, for fireproof items.
	private static Field itemAgeField = ReflectionHelper.findField(EntityItem.class, "age", "field_70292_b", "d");
	private static Field itemDelayField = ReflectionHelper.findField(EntityItem.class, "delayBeforeCanPickup",
			"field_145804_b", "e");

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
		essenceMap.put(EntityEnderman.class, TotemEssentials.proxy.items.get("essence_traveling"));
		essenceMap.put(EntityIllusionIllager.class, TotemEssentials.proxy.items.get("essence_replication"));
		essenceMap.put(EntityVillager.class, TotemEssentials.proxy.items.get("essence_exchange"));
		essenceMap.put(EntityMagmaCube.class, TotemEssentials.proxy.items.get("essence_fireglaze"));
		essenceMap.put(EntityShulker.class, TotemEssentials.proxy.items.get("essence_storage"));
	}

	// Phasing if holding the Phasing Totem
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent ev) {
		if (ev.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ev.getEntityLiving();
			Item mainHand = player.getHeldItemMainhand().getItem();
			Item offHand = player.getHeldItemOffhand().getItem();
			Item totem = TotemEssentials.proxy.items.get("phasing_totem");
			if (mainHand == totem || offHand == totem) {
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
			Item totem = TotemEssentials.proxy.items.get("phasing_totem");
			Item ensouledTotem = TotemEssentials.proxy.items.get("ensouled_phasing_totem");

			// If you don't check for the non-ensouled totem of phasing, they
			// interfere!
			// And then this stops the noclip from the normal totem! :(
			// So check for both here!
			if (mainHand != totem && offHand != totem && mainHand != ensouledTotem && offHand != ensouledTotem) {
				if (!player.isSpectator()) {
					player.noClip = false;
				}
				player.setNoGravity(false);
			}
		}
	}

	// Helper method to get a matching stack from a player's inventory,
	// regardless of which part of the inventory it's in.
	public static ItemStack getStackInPlayerInv(EntityPlayer player, ItemStack compareTo) {
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

	// Really hacky re-building of Mansion isInsideStructure code that ignores
	// the wonky isValid flag.
	private boolean isInMansion(ChunkProviderServer prov, World world, BlockPos pos) {
		IChunkGenerator chunkGen = prov.chunkGenerator;
		if (chunkGen instanceof ChunkGeneratorOverworld) {
			ChunkGeneratorOverworld overworldGen = (ChunkGeneratorOverworld) chunkGen;

			try {
				WoodlandMansion mansionGen = (WoodlandMansion) mansionField.get(overworldGen);
				initializeMansionMethod.invoke(mansionGen, world);
				return getMansionAtIgnoreFlag(mansionGen, pos) != null;

			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return prov.chunkGenerator.isInsideStructure(world, "Mansion", pos); // Fallback
	}

	private StructureStart getMansionAtIgnoreFlag(MapGenStructure gen, BlockPos pos) {

		try {
			Long2ObjectMap<StructureStart> map = (Long2ObjectMap<StructureStart>) structMapField.get(gen);

			ObjectIterator objectiterator = map.values().iterator();
			label31: while (objectiterator.hasNext()) {
				StructureStart structurestart = (StructureStart) objectiterator.next();

				// Removed sizeableStructure check from the below condition.
				if (structurestart.getBoundingBox().isVecInside(pos)) {
					Iterator<StructureComponent> iterator = structurestart.getComponents().iterator();

					while (true) {
						if (!iterator.hasNext()) {
							continue label31;
						}

						StructureComponent structurecomponent = iterator.next();

						if (structurecomponent.getBoundingBox().isVecInside(pos)) {
							break;
						}
					}

					return structurestart;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// Illusioners spawn in Woodland Mansions.
	@SubscribeEvent
	public void alterSpawns(WorldEvent.PotentialSpawns ev) {
		World world = ev.getWorld();
		BlockPos pos = ev.getPos();
		IChunkProvider prov = world.getChunkProvider();
		if (ev.getType() == EnumCreatureType.MONSTER && prov instanceof ChunkProviderServer) {
			ChunkProviderServer serverProv = (ChunkProviderServer) prov;
			if (isInMansion(serverProv, world, pos)) {
				ev.getList().clear();
				ev.getList().add(illusionerSpawn);
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

	// Fireproof items for Totem of Fireglaze
	@SubscribeEvent
	public void onDeathDrops(PlayerDropsEvent ev) {

		// Check for totem in drops list.
		boolean hasTotem = false;
		World world = ev.getEntity().getEntityWorld();
		List<EntityItem> drops = ev.getDrops();
		ListIterator<EntityItem> itr = drops.listIterator();
		while (itr.hasNext()) {
			EntityItem item = itr.next();
			ItemStack stack = item.getItem();
			if (stack.getItem() == TotemEssentials.proxy.items.get("fireglaze_totem")) {
				hasTotem = true;
				stack.setItemDamage(stack.getItemDamage() + 1);
				if (stack.getItemDamage() >= stack.getItem().getMaxDamage(stack)) {
					stack.shrink(1);
					itr.remove();
				}
				break;
			}
		}

		if (hasTotem) {

			// Go through drops, replacing them all with fireproof versions with
			// otherwise identical properties.
			itr = drops.listIterator();
			while (itr.hasNext()) {
				EntityItem item = itr.next();
				if (!(item instanceof EntityItemFireproof)) {
					ItemStack newStack = item.getItem().copy();

					EntityItemFireproof fireproofItem = new EntityItemFireproof(world, item.posX, item.posY, item.posZ,
							newStack);

					int age = item.getAge();
					try {
						int delay = itemDelayField.getInt(item);
						itemDelayField.setInt(fireproofItem, delay);
						itemAgeField.setInt(fireproofItem, age);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}

					fireproofItem.motionX = item.motionX;
					fireproofItem.motionY = item.motionY;
					fireproofItem.motionZ = item.motionZ;

					itr.set(fireproofItem);

				}
			}
		}
	}

	// Totem of Aiming
	@SubscribeEvent
	public void onArrowTick(WorldTickEvent ev) {
		World world = ev.world;
		TickEvent.Type type = ev.type;
		TickEvent.Phase phase = ev.phase;

		if (type == TickEvent.Type.WORLD && phase == TickEvent.Phase.START) {
			for (Entity ent : world.loadedEntityList) {
				if (ent instanceof EntityArrow) {
					EntityArrow arrow = (EntityArrow) ent;
					if (!arrow.hasNoGravity() && arrow.shootingEntity instanceof EntityPlayer) {
						EntityPlayer shooter = (EntityPlayer) arrow.shootingEntity;
						ItemStack totem = getStackInPlayerInv(shooter,
								new ItemStack(TotemEssentials.proxy.items.get("aiming_totem"), 1));
						ItemStack ensouled = getStackInPlayerInv(shooter,
								new ItemStack(TotemEssentials.proxy.items.get("ensouled_aiming_totem"), 1));
						if (totem != ItemStack.EMPTY) {
							arrow.setNoGravity(true);
							totem.damageItem(1, shooter);
						} else if (ensouled != ItemStack.EMPTY) {
							arrow.setNoGravity(true);
							ensouled.damageItem(1, shooter);
						}
					}
				}
			}
		}

	}
}
