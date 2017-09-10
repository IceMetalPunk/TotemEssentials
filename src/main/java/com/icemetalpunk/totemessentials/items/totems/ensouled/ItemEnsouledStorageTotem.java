package com.icemetalpunk.totemessentials.items.totems.ensouled;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEnsouledStorageTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledStorageTotem(String name) {
		super(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (worldIn.isRemote) {
			playerIn.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0f, 1.0f);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}

		int invSize = playerIn.inventory.mainInventory.size();
		int hotbarSize = playerIn.inventory.getHotbarSize();
		for (int col = 0; col < hotbarSize; ++col) {
			if (handIn == EnumHand.MAIN_HAND && col == playerIn.inventory.currentItem) {
				continue;
			}
			ItemStack temp = playerIn.inventory.getStackInSlot(col);
			for (int row = invSize - hotbarSize + col; row > hotbarSize; row -= hotbarSize) {
				playerIn.inventory.setInventorySlotContents((row + hotbarSize) % invSize,
						playerIn.inventory.getStackInSlot(row));
			}
			playerIn.inventory.setInventorySlotContents(hotbarSize + col, temp);
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

}
