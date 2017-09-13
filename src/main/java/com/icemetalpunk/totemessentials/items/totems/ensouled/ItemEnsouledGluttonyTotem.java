package com.icemetalpunk.totemessentials.items.totems.ensouled;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemEnsouledGluttonyTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledGluttonyTotem(String name) {
		super(name);
		this.setMaxDamage(500); // Can cure 500 hunger
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) entityIn;
		FoodStats stats = player.getFoodStats();
		InventoryPlayer inv = player.inventory;

		// Get highest-saturation item stack in inventory
		if (stats.getFoodLevel() <= 4) {
			int amount = 20 - stats.getFoodLevel();
			stats.addStats(amount, 1.0f);
	        worldIn.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
			stack.damageItem(amount, player);
		}

	}

}
