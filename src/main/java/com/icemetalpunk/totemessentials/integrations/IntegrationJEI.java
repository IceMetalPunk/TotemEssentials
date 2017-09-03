package com.icemetalpunk.totemessentials.integrations;

import java.util.HashMap;

import com.icemetalpunk.totemessentials.TotemEssentials;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class IntegrationJEI implements IModPlugin {

	protected HashMap<ItemStack, String> vanillaDescriptions = new HashMap<ItemStack, String>();

	public IntegrationJEI() {
		vanillaDescriptions.put(new ItemStack(Items.TOTEM_OF_UNDYING, 1, OreDictionary.WILDCARD_VALUE),
				"item.undying_totem");
	}

	@Override
	public void register(IModRegistry registry) {
		for (String blockName : TotemEssentials.proxy.blocks.registry.keySet()) {
			Block block = TotemEssentials.proxy.blocks.get(blockName);
			registry.addDescription(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE),
					"jei." + block.getUnlocalizedName());
		}
		for (String itemName : TotemEssentials.proxy.items.registry.keySet()) {
			Item item = TotemEssentials.proxy.items.get(itemName);
			registry.addDescription(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE),
					"jei." + item.getUnlocalizedName());
		}
		for (ItemStack stack : vanillaDescriptions.keySet()) {
			registry.addDescription(stack, "jei." + vanillaDescriptions.get(stack));
		}
	}
}
