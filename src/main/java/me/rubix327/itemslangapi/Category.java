package me.rubix327.itemslangapi;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

/**
 * Categories available for translations.
 */
enum Category {

    /**
     * Items that can be placed on the ground.<br>
     * Examples: Stone, Painting, Banner
     */
    BLOCK("block.minecraft.", ItemStack.class, true),
    /**
     * Items that cannot be placed on the ground.<br>
     * Examples: Emerald, Iron Sword, All Minecarts
     */
    ITEM("item.minecraft.", ItemStack.class, true),
    /**
     * All items materials that can and cannot be placed in the game.
     */
    MATERIAL("item.minecraft.", Material.class, true),
    /**
     * Potion Effects<br>
     * Examples: Speed, Instant Health, Poison.
     */
    POTION_EFFECT_TYPE("effect.minecraft.", PotionEffectType.class, true),
    /**
     * Objects that have custom behaviour in the game (and generally can move)<br>
     * Examples: Arrow, Painting, Splash Potions, All mobs
     */
    ENTITY_TYPE("entity.minecraft.", EntityType.class),
    /**
     * Biomes<br>
     * Examples: Ocean, Plains, Desert, etc.
     */
    BIOME("biome.minecraft.", Biome.class),
    /**
     * Villager professions<br>
     * Examples: None, Armorer, Butcher, Cleric, etc.<br>
     * Can be obtained by ((Villager) event.getEntity()).getProfession().
     */
    VILLAGER_PROFESSION("entity.minecraft.villager.", Villager.Profession.class),
    /**
     * Enchantments<br>
     * Examples: Protection_fire, Thorns, Damage_all
     */
    ENCHANTMENT("enchantment.minecraft.", Enchantment.class, true),
    /**
     * Player statistics<br>
     * Examples: Deaths, Mob_kills, Jump
     */
    STATISTIC("stat.minecraft.", Statistic.class);

    private final String namespace;
    private final Class<?> clazz;
    private final boolean doAdditionalChecks;
    Category(String namespace, Class<?> clazz){
        this(namespace, clazz, false);
    }

    Category(String namespace, Class<?> clazz, boolean doAdditionalChecks){
        this.namespace = namespace;
        this.clazz = clazz;
        this.doAdditionalChecks = doAdditionalChecks;
    }

    public final String getNamespace(){
        return this.namespace;
    }

    public final Class<?> getClazz() {
        return this.clazz;
    }

    public final boolean isDoAdditionalChecks(){
        return this.doAdditionalChecks;
    }

    public static String[] getAllNamespaces() {
        return Arrays.stream(values()).map(Category::getNamespace).toArray(String[]::new);
    }

    public String getNamespacedKey(Object object){
        if (this.isDoAdditionalChecks()){
            if (this.getClazz() == ItemStack.class){
                ItemStack item = (ItemStack) object;
                if (item.getType().isBlock()){
                    if (Comp.isLegacy()){
                        return Category.BLOCK.getNamespace() + Comp.getModernId(item);
                    }
                    return Category.BLOCK.getNamespace() + item.getType();
                }
                else{
                    if (Comp.isLegacy()){
                        return Category.ITEM.getNamespace() + Comp.getModernId(item);
                    }
                    return Category.ITEM.getNamespace() + item.getType();
                }
            }
            else if (this.getClazz() == Material.class){
                Material mat = (Material) object;
                if (mat.isBlock()){
                    return Category.BLOCK.getNamespace() + Comp.getModernId(new ItemStack(mat));
                }
                return Category.ITEM.getNamespace() + Comp.getModernId(new ItemStack(mat));
            }
            else if (this.getClazz() == PotionEffectType.class){
                Comp.PotionEffectType compPet = Comp.PotionEffectType.valueOf(((PotionEffectType) object).getName());
                return Category.POTION_EFFECT_TYPE.getNamespace() + compPet.getKey();
            }
            else if (this.getClazz() == Enchantment.class){
                Comp.Enchantment ench = Comp.Enchantment.valueOf(((Enchantment) object).getName());
                return Category.ENCHANTMENT.getNamespace() + ench.getKey();
            }
        }
        return this.getNamespace() + object.toString();
    }

}
