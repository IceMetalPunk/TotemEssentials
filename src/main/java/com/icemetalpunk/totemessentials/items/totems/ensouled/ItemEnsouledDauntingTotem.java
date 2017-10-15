package com.icemetalpunk.totemessentials.items.totems.ensouled;

import java.util.List;

import com.icemetalpunk.totemessentials.entities.EntityCleanNT;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemEnsouledDauntingTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledDauntingTotem(String name) {
		super(name);
		this.setMaxDamage(75);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (worldIn.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}

		// Pushes mobs away when right-clicked without sneaking
		if (!playerIn.isSneaking()) {
			worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_CREEPER_HURT, SoundCategory.HOSTILE,
					1.0f, 1.0f);

			List<Entity> mobs = worldIn.getEntitiesWithinAABB(EntityLivingBase.class,
					playerIn.getEntityBoundingBox().offset(-7, -7, -7).expand(14, 14, 14));
			for (Entity mob : mobs) {
				if (mob == playerIn) {
					continue;
				}
				double dx = (mob.posX - playerIn.posX);
				double dz = (mob.posZ - playerIn.posZ);
				double dy = (mob.posY - playerIn.posY);
				double dist = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
				dx /= dist;
				dy /= dist;
				dz /= dist;

				mob.motionX += dx * 7;
				mob.motionY += dy;
				mob.motionZ += dz * 7;
			}

			stack.damageItem(1, playerIn);
		}
		// Clean-N-T when sneaking
		else {
			worldIn.spawnEntity(new EntityCleanNT(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn));
			stack.damageItem(5, playerIn);
		}
		if (stack.getItemDamage() >= stack.getMaxDamage()) {
			stack.shrink(1);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

}
