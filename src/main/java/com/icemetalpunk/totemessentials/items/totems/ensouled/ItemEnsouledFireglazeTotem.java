package com.icemetalpunk.totemessentials.items.totems.ensouled;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemEnsouledFireglazeTotem extends ItemEnsouledTotemBase {

	public static final int DEATH_USE_DAMAGE = 15000;

	public ItemEnsouledFireglazeTotem(String name) {
		super(name);
		this.setMaxDamage(60000); // 60000 durability (about 5 minutes in lava)
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) entityIn;
			living.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 40, 0, false, false));
		}
	}

}
