# ![Totem Shell](./images/totem_shell.png) _Totem Essentials_ ![Totem Shell](./images/totem_shell.png)
### For Minecraft 1.12.1 and 1.12.2
#### For contact information, please see [this file](https://github.com/IceMetalPunk/TotemEssentials/blob/master/ContactInfo.md).
Totem Essentials is a Minecraft mod that expands on vanilla's "Totem of Undying" with an entire totem crafting system. You reap mobs for their Essence, then use those to craft different types of Totems with different properties.

All recipes are unlocked in your recipe book when you obtain your first Totem Shell. There's also JEI integration, so I suggest you use JEI to get info about mob drops in the game :)

## Installation
When using the Forge client for 1.12.1 or 1.12.2, simply download [the mod from here](https://github.com/IceMetalPunk/TotemEssentials/raw/master/lib/totemessentials-1.1.0.jar) and put it in your .minecraft/mods folder. That's all :)

As mentioned, I also recommend using JEI as well; you can find the downloads for that mod [here](https://minecraft.curseforge.com/projects/just-enough-items-jei/files). JEI is not necessary to play with Totem Essentials, but it's very useful!

## Configuration
As of version 1.1.0, Totem Essentials has a few configuration options. You can change the default durability of every totem which has durability. You can also enable or disable any totem recipes, and the essence for any disabled totems won't drop anymore, either. **(Note: Changing a recipe configuration will require you to restart Minecraft before the changes take effect.)**

## Videos
[Official Walkthrough](https://youtu.be/j3OsvvUPfrM)

## Totem Shell
Instead of dropping a Totem of Undying, the Woodland Mansion's Evoker ("Evocation Illager") will now drop a Totem Shell. This is the blank slate, the basis of Totem Essentials, the crafting ingredient you will combine with various Essences to craft your new Totems.

## Essences and Totems
### Reaper Essence / Totem of Reaping
Vindicators ("Vindication Illagers") will naturally drop Reaper Essence 40% of the time. All other Essences must be "reaped"; that is, they must be obtained by killing certain mobs with a Totem of Reaping in your inventory.

![Totem of Reaping recipe](./images/totem_crafting.png)

A Totem of Reaping is crafted by surrounding a Totem Shell with 8 Reaper Essence. (This is the basic crafting pattern for all totems, by the way.) It has 8 durability; whenever you kill a mob that can drop Essence with a Totem of Reaping in your inventory, you've "reaped" that mob. It will drop its Essence and the Totem will take 1 damage. This means each Totem of Reaping lets you reap up to 8 mobs (although it can be enchanted with Unbreaking and Mending to extend its use).

Reaped mobs will drop 1 Essence, plus 1 extra Essence for each level of Looting your weapon has. No matter your level of looting or the number of Essence dropped, the Totem will always take only 1 damage per mob reaped.

### Vexatious Essence / Totem of Phasing
Vexatious Essence is reaped from, you guessed it, Vexes. If you craft a Totem Shell surrounded by 8 Vexatious Essence, you'll get a Totem of Phasing.

As long as you're holding a Totem of Phasing _in your main hand_, you'll be unaffected by gravity and able to move through solid blocks. You also won't be able to jump, though. Each tick (1/20 of a second) you're inside a solid block, the Totem of Phasing will take 1 damage. Of course, if it breaks while you're inside an opaque block, you'll immediately start suffocating, so be careful!

### Lactic Essence / Totem of Curing
If you milk a cow, you get milk. If you reap a cow, you get Lactic Essence. Craft 8 Lactic Essence around a Totem Shell to make a Totem of Curing.

If you would receive a negative potion effect with a Totem of Curing in your inventory, it'll be cured immediately. Doing so does damage the Totem, though, at a rate of 1 damage per effect cleared per level. So removing a Wither I effect would cost 1 damage, but removing Poison II would cost 2 damage; and removing them both would cost a total of 3 damage.

The Totem of Curing has 10 durability, by the way. That's probably important to know.

### Featherfoot Essence / Totem of Featherfoot
Chickens float, so why can't you? What if I told you that you could?

Then I'd be lying. You can't float, but if you reap enough chickens for 8 Featherfoot Essence, you can craft up a Totem of Featherfoot. As long as this is in your inventory, it'll absorb all fall damage you would otherwise take, until it breaks.

It's like Feather Fallling on steroids, if it took up an extra inventory slot...

### Vampiric Essence / Totem of Vampirism
Bats. They are completely useless, right? Nope! Reap them for their Vampiric Essence, which you can use to craft a Totem of Vampirism.

Whenever you deal damage to a mob while you have a Totem of Vampirism in your inventory, you'll heal equal to the amount of damage you dealt. Healing this way deals the same amount of damage, rounded up, to the totem.

### Traveling Essence / Totem of Traveling
Ender pearls are great, but they can only take you so far. Skip to the next level of teleportation by reaping enough Endermen of their Traveling Essence to make a Totem of Traveling!

Sneak + Use the Totem of Traveling to bind it to your current coordinates. After that, you can simply use it normally to teleport back to that spot from anywhere in the world. Note that you can't teleport cross-dimensionally; but you can see what coordinates and dimension the Totem is bound to in its mouse-over tooltip.

It takes 1 damage per use, plus 1 damage for every 10 blocks you travel. If it doesn't have enough durability left to make the full trip, you will teleport somewhere between your current location and your destination... better hope it's not in lava!

### Replication Essence / Totem of Replication
Now that Illusioners spawn naturally in Woodland Mansions (what's that? YES, THEY DO!), maybe you can reap them for some of their Replication Essence to learn the magicians' secrets!

Hit any mob with a Totem of Replication to bind it to that mob type. Then you can use the Totem on a block to spawn the mob there. It takes 1 damage for every mob spawned, and starts with 50 durability.

### Exchange Essence / Totem of Exchange

Killing villagers is bad for trading. But how else will you reap their wonderful Exchange Essence?

A Totem of Exchange can be put in the left slot of an anvil to "decraft" any item in the right slot. That is, it'll give you an Exchange Packet which can be used to obtain all the crafting ingredients of the item you've decrafted. The opposite of trading a villager supplies for an item, but ideal if you're a DIY kind of person.

### Fireglaze Essence / Totem of Fireglaze

Potions of Fire Resistance are great, but if you do happen to die around lava, you can kiss your items goodbye. But what if the fireproofing abilities of magma cream could be harnessed for your items, too?

You can reap Magma Cubes for their Fireglaze Essence, which can be turned into a Totem of Fireglaze. If a Totem of Fireglaze drops from your inventory upon your death, all the items you've dropped will become fireproof until they are collected. You only get two uses out of this Totem, though, so use them wisely!

(Oh, yes, and if you have multiple Totems of Fireglaze drop from your corpse, only one will take damage at a time, so thank magma for that!)

### Storage Essence / Totem of Storage

Shulker shells are super useful for blocks of storage, but inventory management can still be a bit tedious sometimes. Enter Storage Essence (reaped from Shulkers, obviously).

Using a Totem of Storage swaps your bottom inventory row with your hotbar and vice-versa. If your Totem is in your hotbar, that slot won't be swapped, but all the rest will; if you're using the Totem from your offhand, then all your hotbar slots will be swapped. Useful!

And unlike some other Totems I could mention (and have!), the Totem of Storage has no durability, so you can use one forever! If you don't lose it, that is.

### Aim Essence / Totem of Aim
Skeletons are always shooting at you with bows... maybe if you reap their Essence, you can use it to make a Totem that will help you aim?

Indeed, you can! A Totem of Aiming makes all your arrows fly straight by removing their gravity. It takes 1 damage per arrow enhanced.

### Aggro Essence / Totem of Aggression
Zombie Pigmen are known for getting extremely aggressive and attacking you for the smallest slight. But if you reap them of their Essence, you can create a Totem of Aggression!

The Totem of Aggression causes all mobs who target you within a 20 block radius to instead target another mob nearby. If there's only one mob, though, it'll still come after you... The Totem takes 1 damage for each mob it "turns".

### Wisdom Essence / Totem of Wisdom
Elder Guardians are elders, and we should respect our elders because they're wise. Or... we can reap the Elder Guardians of their Wisdom Essence. Up to you.

Wisdom Essence can craft a Totem of Wisdom. With one of these in your inventory, all XP orbs you collect are doubled in value. The Totem takes 1 damage per extra XP you earn. Mmmm, experience.

### Gluttony Essence / Totem of Gluttony
Husks are always getting so hungry walking around in the desert. Which means their essence might be delicious! Er, I mean, it might help you manage your hunger... yeah, that... So reap it!

Gluttony Essence can be used to craft a Totem of Gluttony. Keep one in your inventory with some food, and if your hunger drops below 2 haunches, it'll automatically eat the highest-saturation food for you. It takes more damage eating better foods.

### Flamebody Essence / Totem of Flamebody
Blazes, being fire elementals, are kind of hot. Literally, their bodies are on fire. And if you reap their essence, yours could be, too!

When a mob or player damages you while you have a Totem of Flamebody in your inventory, the Totem will take damage, and your attacker will catch on fire for 7 seconds. If they keep hitting you, they keep burning! It's like thorns, but with more charring.

### Daunting Essence / Totem of Daunting
Just hearing a creeper's hiss can make the bravest warrior turn and run. Therefore, essence reaped from a creeper should have the same effect, right?

Using a Totem of Reaping will push away all mobs in range, using a bit of durability for each mob it pushes. This includes other players, too! Great for getting out of a crowded pinch.

## Progression Strategy
Hey, don't let me tell you how to play the game. It's your game, you have fun with it your own way!

But if you want to know how I, as the mod author, intended the progression to go, here's how:

Step 1: Find a Woodland Mansion, collect as much Reaping Essence as you can from Vindicators (preferably with Looting), and then kill an Evoker for your first Totem Shell.

Step 2: Craft your first Totem Shell into a Totem of Reaping, then reap as many Illusioners as you can.

Step 3: Kill a second Evoker, and use its Totem Shell to make a Totem of Replication.

Step 4: Smack an Evoker with the Totem of Replication. Now you can spawn in tons of Evokers to get tons of Totem Shells and keep your Totem Essentials journey going!

Suggestion: You may want to replicate some Vindicators, too, so you don't lose your Totem of Reaping when it breaks :)

## Advanced Totemancy
Did you know that creating these Totems from Essence is a form of magic called Totemancy? And like all good types of magic, Totemancy has a scientific hierarchy of progress. That's a fancy way of saying, "there are Tier II Totems".

### Ensouled Essence

![Ensouled Essence crafting](./images/ensouled_essence_crafting.png)

Craft 4 of any Essence with 5 Soul Sand (make an X with the Soul Sand and fill the side gaps with Essence) to craft Ensouled Essence. Use 8 Ensouled Essence in a Totem's recipe instead of normal Essence, and you'll make the Ensouled Totem of its type.

![Ensouled Totem upgrading](./images/ensouled_upgrading.png)

You can also upgrade an existing Totem by placing it in the middle of your crafting grid, filling the top and bottom rows with the appropriate Ensouled Essence, and filling the left and right of the Totem with Reinforced Soul Sand.

### Reinforced Soul Sand

![Reinforced Soul Sand crafting](./images/reinforced_soul_sand_crafting.png)

Oh, "what's reinforced soul sand?" you ask? Simple! Just craft 5 soul sand in an X shape and you'll get one piece of Reinforced Soul Sand. It looks similar to normal soul sand, but it has some special properties: it doesn't slow you down, it fills a full block space, and one second after you walk on it, it falls like sand! Placing blocks near it won't make it fall; something has to walk on it.

More importantly for a Totemancer like you, reinforced soul sand can be used to Ensoul an already-crafted totem rather than starting from scratch with a new Totem Shell :)

### Ensouled Totems

Ensouled Totems have more durability, and they each provide upgraded abilities compared to their Tier I counterparts.

#### Ensouled Totem of Phasing
Like its non-ensouled counterpart, the Ensouled Totem of Phasing lets you phase through walls and be unaffected by gravity. Unlike the normal version, the Ensouled version of this Totem has twice as much durability and lets you fly as though you were in Creative Mode! (That is, your Jump Key = move up, your Sneak Key = move down).

#### Ensouled Totem of Traveling
Remember how the normal Totem of Traveling can't teleport you between dimensions? Well, the Ensouled version can! It takes 10 extra damage when traveling across dimensions, but it has twice as much durability anyway. Zoom zoom.

#### Ensouled Totem of Replication
Tired of your replicated mobs never turning out quite how you wanted them? Like, you wanted a crowd of blue sheep, but they keep coming out different colors? Look no further than the Ensouled Totem of Replication!
An Ensouled Totem of Replication works like a regular one, except it copies all the NBT tags of the bound entity to the new one. Perfect copies each time! Plus, it can spawn 75 copies instead of the normal Totem's 50.

#### Ensouled Totem of Vampirism
Sucking blood is great for a vampire. You know what's not great? The sun. After healing twice as much as a normal Totem of Vampirism, the ensouled version will inflict your victim with the Sunburn effect, causing them to burn up in the sun. Like a zombie, with no hope for helmet protection.

#### Ensouled Totem of Storage
The normal Totem of Storage lets you swap between your hotbar and the bottom row of your inventory. LAME! The ensouled version allows you to cycle through all the rows of your inventory, including your hotbar, whenever you want. Super useful.

At least, it's lame compared to the Ensouled Totem of Storage, which lets you cycle among all your inventory rows. It still won't cycle the column your Totem is in, though, so you can keep using it; try using it from your offhand to cycle the entire inventory! :)

#### Ensouled Totem of Aim
Even with a straight shot from a Totem of Aim, it can be hard to see your target and hit it from far enough away. So the Ensouled Totem of Aim gives the Glowing effect to any mobs you look at while pulling back your bow. Within 100 blocks.

#### Ensouled Totem of Featherfoot
Taking no fall damage is good. But taking no fall damage *and* floating to the ground like a chicken? Even better!

#### Ensouled Totem of Exchange
DIY just got cheaper. This allows you to decraft more items than the normal Totem of Exchange, and it costs half as much XP per item.

#### Ensouled Totem of Fireglaze
Pour some magma (cream) on me! This Totem saves your items from burning when you die, *and* absorbs all fire damage you would take!

#### Ensouled Totem of Curing
Keep this in your inventory, and it'll cure you of negative potion effects *and* double the duration of your positive potion effects. Plus, it has 30 durability and only takes 1 damage per effect per level. It's like the vitamins to your medicine.

#### Ensouled Totem of Aggression

With the normal Totem of Aggression, mobs will attack each other, but there will be one winner. And that winner? It'll be mad at you. Enter the Ensouled Totem of Aggression!

In addition to the anger-inducing effects of the normal Totem of Aggression, if there's only one mob around, the Ensouled Totem of Aggression will calm it down so it won't attack you. Keeping it calm takes a lot of durability, though, so don't hang around too long!

#### Ensouled Totem of Wisdom
What's the point in doubling the amount of XP you earn if you lose it all when you die? So don't lose it! Keep an Ensouled Totem of Wisdom on you, and in addition to double the XP, you'll also keep all your experience when you die! (For a cost of 500 durability...)

#### Ensouled Totem of Reaping
If you're going to kill someone and steal bits of their soul, you may as well take their heads home as trophies, right?  At least, with this Totem in your inventory, there's a 33% chance of a head dropping from any of the vanilla mobs that have heads. (Wither Skeletons, Creepers, Skeletons, Zombies, and the Ender Dragon.)

Note: Looting doesn't increase the chances of these extra head drops. Sorry-not-sorry.

#### Ensouled Totem of Gluttony
A Totem of Gluttony helps with food-based micromanagement, but if you never want to think about food again, try the ensouled verion! No need for food items, it'll top up your hunger when you're low on its own!

#### Ensouled Totem of Undying
In addition to the normal Totem of Undying saving effects, having the ensouled version anywhere in your inventory also gives you invisibility, gives you fire resistance if fire killed you, and teleports you to your spawn point if you fell into the void. It also has 10 uses.

#### Ensouled Totem of Flamebody
In case 7 seconds of burning isn't enough revenge for you, having the Ensouled Totem of Undying in your inventory will set any mobs or players you touch on fire for 5 seconds, if they can burn. If anyone attacks you, they'll be set on fire longer the more damage they dealt. Ultimate Burning Revenge (TM).

#### Ensouled Totem of Daunting
Creepers aren't just scary; they also explode. Duh. If you sneak + use an Ensouled Totem of Daunting, it will create an explosion that cleanly clears out a 5x5x5 cube around it but deals no damage to any living things. Cleaner than TNT, safer than creepers!