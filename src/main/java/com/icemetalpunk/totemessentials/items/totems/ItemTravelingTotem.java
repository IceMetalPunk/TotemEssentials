package com.icemetalpunk.totemessentials.items.totems;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTravelingTotem extends ItemTotemBase {

	protected HashMap<Integer, String> dimensionNames = new HashMap<Integer, String>();

	public ItemTravelingTotem(String name) {
		super(name);
		this.setMaxDamage(250); // 250 durability - 1 damage per 10 blocks =
								// 2500 blocks total

		for (int dimID : DimensionManager.getStaticDimensionIDs()) {
			WorldProvider provider = DimensionManager.createProviderFor(dimID);
			dimensionNames.put(dimID, formatDimensionName(provider.getDimensionType().getName()));
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Destination")) {
			int[] destTag = stack.getTagCompound().getIntArray("Destination");
			String dimensionName = " - Unknown Dimension";
			if (destTag.length > 3) {
				if (dimensionNames.containsKey(Integer.valueOf(destTag[3]))) {
					dimensionName = " - " + dimensionNames.get(Integer.valueOf(destTag[3]));
				}
			}
			tooltip.add(I18n.format("item.traveling_totem.bound",
					new Object[] { destTag[0], destTag[1], destTag[2], dimensionName }));
		} else {
			tooltip.add(I18n.format("item.traveling_totem.unbound", new Object[0]));
		}
	}

	public String formatDimensionName(String name) {
		if (name.startsWith("the_")) {
			name = name.replaceFirst("the_", "");
		}
		name = name.replaceAll("_", " ");
		name = WordUtils.capitalizeFully(name);
		return name;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (worldIn.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}

		BlockPos pos = playerIn.getPosition();

		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}

		if (playerIn.isSneaking()) {
			nbt.setIntArray("Destination",
					new int[] { pos.getX(), pos.getY(), pos.getZ(), worldIn.provider.getDimension() });
			stack.setTagCompound(nbt);

			// TODO: Notify the player that it's been set in a better way.
			playerIn.sendMessage(new TextComponentTranslation("item.traveling_totem.set",
					new Object[] { pos.getX(), pos.getY(), pos.getZ() }));

		} else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Destination")) {

			int[] destTag = nbt.getIntArray("Destination");

			if (destTag.length > 3 && destTag[3] != worldIn.provider.getDimension()) {
				String dim1 = dimensionNames.get(destTag[3]);
				String dim2 = dimensionNames.get(worldIn.provider.getDimension());
				playerIn.sendMessage(new TextComponentTranslation("item.traveling_totem.wrong_dimension",
						new Object[] { dim1, dim2 }));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}

			BlockPos destination = new BlockPos(destTag[0], destTag[1], destTag[2]);
			double dist = pos.getDistance(destination.getX(), destination.getY(), destination.getZ());

			int maxDamage = (int) (1 + Math.round(dist / 10.0d));
			int damageLeft = stack.getMaxDamage() - stack.getItemDamage();
			int damage = Math.min(damageLeft, maxDamage);
			double t = (double) damage / (double) maxDamage;

			double newX = pos.getX() + t * (destination.getX() - pos.getX());
			double newY = pos.getY() + t * (destination.getY() - pos.getY());
			double newZ = pos.getZ() + t * (destination.getZ() - pos.getZ());

			stack.damageItem(damage, playerIn);
			playerIn.setPositionAndUpdate(newX, newY, newZ);

			// Effects
			Random random = worldIn.rand;
			for (int j = 0; j < 128; ++j) {
				double d6 = (double) j / 127.0D;
				float f = (random.nextFloat() - 0.5F) * 0.2F;
				float f1 = (random.nextFloat() - 0.5F) * 0.2F;
				float f2 = (random.nextFloat() - 0.5F) * 0.2F;
				double d3 = pos.getX() + (playerIn.posX - pos.getX()) * d6
						+ (random.nextDouble() - 0.5D) * (double) playerIn.width * 2.0D;
				double d4 = pos.getY() + (playerIn.posY - pos.getY()) * d6
						+ random.nextDouble() * (double) playerIn.height;
				double d5 = pos.getZ() + (playerIn.posZ - pos.getZ()) * d6
						+ (random.nextDouble() - 0.5D) * (double) playerIn.width * 2.0D;
				worldIn.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double) f, (double) f1, (double) f2);
			}
			worldIn.playSound((EntityPlayer) null, pos.getX(), pos.getY(), pos.getZ(),
					SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
			playerIn.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
		} else {
			playerIn.sendMessage(new TextComponentTranslation("item.traveling_totem.unset", new Object[0]));
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
}
