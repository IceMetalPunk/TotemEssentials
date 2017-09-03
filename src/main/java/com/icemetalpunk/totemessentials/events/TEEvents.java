package com.icemetalpunk.totemessentials.events;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.icemetalpunk.totemessentials.TotemEssentials;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class TEEvents {
	private HashMap<Class<? extends Entity>, HashMap<Item, ItemStack>> dropReplacements = new HashMap<Class<? extends Entity>, HashMap<Item, ItemStack>>();
	private HashMap<Class<? extends Entity>, Item> essenceMap = new HashMap<Class<? extends Entity>, Item>();

	public final SpawnListEntry illusionerSpawn = new SpawnListEntry(EntityIllusionIllager.class, 100, 1, 1);

	public TEEvents() {
		// Populate drop replacements map
		HashMap<Item, ItemStack> tempMap = new HashMap<Item, ItemStack>();
		tempMap.put(Items.TOTEM_OF_UNDYING, new ItemStack((Item) TotemEssentials.proxy.items.get("totem_shell"), 1));
		dropReplacements.put(EntityEvoker.class, tempMap);

		// Populate essence drop map
		essenceMap.put(EntityVex.class, TotemEssentials.proxy.items.get("essence_vexatious"));
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

	// Really hacky re-building of Mansion isInsideStructure code that ignores
	// the wonky isValid flag.
	private boolean isInMansion(ChunkProviderServer prov, World world, BlockPos pos) {
		IChunkGenerator chunkGen = prov.chunkGenerator;
		if (chunkGen instanceof ChunkGeneratorOverworld) {
			ChunkGeneratorOverworld overworldGen = (ChunkGeneratorOverworld) chunkGen;

			// Woodland Mansion Generator is private; need to reflect into it?
			Field mansionField = ReflectionHelper.findField(ChunkGeneratorOverworld.class, "woodlandMansionGenerator",
					"field_191060_C", "C");
			try {
				WoodlandMansion mansionGen = (WoodlandMansion) mansionField.get(overworldGen);
				Method initializeMethod = ReflectionHelper.findMethod(MapGenStructure.class, "initializeStructureData",
						"func_143027_a", World.class);
				initializeMethod.invoke(mansionGen, world);
				return getMansionAtIgnoreFlag(mansionGen, pos) != null;

			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return prov.chunkGenerator.isInsideStructure(world, "Mansion", pos); // Fallback
	}

	private StructureStart getMansionAtIgnoreFlag(MapGenStructure gen, BlockPos pos) {
		Field mapField = ReflectionHelper.findField(MapGenStructure.class, "structureMap", "field_75053_d", "c");

		try {
			Long2ObjectMap<StructureStart> map = (Long2ObjectMap<StructureStart>) mapField.get(gen);

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

	// FIXME: Make Illusioners spawn in Mansions; DOESN'T WORK.
	@SubscribeEvent
	public void alterSpawns(WorldEvent.PotentialSpawns ev) {
		World world = ev.getWorld();
		BlockPos pos = ev.getPos();
		IChunkProvider prov = world.getChunkProvider();
		// System.out.println("Get potential spawns!");
		if (ev.getType() == EnumCreatureType.MONSTER && prov instanceof ChunkProviderServer) {
			ChunkProviderServer serverProv = (ChunkProviderServer) prov;
			// System.out.println("Is Monster & Server Provider");
			if (isInMansion(serverProv, world, pos)) {
				// System.out.println("Mansion at " + pos);
				ev.getList().clear();
				ev.getList().add(illusionerSpawn);
			}
		}
		// Reference: ChunkGeneratorOverworld#getPossibleCreatures
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
