package com.icemetalpunk.totemessentials.items.totems;

public class ItemFireglazeTotem extends ItemTotemBase {
	public static final int DEATH_USE_DAMAGE = 1;

	public ItemFireglazeTotem(String name) {
		super(name);
		this.setMaxDamage(2); // 2 uses
	}

}
