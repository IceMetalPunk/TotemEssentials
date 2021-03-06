package com.icemetalpunk.totemessentials.items.totems;

import java.util.List;

import com.google.common.base.Predicate;
import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemAggressionTotem extends ItemTotemBase {

	public ItemAggressionTotem(String name) {
		super(name);
		this.setMaxDamage(50); // Can aggro 50 mobs
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		aggroMobs(stack, worldIn, entityIn);
	}

	public static int aggroMobs(ItemStack stack, World world, Entity entity) {
		if (!(entity instanceof EntityLivingBase) || world.isRemote) {
			return 0;
		}

		WorldServer server = null;
		if (world instanceof WorldServer) {
			server = (WorldServer) world;
		}

		int turned = 0;
		EntityLivingBase living = (EntityLivingBase) entity;

		// Get possible aggro targets
		double r = 20.0;
		AxisAlignedBB aabb = living.getEntityBoundingBox().expand(r, r, r).expand(-r, -r, -r);

		List<EntityLivingBase> others = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb,
				new Predicate<EntityLivingBase>() {
					@Override
					public boolean apply(EntityLivingBase input) {
						return input != living;
					}
				});

		// Get possible turns
		List<EntityLiving> possibles = world.getEntitiesWithinAABB(EntityLiving.class, aabb,
				new Predicate<EntityLiving>() {
					@Override
					public boolean apply(EntityLiving input) {
						return (input != living && input instanceof IMob && input.getAttackTarget() == living);
					}
				});

		// If there are no other valid targets, this would cause an infinite
		// loop, so don't bother.
		if (others.size() <= 1) {
			return 0;
		}
		for (EntityLiving target : possibles) {
			EntityLivingBase newTarget;
			do {
				newTarget = others.get(world.rand.nextInt(others.size()));
			} while (newTarget == target);

			target.setRevengeTarget(newTarget);
			target.setAttackTarget(newTarget);
			stack.damageItem(1, living);
			++turned;

			if (server != null) {
				for (int i = 0; i < 50; ++i) {
					server.spawnParticle(EnumParticleTypes.CRIT_MAGIC, target.posX, target.posY, target.posZ,
							world.rand.nextInt(2) + 1, 2, world.rand.nextInt(2) + 1);
				}
			}
		}

		if (turned > 0) {
			world.playSound(null, entity.getPosition(), TotemEssentials.proxy.sounds.get("sfx_aggro"),
					SoundCategory.AMBIENT, 1.0f, 1.0f);
		}
		return turned;
	}
}