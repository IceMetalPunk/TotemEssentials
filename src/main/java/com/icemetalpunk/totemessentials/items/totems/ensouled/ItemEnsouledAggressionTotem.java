package com.icemetalpunk.totemessentials.items.totems.ensouled;

import java.util.List;

import com.google.common.base.Predicate;
import com.icemetalpunk.totemessentials.items.totems.ItemAggressionTotem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemEnsouledAggressionTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledAggressionTotem(String name) {
		super(name);
		this.setMaxDamage(150); // Can aggro/calm mobs 150 times (calming
								// happens frequently!)
	}

	public static void performEffect(ItemStack stack, World world, Entity entity) {
		if (!(entity instanceof EntityLivingBase) || world.isRemote) {
			return;
		}
		WorldServer server = null;
		if (world instanceof WorldServer) {
			server = (WorldServer) world;
		}

		int turned = ItemAggressionTotem.aggroMobs(stack, world, entity);

		// If only 1 mob around, turned == 0, so calm the 1 mob
		if (turned == 0) {

			EntityLivingBase living = (EntityLivingBase) entity;

			// Get possible aggro targets
			double r = 20.0;
			AxisAlignedBB aabb = living.getEntityBoundingBox().expand(r, r, r).expand(-r, -r, -r);

			// Get possible turns
			List<EntityLiving> possibles = world.getEntitiesWithinAABB(EntityLiving.class, aabb,
					new Predicate<EntityLiving>() {
						@Override
						public boolean apply(EntityLiving input) {
							return (input != living && input instanceof IMob && input.getAttackTarget() == living);
						}
					});
			if (possibles.size() == 1) {
				EntityLiving target = possibles.get(0);

				// Calming effect
				target.setRevengeTarget(null);
				target.setAttackTarget(null);
				if (target instanceof AbstractSkeleton) {
					target.resetActiveHand();
				}
				stack.damageItem(1, living);

				// TODO: Add calming sound effect
				if (server != null) {
					for (int i = 0; i < 50; ++i) {
						server.spawnParticle(EnumParticleTypes.CRIT_MAGIC, target.posX, target.posY, target.posZ,
								world.rand.nextInt(2) + 1, 2, world.rand.nextInt(2) + 1);
					}
				}
			}
		}
	}
}
