package com.icemetalpunk.totemessentials.items.essences;

import org.apache.commons.lang3.text.WordUtils;

import com.icemetalpunk.totemessentials.IOreDicted;
import com.icemetalpunk.totemessentials.items.TEItem;

import net.minecraftforge.oredict.OreDictionary;

public class ItemEssenceBase extends TEItem implements IOreDicted {

	public final String essenceType;

	public ItemEssenceBase(String name) {
		super(name);
		if (name.startsWith("essence_")) {
			this.essenceType = name.replaceFirst("essence_", "");
		} else {
			this.essenceType = name;
		}
	}

	@Override
	public void registerOreDict() {
		OreDictionary.registerOre("itemEssence", this);
		OreDictionary.registerOre("essence" + WordUtils.capitalizeFully(this.essenceType), this);
	}

}
