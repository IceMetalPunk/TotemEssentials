package com.icemetalpunk.totemessentials.events;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.icemetalpunk.totemessentials.TotemEssentials;
import com.icemetalpunk.totemessentials.config.TEConfig;
import com.icemetalpunk.totemessentials.items.EntityItemFireproof;
import com.icemetalpunk.totemessentials.items.totems.ItemFireglazeTotem;
import com.icemetalpunk.totemessentials.items.totems.ensouled.ItemEnsouledFireglazeTotem;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
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
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class TEEvents {
	private HashMap<Class<? extends Entity>, HashMap<Item, ItemStack>> dropReplacements = new HashMap<>();
	private static HashMap<Class<? extends Entity>, Item> essenceMap = new HashMap<>();

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
		if (TEConfig.recipeList.containsKey("Phasing Totem") && TEConfig.recipeList.get("Phasing Totem")) {
			essenceMap.put(EntityVex.class, TotemEssentials.proxy.items.get("essence_vexatious"));
		}
		if (TEConfig.recipeList.containsKey("Undying Totem") && TEConfig.recipeList.get("Undying Totem")) {
			essenceMap.put(EntityZombie.class, TotemEssentials.proxy.items.get("essence_undying"));
		}
		if (TEConfig.recipeList.containsKey("Curing Totem") && TEConfig.recipeList.get("Curing Totem")) {
			essenceMap.put(EntityCow.class, TotemEssentials.proxy.items.get("essence_lactic"));
		}
		if (TEConfig.recipeList.containsKey("Featherfoot Totem") && TEConfig.recipeList.get("Featherfoot Totem")) {
			essenceMap.put(EntityChicken.class, TotemEssentials.proxy.items.get("essence_featherfoot"));
		}
		if (TEConfig.recipeList.containsKey("Vampire Totem") && TEConfig.recipeList.get("Vampire Totem")) {
			essenceMap.put(EntityBat.class, TotemEssentials.proxy.items.get("essence_vampiric"));
		}
		if (TEConfig.recipeList.containsKey("Traveling Totem") && TEConfig.recipeList.get("Traveling Totem")) {
			essenceMap.put(EntityEnderman.class, TotemEssentials.proxy.items.get("essence_traveling"));
		}
		if (TEConfig.recipeList.containsKey("Replication Totem") && TEConfig.recipeList.get("Replication Totem")) {
			essenceMap.put(EntityIllusionIllager.class, TotemEssentials.proxy.items.get("essence_replication"));
		}
		if (TEConfig.recipeList.containsKey("Exchange Totem") && TEConfig.recipeList.get("Exchange Totem")) {
			essenceMap.put(EntityVillager.class, TotemEssentials.proxy.items.get("essence_exchange"));
		}
		if (TEConfig.recipeList.containsKey("Fireglaze Totem") && TEConfig.recipeList.get("Fireglaze Totem")) {
			essenceMap.put(EntityMagmaCube.class, TotemEssentials.proxy.items.get("essence_fireglaze"));
		}
		if (TEConfig.recipeList.containsKey("Storage Totem") && TEConfig.recipeList.get("Storage Totem")) {
			essenceMap.put(EntityShulker.class, TotemEssentials.proxy.items.get("essence_storage"));
		}
		if (TEConfig.recipeList.containsKey("Aiming Totem") && TEConfig.recipeList.get("Aiming Totem")) {
			essenceMap.put(EntitySkeleton.class, TotemEssentials.proxy.items.get("essence_aim"));
			essenceMap.put(EntityStray.class, TotemEssentials.proxy.items.get("essence_aim"));
		}
		if (TEConfig.recipeList.containsKey("Aggression Totem") && TEConfig.recipeList.get("Aggression Totem")) {
			essenceMap.put(EntityPigZombie.class, TotemEssentials.proxy.items.get("essence_aggro"));
		}
		if (TEConfig.recipeList.containsKey("Wisdom Totem") && TEConfig.recipeList.get("Wisdom Totem")) {
			essenceMap.put(EntityElderGuardian.class, TotemEssentials.proxy.items.get("essence_wisdom"));
		}
		if (TEConfig.recipeList.containsKey("Gluttony Totem") && TEConfig.recipeList.get("Gluttony Totem")) {
			essenceMap.put(EntityHusk.class, TotemEssentials.proxy.items.get("essence_gluttony"));
		}
		if (TEConfig.recipeList.containsKey("Flamebody Totem") && TEConfig.recipeList.get("Flamebody Totem")) {
			essenceMap.put(EntityBlaze.class, TotemEssentials.proxy.items.get("essence_flamebody"));
		}
		if (TEConfig.recipeList.containsKey("Daunting Totem") && TEConfig.recipeList.get("Daunting Totem")) {
			essenceMap.put(EntityCreeper.class, TotemEssentials.proxy.items.get("essence_daunting"));
		}
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
					player.setNoGravity(false);
				}
			}
		}
	}

	// Helper method to get a matching stack from a player's inventory,
	// regardless of which part of the inventory it's in.
	public static ItemStack getStackInPlayerInv(EntityPlayer player, ItemStack compareTo) {
		InventoryPlayer inv = player.inventory;
		ItemStack stack = ItemStack.EMPTY;

		ItemStack test = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			test = inv.getStackInSlot(i);
			if (!test.isEmpty() && test.getItem().equals(compareTo.getItem())) {
				stack = test;
				break;
			}
		}
		if (stack == ItemStack.EMPTY && !player.getHeldItemOffhand().isEmpty()
				&& player.getHeldItemOffhand().getItem().equals(compareTo.getItem())) {
			stack = player.getHeldItemOffhand();
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
			ItemStack totem = getStackInPlayerInv(player,
					new ItemStack(TotemEssentials.proxy.items.get("reaping_totem"), 1));
			ItemStack ensouled = getStackInPlayerInv(player,
					new ItemStack(TotemEssentials.proxy.items.get("ensouled_reaping_totem"), 1));
			ItemStack toDamage = (totem != ItemStack.EMPTY) ? totem : ensouled;
			if (toDamage != ItemStack.EMPTY) {
				toDamage.damageItem(1, player);
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

	}

	// Totem of Featherfoot handler
	@SubscribeEvent
	public void onFallDamage(LivingHurtEvent ev) {
		DamageSource source = ev.getSource();
		EntityLivingBase ent = ev.getEntityLiving();
		int intAmount = MathHelper.ceil(ev.getAmount());
		if (source.damageType == "fall" && ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ent;
			ItemStack totem = getStackInPlayerInv(player,
					new ItemStack(TotemEssentials.proxy.items.get("featherfoot_totem")));
			ItemStack ensouled = getStackInPlayerInv(player,
					new ItemStack(TotemEssentials.proxy.items.get("ensouled_featherfoot_totem")));
			ItemStack toDamage = (totem != ItemStack.EMPTY) ? totem : ensouled;
			if (toDamage != ItemStack.EMPTY) {
				int totalDurability = toDamage.getMaxDamage();
				int currentDamage = toDamage.getItemDamage();
				if (currentDamage + intAmount >= totalDurability + 1) {
					toDamage.damageItem(totalDurability - currentDamage + 1, player);
					intAmount -= totalDurability - currentDamage;
					ev.setAmount(intAmount);
				} else {
					toDamage.damageItem(intAmount, player);
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
		int intAmount = MathHelper.ceil(damagedAmount);

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
					match.damageItem(MathHelper.ceil(maxHealth - currentHealth), player);
				} else {
					match.damageItem(MathHelper.ceil(healAmount), player);
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
			boolean isTotem = stack.getItem() == TotemEssentials.proxy.items.get("fireglaze_totem");
			boolean isEnsouled = stack.getItem() == TotemEssentials.proxy.items.get("ensouled_fireglaze_totem");
			if (isTotem || isEnsouled) {
				hasTotem = true;
				if (isTotem) {
					stack.setItemDamage(stack.getItemDamage() + ItemFireglazeTotem.DEATH_USE_DAMAGE);
				} else {
					stack.setItemDamage(stack.getItemDamage() + ItemEnsouledFireglazeTotem.DEATH_USE_DAMAGE);
				}
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
			List<EntityArrow> arrows = world.getEntities(EntityArrow.class, new Predicate<EntityArrow>() {
				@Override
				public boolean apply(EntityArrow input) {
					return (!input.hasNoGravity() && input.shootingEntity instanceof EntityPlayer);
				}
			});

			for (EntityArrow arrow : arrows) {
				EntityPlayer shooter = (EntityPlayer) arrow.shootingEntity;
				ItemStack totem = getStackInPlayerInv(shooter,
						new ItemStack(TotemEssentials.proxy.items.get("aiming_totem"), 1));
				ItemStack ensouled = getStackInPlayerInv(shooter,
						new ItemStack(TotemEssentials.proxy.items.get("ensouled_aiming_totem"), 1));
				ItemStack toDamage = (totem != ItemStack.EMPTY) ? totem : ensouled;
				if (toDamage != ItemStack.EMPTY) {
					arrow.setNoGravity(true);
					toDamage.damageItem(1, shooter);
				}
			}
		}
	}

	// Totem of Wisdom XP doubling
	@SubscribeEvent
	public void onPickupXP(PlayerPickupXpEvent ev) {
		EntityXPOrb orb = ev.getOrb();
		EntityPlayer player = ev.getEntityPlayer();
		ItemStack totem = getStackInPlayerInv(player, new ItemStack(TotemEssentials.proxy.items.get("wisdom_totem")));
		ItemStack ensouled = getStackInPlayerInv(player,
				new ItemStack(TotemEssentials.proxy.items.get("ensouled_wisdom_totem")));
		if (totem != ItemStack.EMPTY || ensouled != ItemStack.EMPTY) {
			int amount = orb.getXpValue();
			amount = Math.min(amount, totem.getMaxDamage() - totem.getItemDamage());
			orb.xpValue += amount;
			totem.damageItem(amount, player);
		}
	}

	// Totem of Flamebody: set things on fire when they hit you
	@SubscribeEvent
	public void onFlameBody(LivingHurtEvent ev) {
		EntityLivingBase living = ev.getEntityLiving();
		Entity source = ev.getSource().getTrueSource();

		if (!(living instanceof EntityPlayer) || source == null) {
			return;
		}
		EntityPlayer player = (EntityPlayer) living;
		ItemStack totem = getStackInPlayerInv(player,
				new ItemStack(TotemEssentials.proxy.items.get("flamebody_totem")));
		if (totem != ItemStack.EMPTY) {
			source.setFire(7);
			totem.damageItem(1, player);

			// Vanilla checks >, not >=, so it allows 0-durability tools! FTFY.
			if (totem.getItemDamage() >= totem.getMaxDamage()) {
				player.renderBrokenItemStack(totem);
				totem.shrink(1);
			}

		}

	}

	// Totem of Exchange v2 anvil stuff
	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent ev) {
		ItemStack left = ev.getLeft();
		ItemStack actual = ev.getRight();
		Item normalTotem = TotemEssentials.proxy.items.get("exchange_totem");
		Item ensouledTotem = TotemEssentials.proxy.items.get("ensouled_exchange_totem");
		if (!left.getItem().equals(normalTotem) && !left.getItem().equals(ensouledTotem)) {
			return;
		}

		ItemStack packet = new ItemStack(TotemEssentials.proxy.items.get("exchange_packet"));
		NBTTagCompound tags = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		List<IRecipe> recipes = GameRegistry.findRegistry(IRecipe.class).getValues();
		int neededCount = 1;
		for (IRecipe recipe : recipes) {
			ItemStack output = recipe.getRecipeOutput();
			if (actual.isItemEqualIgnoreDurability(output) && actual.getCount() >= output.getCount()) {
				neededCount = output.getCount();
				for (Ingredient input : recipe.getIngredients()) {
					ItemStack[] matches = input.getMatchingStacks();
					if (matches.length > 0) {
						NBTTagCompound component = new NBTTagCompound();
						matches[0].writeToNBT(component);
						list.appendTag(component);
					}
				}
				break;
			}
		}
		if (list.tagCount() > 0) {
			tags.setTag("Components", list);
			packet.setTagCompound(tags);
			ev.setOutput(packet);
			int cost = 10;
			if (left.getItem().equals(ensouledTotem)) {
				cost = 5;
			}
			ev.setCost(cost);
			ev.setMaterialCost(neededCount);
		}

	}

	@SubscribeEvent
	public void onRepair(AnvilRepairEvent ev) {
		ItemStack input = ev.getItemInput();
		ItemStack result = ev.getItemResult();
		EntityPlayer player = ev.getEntityPlayer();

		Item normalTotem = TotemEssentials.proxy.items.get("exchange_totem");
		Item ensouledTotem = TotemEssentials.proxy.items.get("ensouled_exchange_totem");
		if (input.getItem().equals(normalTotem) || input.getItem().equals(ensouledTotem)) {
			ItemStack give = input.copy();

			if (!player.isCreative()) {
				give.damageItem(1, player);
			}
			if (give.getItemDamage() < give.getMaxDamage()) {

				if (player.openContainer instanceof ContainerRepair) {
					ContainerRepair cont = (ContainerRepair) player.openContainer;
					anvilRefreshers.put(player, Pair.of(cont, give));
				} else {
					TotemEssentials.giveItems(give, player);
				}
			}
		}
	}

	public static HashMap<EntityPlayer, Pair<ContainerRepair, ItemStack>> anvilRefreshers = new HashMap<>();

	@SubscribeEvent
	public void onTick(PlayerTickEvent ev) {
		if (anvilRefreshers.containsKey(ev.player)) {
			Pair<ContainerRepair, ItemStack> pair = anvilRefreshers.get(ev.player);
			pair.getLeft().putStackInSlot(0, pair.getRight());
			anvilRefreshers.remove(ev.player);
		}
	}

}
