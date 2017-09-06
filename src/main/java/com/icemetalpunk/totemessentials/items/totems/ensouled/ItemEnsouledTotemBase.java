package com.icemetalpunk.totemessentials.items.totems.ensouled;

import com.icemetalpunk.totemessentials.items.totems.ItemTotemBase;

import net.minecraft.item.ItemStack;

public class ItemEnsouledTotemBase extends ItemTotemBase {

	public ItemEnsouledTotemBase(String name) {
		super(name);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

}
