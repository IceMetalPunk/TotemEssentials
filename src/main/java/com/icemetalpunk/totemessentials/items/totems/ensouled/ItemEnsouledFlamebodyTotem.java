package com.icemetalpunk.totemessentials.items.totems.ensouled;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;

public class ItemEnsouledFlamebodyTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledFlamebodyTotem(String name) {
		super(name);
		this.setMaxDamage(200); // Can set mobs on fire 200 times.
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (worldIn.isRemote || !(entityIn instanceof EntityLivingBase)) {
			return;
		}
		EntityLivingBase living = (EntityLivingBase) entityIn;
		List<Entity> list = worldIn.getEntitiesInAABBexcluding(living, living.getEntityBoundingBox(),
				EntitySelectors.getTeamCollisionPredicate(living));
		for (Iterator<Entity> it = list.iterator(); it.hasNext();) {
			Entity ent = it.next();
			if (!(ent instanceof EntityLivingBase) || ent.isBurning() || ent.isImmuneToFire()) {
				continue;
			}
			ent.setFire(5);
			stack.damageItem(1, living);
			if (stack.getItemDamage() >= stack.getMaxDamage()) {
				stack.shrink(1);
			}
		}
	}
}
