package com.icemetalpunk.totemessentials.items.totems.ensouled;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEnsouledFeatherfootTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledFeatherfootTotem(String name) {
		super(name);
		this.setMaxDamage(200); // Can absorb 100 points of fall damage.
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof EntityLivingBase)) {
			return;
		}
		EntityLivingBase living = (EntityLivingBase) entityIn;
		if (living.motionY < 0.0d && !living.isOnLadder() && !living.isElytraFlying()) {
			living.motionY *= 0.60d;
		}
	}

}
