package com.icemetalpunk.totemessentials.items.totems;

public class ItemPhasingTotem extends ItemTotemBase {

	public ItemPhasingTotem(String name) {
		super(name);
		this.setMaxDamage(600); // 30 seconds in a block (600 ticks)
	}

}
