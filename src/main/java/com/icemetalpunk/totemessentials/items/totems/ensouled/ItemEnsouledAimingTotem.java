package com.icemetalpunk.totemessentials.items.totems.ensouled;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemEnsouledAimingTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledAimingTotem(String name) {
		super(name);
		this.setMaxDamage(200); // 200 arrows affected
	}

	// Highlight mobs when looked at with bow pulled
	public static EntityLivingBase getEntityLookedAt(EntityLivingBase player) {
		World world = player.getEntityWorld();

		EntityLivingBase highlightEnt = null;
		double nearest = 100;

		for (Entity ent : world.getLoadedEntityList()) {
			if (ent instanceof EntityLivingBase && ent != player) {
				Vec3d vec3d = player.getLook(1.0F).normalize();
				Vec3d vec3d1 = new Vec3d(ent.posX - player.posX, ent.getEntityBoundingBox().minY
						+ (double) ent.getEyeHeight() - (player.posY + (double) player.getEyeHeight()),
						ent.posZ - player.posZ);
				double d0 = vec3d1.lengthVector();
				vec3d1 = vec3d1.normalize();
				double d1 = vec3d.dotProduct(vec3d1);
				if (d0 <= 160 && d0 < nearest && d1 > 1.0D - 0.025D / d0 && player.canEntityBeSeen(ent)) {
					nearest = d0;
					highlightEnt = (EntityLivingBase) ent;
				}
			}
		}
		return highlightEnt;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof EntityLivingBase)) {
			return;
		}
		EntityLivingBase player = (EntityLivingBase) entityIn;
		ItemStack usingItem = player.getActiveItemStack();
		int isInUse = player.getItemInUseCount();
		if (usingItem.getItem() instanceof ItemBow && isInUse > 0) {
			EntityLivingBase livingEnt = getEntityLookedAt(player);
			if (livingEnt != null) {
				livingEnt.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 20));
			}
		}
	}

}
