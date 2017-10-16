package com.icemetalpunk.totemessentials.items.totems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemCuringTotem extends ItemTotemBase {

	public ItemCuringTotem(String name) {
		super(name);
		this.setMaxDamage(10); // 10 uses
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (clearNegativeEffects(stack, entityIn) > 0) {
			// TODO: Add curing sound effect
		}
	}

	public static int clearNegativeEffects(ItemStack stack, Entity entity) {
		if (!(entity instanceof EntityLivingBase)) {
			return 0;
		}

		EntityLivingBase ent = (EntityLivingBase) entity;

		int cured = 0;
		Iterator<Entry<Potion, PotionEffect>> itr = ent.getActivePotionMap().entrySet().iterator();
		EntityPlayer player = (EntityPlayer) ent;

		/*
		 * Get bad potion effects. Separated from removal for concurrent
		 * modification reasons
		 */
		PotionEffect effect;
		Entry<Potion, PotionEffect> entry;
		Potion actualPotion;
		List<Entry<Potion, PotionEffect>> toRemove = new ArrayList<>();
		while (itr.hasNext()) {
			entry = itr.next();
			effect = entry.getValue();
			actualPotion = entry.getKey();
			if (actualPotion.isBadEffect()) {
				toRemove.add(entry);
			}
		}

		// Cure the effects
		for (Entry<Potion, PotionEffect> mapEntry : toRemove) {
			actualPotion = mapEntry.getKey();
			effect = mapEntry.getValue();
			player.removePotionEffect(actualPotion);
			++cured;
			int level = effect.getAmplifier();
			stack.damageItem(level + 1, player);
			if (stack.isEmpty()) {
				break;
			}
		}

		return cured;
	}

}
