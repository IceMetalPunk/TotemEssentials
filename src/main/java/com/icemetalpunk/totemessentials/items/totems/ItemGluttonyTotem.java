package com.icemetalpunk.totemessentials.items.totems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

public class ItemGluttonyTotem extends ItemTotemBase {

	public ItemGluttonyTotem(String name) {
		super(name);
		this.setMaxDamage(100); // Can cure 100/5=20 saturation
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
			float highSat = 0;
			ItemStack bestFood = ItemStack.EMPTY;
			for (int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack slotStack = inv.getStackInSlot(i);
				if (slotStack.getItem() instanceof ItemFood) {
					ItemFood food = (ItemFood) slotStack.getItem();
					if (food.getSaturationModifier(slotStack) > highSat) {
						highSat = food.getSaturationModifier(slotStack);
						bestFood = slotStack;
					}
				}
			}

			if (bestFood != ItemStack.EMPTY) {
				bestFood.getItem().onItemUseFinish(bestFood, worldIn, player);
				stack.damageItem((int) Math.ceil(highSat * 5), player);
			}
		}

	}

}
