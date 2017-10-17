package com.icemetalpunk.totemessentials.items.totems.ensouled;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.google.common.primitives.Ints;
import com.icemetalpunk.totemessentials.TotemEssentials;
import com.icemetalpunk.totemessentials.items.totems.ItemCuringTotem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemEnsouledCuringTotem extends ItemEnsouledTotemBase {

	public ItemEnsouledCuringTotem(String name) {
		super(name);
		this.setMaxDamage(30); // 30 uses
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		if (worldIn.isRemote || !(entityIn instanceof EntityLivingBase)) {
			return;
		}
		EntityLivingBase living = (EntityLivingBase) entityIn;
		int cured = ItemCuringTotem.clearNegativeEffects(stack, entityIn);
		int boosted = 0;

		// Get the tracked effects from the totem's tags.
		NBTTagCompound tag;
		if (stack.hasTagCompound()) {
			tag = stack.getTagCompound();
		} else {
			tag = new NBTTagCompound();
		}
		UUID playerID = null;
		if (tag.hasUniqueId("LastPlayer")) {
			playerID = tag.getUniqueId("LastPlayer");
		}
		List<Integer> effectList = new ArrayList<Integer>();
		if (tag.hasKey("EffectsCleared")) {
			effectList = new ArrayList<Integer>(Ints.asList(tag.getIntArray("EffectsCleared")));
		}

		boolean hasChanged = false;

		// Clear effects list if a different person has the totem now.
		if (playerID == null || !playerID.equals(living.getUniqueID())) {
			hasChanged = true;
			effectList.clear();
		}

		// Buff effects that weren't tracked.
		for (PotionEffect effect : living.getActivePotionEffects()) {
			int potionID = Potion.getIdFromPotion(effect.getPotion());
			if (effect.getPotion().isBeneficial() && !effectList.contains(potionID)) {
				++boosted;
				living.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration() * 2,
						effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
				effectList.add(potionID);
				hasChanged = true;
				stack.damageItem(effect.getAmplifier() + 1, living);
			}
		}

		// Remove tracked effects that don't exist anymore.
		Iterator<Integer> it = effectList.iterator();
		while (it.hasNext()) {
			Integer id = it.next();
			PotionEffect effect = living.getActivePotionEffect(Potion.getPotionById(id));
			if (effect == null) {
				it.remove();
				hasChanged = true;
			}
		}

		// Update the tags.
		if (hasChanged) {
			tag.setUniqueId("LastPlayer", living.getUniqueID());
			tag.setIntArray("EffectsCleared", Ints.toArray(effectList));
			stack.setTagCompound(tag);
		}

		if (cured > 0) {
			worldIn.playSound(null, living.getPosition(), TotemEssentials.proxy.sounds.get("sfx_curing"), SoundCategory.AMBIENT, 1.0f, 1.0f);
		}
		if (boosted > 0) {
			worldIn.playSound(null, living.getPosition(), TotemEssentials.proxy.sounds.get("sfx_boost_effect"), SoundCategory.AMBIENT, 1.0f, 1.0f);
		}
		
	}

}
