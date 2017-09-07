package com.icemetalpunk.totemessentials.items.totems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemStorageTotem extends ItemTotemBase {

	public ItemStorageTotem(String name) {
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
		for (int i = 0; i < hotbarSize; ++i) {
			ItemStack temp = playerIn.inventory.getStackInSlot(invSize - hotbarSize + i);
			if (handIn != EnumHand.MAIN_HAND || i != playerIn.inventory.currentItem) {
				playerIn.inventory.setInventorySlotContents(invSize - hotbarSize + i,
						playerIn.inventory.getStackInSlot(i));
				playerIn.inventory.setInventorySlotContents(i, temp);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

}
