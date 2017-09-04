package com.icemetalpunk.totemessentials.items.totems;

import org.apache.commons.lang3.text.WordUtils;

import com.icemetalpunk.totemessentials.items.TEItem;

import net.minecraftforge.oredict.OreDictionary;

public class ItemTotemBase extends TEItem {

	public final String totemType;

	public ItemTotemBase(String name) {
		super(name);
		if (name.endsWith("_totem")) {
			this.totemType = name.replaceAll("_totem$", "");
		} else {
			this.totemType = name;
		}

		OreDictionary.registerOre("itemTotem", this);
		OreDictionary.registerOre("totem" + WordUtils.capitalizeFully(this.totemType), this);
	}

}
