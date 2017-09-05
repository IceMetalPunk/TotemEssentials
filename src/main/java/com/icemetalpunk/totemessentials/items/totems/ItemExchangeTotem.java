package com.icemetalpunk.totemessentials.items.totems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemExchangeTotem extends ItemTotemBase {

	protected ArrayList<NonNullList<ItemStack>> oreList = new ArrayList<NonNullList<ItemStack>>();
	protected Set<Block> oreBlocks = new HashSet<Block>();

	public ItemExchangeTotem(String name) {
		super(name);
		this.setMaxDamage(50); // 50 exchanges
		buildOreList();
	}

	protected void buildOreList() {
		for (String ore : OreDictionary.getOreNames()) {
			if (ore.startsWith("ore")) {
				NonNullList<ItemStack> allOres = OreDictionary.getOres(ore, true);
				oreList.add(allOres);
				for (ItemStack stack : allOres) {
					Item item = stack.getItem();
					if (Block.getBlockFromItem(item) != Blocks.AIR) {
						oreBlocks.add(Block.getBlockFromItem(item));
					}
				}
			}
		}
		oreBlocks.add(Blocks.LIT_REDSTONE_ORE);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (worldIn.isRemote) {
			return EnumActionResult.PASS;
		}

		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if (oreBlocks.contains(block)) {
			int randListInd = worldIn.rand.nextInt(oreList.size());
			NonNullList<ItemStack> randList = oreList.get(randListInd);
			if (randList.size() > 0) {
				int randOreInd = worldIn.rand.nextInt(randList.size());
				ItemStack oreStack = randList.get(randOreInd);
				Block oreBlock = Block.getBlockFromItem(oreStack.getItem());
				if (oreBlock != Blocks.AIR) {
					worldIn.setBlockState(pos, oreBlock.getDefaultState());
					stack.damageItem(1, playerIn);
					// TODO: Add sound effects/particles/whatever.
					return EnumActionResult.SUCCESS;
				}
			}
		}

		return EnumActionResult.PASS;
	}
}
