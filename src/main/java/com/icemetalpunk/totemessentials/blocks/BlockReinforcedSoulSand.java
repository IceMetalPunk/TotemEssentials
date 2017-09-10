package com.icemetalpunk.totemessentials.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockReinforcedSoulSand extends TEBlock {

	public BlockReinforcedSoulSand(String name) {
		super(name, Material.SAND, MapColor.BROWN);
		this.setSoundType(SoundType.SAND);
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		world.scheduleUpdate(pos, this, this.tickRate(world));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			this.checkFallable(worldIn, pos);
		}
	}

	private void checkFallable(World worldIn, BlockPos pos) {
		if ((worldIn.isAirBlock(pos.down()) || BlockFalling.canFallThrough(worldIn.getBlockState(pos.down())))
				&& pos.getY() >= 0) {
			int i = 32;

			if (worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
				if (!worldIn.isRemote) {
					EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D,
							(double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
					this.onStartFalling(entityfallingblock);
					worldIn.spawnEntity(entityfallingblock);
				}
			} else {
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.setBlockToAir(pos);
				BlockPos blockpos;

				for (blockpos = pos.down(); (worldIn.isAirBlock(blockpos)
						|| BlockFalling.canFallThrough(worldIn.getBlockState(blockpos)))
						&& blockpos.getY() > 0; blockpos = blockpos.down()) {
					;
				}

				if (blockpos.getY() > 0) {
					worldIn.setBlockState(blockpos.up(), state);
				}
			}
		}
	}

	protected void onStartFalling(EntityFallingBlock fallingEntity) {
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World worldIn) {
		return 20;
	}

	public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
	}

	public void onBroken(World worldIn, BlockPos pos) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextInt(16) == 0) {
			BlockPos blockpos = pos.down();

			if (BlockFalling.canFallThrough(worldIn.getBlockState(blockpos))) {
				double d0 = (double) ((float) pos.getX() + rand.nextFloat());
				double d1 = (double) pos.getY() - 0.05D;
				double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
				worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D,
						Block.getStateId(stateIn));
			}
		}
	}

}
