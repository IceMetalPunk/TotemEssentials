package com.icemetalpunk.totemessentials.items.totems.ensouled;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEnsouledPhasingTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledPhasingTotem(String name) {
		super(name);
		this.setMaxDamage(1200); // 60 seconds in a block (1200 ticks)
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) entityIn;

		if (!isSelected && player.inventory.offHandInventory.get(0).getItem() != this) {
			return;
		}

		if (!player.getEntityData().hasKey("isPhaseFlying") || !player.getEntityData().getBoolean("isPhaseFlying")) {
			player.getEntityData().setBoolean("isPhaseFlying", true);
			player.getEntityData().setBoolean("canOtherFlying", player.capabilities.allowFlying);
			player.getEntityData().setBoolean("wasOtherFlying", player.capabilities.isFlying);
			player.getEntityData().setBoolean("hadOtherNoGravity", player.hasNoGravity());
			player.getEntityData().setBoolean("hadOtherNoClip", player.noClip);
		}
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
