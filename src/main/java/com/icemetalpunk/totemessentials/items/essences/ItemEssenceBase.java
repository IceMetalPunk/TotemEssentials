package com.icemetalpunk.totemessentials.items.essences;

import org.apache.commons.lang3.text.WordUtils;

import com.icemetalpunk.totemessentials.items.TEItem;

import net.minecraftforge.oredict.OreDictionary;

public class ItemEssenceBase extends TEItem {

	public final String essenceType;

	public ItemEssenceBase(String name) {
		super(name);
		if (name.startsWith("essence_")) {
			this.essenceType = name.replaceFirst("essence_", "");
		} else {
			this.essenceType = name;
		}

		OreDictionary.registerOre("itemEssence", this);
		OreDictionary.registerOre("essence" + WordUtils.capitalizeFully(this.essenceType), this);

	}

}
