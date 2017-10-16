package com.icemetalpunk.totemessentials.items;

import java.util.List;

import javax.annotation.Nullable;

import com.icemetalpunk.totemessentials.IOreDicted;
import com.icemetalpunk.totemessentials.TotemEssentials;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemExchangePacket extends TEItem implements IOreDicted {

	public ItemExchangePacket(String name) {
		super(name);
		this.setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("item.totemessentials.exchange_packet.instructions", new Object[0]));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (!stack.hasTagCompound()) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		NBTTagCompound tags = stack.getTagCompound();
		if (!tags.hasKey("Components")) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		NBTTagList components = tags.getTagList("Components", Constants.NBT.TAG_COMPOUND);
		for (NBTBase comp : components) {
			NBTTagCompound compound = (NBTTagCompound) comp;
			if (compound.hasKey("id")) {
				ItemStack newStack = new ItemStack(compound);
				TotemEssentials.giveItems(newStack, playerIn);
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
	}

	@Override
	public void registerOreDict() {
		OreDictionary.registerOre("itemExchangePacket", this);
	}

}
