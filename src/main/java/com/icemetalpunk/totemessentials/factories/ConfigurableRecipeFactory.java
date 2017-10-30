package com.icemetalpunk.totemessentials.factories;

import java.util.function.BooleanSupplier;

import org.apache.commons.lang3.text.WordUtils;

import com.google.gson.JsonObject;
import com.icemetalpunk.totemessentials.config.TEConfig;

import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class ConfigurableRecipeFactory implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return new BooleanSupplier() {

			@Override
			public boolean getAsBoolean() {
				String name = json.get("name").getAsString();
				name = WordUtils.capitalizeFully(name.replaceAll("_", " "));
				if (TEConfig.recipeList.containsKey(name)) {
					return TEConfig.recipeList.get(name);
				}
				return true;
			}

		};
	}

}
