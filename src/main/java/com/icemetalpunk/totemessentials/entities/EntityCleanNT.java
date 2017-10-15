package com.icemetalpunk.totemessentials.entities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityCleanNT extends EntityTNTPrimed {

	public EntityCleanNT(World worldIn) {
		super(worldIn);
		this.setFuse(60);
	}

	public EntityCleanNT(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
		super(worldIn, x, y, z, igniter);
		this.setFuse(60);
	}

	@Override
	public void onUpdate() {
		int theFuse = this.getFuse();
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (!this.hasNoGravity()) {
			this.motionY -= 0.03999999910593033D;
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
			this.motionY *= -0.5D;
		}

		this.setFuse(--theFuse);

		if (theFuse <= 0) {
			this.setDead();

			if (!this.world.isRemote) {
				this.cleanExplode();
			}
		} else {
			this.handleWaterMovement();
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D,
					0.0D);
		}
	}

	public void cleanExplode() {
		this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE,
				SoundCategory.BLOCKS, 4.0F,
				(1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

		this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posZ, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);

		Iterable<BlockPos> blocks = BlockPos.getAllInBox(this.getPosition().add(-3, -3, -3),
				this.getPosition().add(3, 3, 3));
		for (BlockPos pos : blocks) {
			IBlockState state = this.world.getBlockState(pos);
			if (state.getBlockHardness(this.world, pos) >= 0) {
				this.world.destroyBlock(pos, true);
			}
		}
	}
}
