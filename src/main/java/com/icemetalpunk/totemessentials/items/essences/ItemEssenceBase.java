package com.icemetalpunk.totemessentials.items.essences;

import org.apache.commons.lang3.text.WordUtils;

import com.icemetalpunk.totemessentials.IOreDicted;
import com.icemetalpunk.totemessentials.items.ItemRegistry;
import com.icemetalpunk.totemessentials.items.TEItem;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemEssenceBase extends TEItem implements IOreDicted {

	public final String essenceType;
	public boolean isEnsouled = false;

	public ItemEssenceBase(String name, boolean isEnsouled) {
		super(name);
		if (name.startsWith("ensouled_essence_") && isEnsouled) {
			this.essenceType = "ensouled" + WordUtils.capitalize(name.replaceFirst("ensouled_essence_", ""));
		} else if (name.startsWith("essence_")) {
			this.essenceType = name.replaceFirst("essence_", "");
		} else {
			this.essenceType = name;
		}
		this.isEnsouled = isEnsouled;

		// Automatically register an ensouled version of each essence from the
		// normal versions.
		if (!this.isEnsouled) {
			try {
				ItemRegistry.registry.put("ensouled_" + name, this.getClass()
						.getDeclaredConstructor(String.class, boolean.class).newInstance("ensouled_" + name, true));
			} catch (Exception e) {
				System.err.println("Error: could not create ensouled essence of type " + this.essenceType);
				System.err.println("");
				System.err.println("");
				e.printStackTrace();
			}
		}
	}

	public ItemEssenceBase(String name) {
		this(name, false);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return this.isEnsouled || super.hasEffect(stack);
	}

	@Override
	public void registerOreDict() {
		OreDictionary.registerOre("itemEssence", this);
		OreDictionary.registerOre("essence" + WordUtils.capitalize(this.essenceType), this);
	}

}
