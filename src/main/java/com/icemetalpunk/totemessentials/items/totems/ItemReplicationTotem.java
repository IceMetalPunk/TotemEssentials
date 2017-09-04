package com.icemetalpunk.totemessentials.items.totems;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemReplicationTotem extends ItemTotemBase {

	public ItemReplicationTotem(String name) {
		super(name);
		this.setMaxDamage(50); // 50 spawns
	}

	public String getNameFromId(String id) {
		ResourceLocation resID = new ResourceLocation(id);
		String ret = ForgeRegistries.ENTITIES.getValue(resID).getName();
		return I18n.format("entity." + ret + ".name", new Object[0]);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Binding")) {
			String name = getNameFromId(stack.getTagCompound().getString("Binding"));
			tooltip.add(I18n.format("item.replication_totem.boundTo", new Object[] { name }));
			Item p;
		} else {
			tooltip.add(I18n.format("item.replication_totem.unbound", new Object[0]));
		}
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		if (attacker.world.isRemote || !(attacker instanceof EntityPlayer)) {
			return false;
		}
		EntityPlayer player = (EntityPlayer) attacker;

		NBTTagCompound tag;
		if (stack.hasTagCompound()) {
			tag = stack.getTagCompound();
		} else {
			tag = new NBTTagCompound();
		}

		String id = target.serializeNBT().getString("id");
		tag.setString("Binding", id);
		stack.setTagCompound(tag);

		String name = getNameFromId(id);
		player.sendMessage(new TextComponentTranslation("item.replication_totem.set", new Object[] { name }));

		return false;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand handIn,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (worldIn.isRemote) {
			return EnumActionResult.PASS;
		}

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Binding")) {

			NBTTagCompound nbt = stack.getTagCompound();
			ResourceLocation id = new ResourceLocation(nbt.getString("Binding"));
			Entity mobSpawn = ForgeRegistries.ENTITIES.getValue(id).newInstance(worldIn);

			pos = pos.offset(facing);
			mobSpawn.setPosition(pos.getX() + 0.5, pos.getY() - (facing == EnumFacing.DOWN ? mobSpawn.height - 1 : 0),
					pos.getZ() + 0.5);
			worldIn.spawnEntity(mobSpawn);
			stack.damageItem(1, playerIn);

		} else {
			playerIn.sendMessage(new TextComponentTranslation("item.replication_totem.unset", new Object[0]));
			return EnumActionResult.FAIL;
		}

		return EnumActionResult.SUCCESS;
	}
}
