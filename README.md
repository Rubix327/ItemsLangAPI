# ItemsLangAPI
[![](https://jitpack.io/v/Rubix327/ItemsLangAPI.svg)](https://jitpack.io/#Rubix327/ItemsLangAPI)

This API will help you to translate almost every game object (items, enchantments, effects, ... see below) to any language you like.

## Use cases
- You want to send player a message with custom item name depending on their language;
- You want to show player a GUI with language-based namings (e.g., items translated to their own language);
- You want to use localized item/enchantments/etc names anywhere else.

## Features (Methods)
- `translate` - Translating a java object (ItemStack, Enchantment, Biome, ect., see full list at *categories*);
- `translateExact` - Translating an exact defined namespaced key (e.g., "item.minecraft.diamond", Lang.FR_FR will return "Diamant");
- `translateAnything` - Translating anything without knowing a namespace (e.g., "fox", Lang.ES_ES will return Zorro);
- TODO: Custom language files (can be added by user);
- TODO: Add more categories.

## Categories

There are 8 categories of objects you can use now:
- ItemStack (returns an item name);
- Material (same as ItemStack);
- PotionEffectType (returns a potion effect name);
- EntityType (returns an entity name - Arrow, Zombie, Player);
- Biome (returns a biome name);
- Villager.Profession (returns a villager profession);
- Enchantment (returns an enchantment name);
- Statistic (returns a player-server statistic name).

### Unsupported categories

Since we try to provide the ability to work on rather old game versions (1.8+), 
it is not possible to add some categories to the API. However, you can add them
yourself by changing this API for your specific version.
- Villager.Type (added in MC 1.14)
- TropicalFish.Pattern (added in MC 1.13)

## How to compile

**Maven:**<br>
Please use the latest release version instead of `1.0.0`
```maven
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Rubix327</groupId>
        <artifactId>Foundation</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

**Gradle:**<br>
Please use the latest release version instead of `1.0.0`
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    compileOnly "com.github.Rubix327:ItemsLangAPI:1.0.0"
}
```

## Implementing & How To Use
Firstly, get the API and load your preferred languages.
```
ItemsLangAPI api = ItemsLangAPI.getApi();

@Override
public void onEnable() {
    api.load(Lang.EN_US, Lang.RU_RU, Lang.CA_ES);
}
```
You can also load all languages at once. 
But if you don't need them all, I'd recommend sticking with the method above
for the sake of not littering your memory with the useless data.
```
ItemsLangAPI.getApi().loadAll();
```
If you don't load a language and then try to use it, you will get `null`.

**Using _translate_ method:**<br>
Specify the object you want to be translated and then the required language (Lang).
```
ItemsLangAPI.getApi().translate(new ItemStack(Material.BIRCH_LOG), Lang.CA_ES);
```
You can also use `Player.getLocale()` starting from MC version 1.12.
```
ItemsLangAPI.getApi().translate(Material.DIAMOND_BLOCK, event.getPlayer().getLocale());
```
You can use String as a language parameter. The API will try to automatically convert it to a Lang,
but if this Lang does not exist, default will be used.
```
ItemsLangAPI.getApi().translate(Enchantment.ARROW_FIRE, "fr_fr");
```

**Additional features**<br>
You can disable the warning notification (it is displayed when something is trying to
translate an object to a non-existent language) using the following:
```
ItemsLangAPI.getApi().hideWarnings();
```
You can set the default language using the following:
```
ItemsLangAPI.getApi().setDefault(Lang.RU_RU);
```
The default language is loaded regardless of using `api.load()`. By default, it is _EN_US_.

## Using on legacy versions (below 1.13)
ItemsLangAPI will automatically try to convert your item's numerical id to the modern one.
But if you still need to get the modern name from the item running your server on
a legacy version, you can use:
```
Comp.getModernId(ItemStack item)
```
If your server version is 1.13+, this method will always return `null` because it is not
intended to work on the modern versions. If you want to get a modern name from
the legacy id, use:
```
ItemsLangAPI.getApi().getLegacyToModern().get("35-1"); // -> orange_wool
```