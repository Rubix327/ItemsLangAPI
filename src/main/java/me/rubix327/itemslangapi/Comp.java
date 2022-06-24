package me.rubix327.itemslangapi;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Comp {

    private static final boolean isLegacy = defineIsLegacy();

    protected enum PotionEffectType {
        SPEED("speed"),

        /**
         * Decreases movement speed.
         */
        SLOW("slowness"),

        /**
         * Increases dig speed.
         */
        FAST_DIGGING("haste"),

        /**
         * Decreases dig speed.
         */
        SLOW_DIGGING("mining_fatigue"),

        /**
         * Increases damage dealt.
         */
        INCREASE_DAMAGE("strength"),

        /**
         * Heals an entity.
         */
        HEAL("instant_health"),

        /**
         * Hurts an entity.
         */
        HARM("instant_damage"),

        /**
         * Increases jump height.
         */
        JUMP("jump_boost"),

        /**
         * Warps vision on the client.
         */
        CONFUSION("nausea"),

        /**
         * Regenerates health.
         */
        REGENERATION("regeneration"),

        /**
         * Decreases damage dealt to an entity.
         */
        DAMAGE_RESISTANCE("resistance"),

        /**
         * Stops fire damage.
         */
        FIRE_RESISTANCE("fire_resistance"),

        /**
         * Allows breathing underwater.
         */
        WATER_BREATHING("water_breathing"),

        /**
         * Grants invisibility.
         */
        INVISIBILITY("invisibility"),

        /**
         * Blinds an entity.
         */
        BLINDNESS("blindness"),

        /**
         * Allows an entity to see in the dark.
         */
        NIGHT_VISION("night_vision"),

        /**
         * Increases hunger.
         */
        HUNGER("hunger"),

        /**
         * Decreases damage dealt by an entity.
         */
        WEAKNESS("weakness"),

        /**
         * Deals damage to an entity over time.
         */
        POISON("poison"),

        /**
         * Deals damage to an entity over time and gives the health to the
         * shooter.
         */
        WITHER("wither"),

        /**
         * Increases the maximum health of an entity.
         */
        HEALTH_BOOST("health_boost"),

        /**
         * Increases the maximum health of an entity with health that cannot be
         * regenerated, but is refilled every 30 seconds.
         */
        ABSORPTION("absorption"),

        /**
         * Increases the food level of an entity each tick.
         */
        SATURATION("saturation"),

        /**
         * Outlines the entity so that it can be seen from afar.
         */
        GLOWING("glowing"),

        /**
         * Causes the entity to float into the air.
         */
        LEVITATION("levitation"),

        /**
         * Loot table luck.
         */
        LUCK("luck"),

        /**
         * Loot table unluck.
         */
        UNLUCK("unluck"),

        /**
         * Slows entity fall rate.
         */
        SLOW_FALLING("slow_falling"),

        /**
         * Effects granted by a nearby conduit. Includes enhanced underwater abilities.
         */
        CONDUIT_POWER("conduit_power"),

        /**
         * Increses underwater movement speed.<br>
         * Squee'ek uh'k kk'kkkk squeek eee'eek.
         */
        DOLPHINS_GRACE("dolphins_grace"),

        /**
         * Triggers a raid when the player enters a village.<br>
         * oof.
         */
        BAD_OMEN("bad_omen"),

        /**
         * Reduces the cost of villager trades.<br>
         * \o/.
         */
        HERO_OF_THE_VILLAGE("hero_of_the_village"),

        /**
         * Causes the player's vision to dim occasionally.
         */
        DARKNESS("darkness");

        private final String key;
        PotionEffectType(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }

    protected enum Enchantment{
        PROTECTION_ENVIRONMENTAL("protection"),

        /**
         * Provides protection against fire damage
         */
        PROTECTION_FIRE("fire_protection"),

        /**
         * Provides protection against fall damage
         */
        PROTECTION_FALL("feather_falling"),

        /**
         * Provides protection against explosive damage
         */
        PROTECTION_EXPLOSIONS("blast_protection"),

        /**
         * Provides protection against projectile damage
         */
        PROTECTION_PROJECTILE("projectile_protection"),

        /**
         * Decreases the rate of air loss whilst underwater
         */
        OXYGEN("respiration"),

        /**
         * Increases the speed at which a player may mine underwater
         */
        WATER_WORKER("aqua_affinity"),

        /**
         * Damages the attacker
         */
        THORNS("thorns"),

        /**
         * Increases walking speed while in water
         */
        DEPTH_STRIDER("depth_strider"),

        /**
         * Freezes any still water adjacent to ice / frost which player is walking on
         */
        FROST_WALKER("frost_walker"),

        /**
         * Item cannot be removed
         */
        BINDING_CURSE("binding_curse"),

        /**
         * Increases damage against all targets
         */
        DAMAGE_ALL("sharpness"),

        /**
         * Increases damage against undead targets
         */
        DAMAGE_UNDEAD("smite"),

        /**
         * Increases damage against arthropod targets
         */
        DAMAGE_ARTHROPODS("bane_of_arthropods"),

        /**
         * All damage to other targets will knock them back when hit
         */
        KNOCKBACK("knockback"),

        /**
         * When attacking a target, has a chance to set them on fire
         */
        FIRE_ASPECT("fire_aspect"),

        /**
         * Provides a chance of gaining extra loot when killing monsters
         */
        LOOT_BONUS_MOBS("looting"),

        /**
         * Increases damage against targets when using a sweep attack
         */
        SWEEPING_EDGE("sweeping"),

        /**
         * Increases the rate at which you mine/dig
         */
        DIG_SPEED("efficiency"),

        /**
         * Allows blocks to drop themselves instead of fragments (for example,
         * stone instead of cobblestone)
         */
        SILK_TOUCH("silk_touch"),

        /**
         * Decreases the rate at which a tool looses durability
         */
        DURABILITY("unbreaking"),

        /**
         * Provides a chance of gaining extra loot when destroying blocks
         */
        LOOT_BONUS_BLOCKS("fortune"),

        /**
         * Provides extra damage when shooting arrows from bows
         */
        ARROW_DAMAGE("power"),

        /**
         * Provides a knockback when an entity is hit by an arrow from a bow
         */
        ARROW_KNOCKBACK("punch"),

        /**
         * Sets entities on fire when hit by arrows shot from a bow
         */
        ARROW_FIRE("flame"),

        /**
         * Provides infinite arrows when shooting a bow
         */
        ARROW_INFINITE("infinity"),

        /**
         * Decreases odds of catching worthless junk
         */
        LUCK("luck_of_the_sea"),

        /**
         * Increases rate of fish biting your hook
         */
        LURE("lure"),

        /**
         * Causes a thrown trident to return to the player who threw it
         */
        LOYALTY("loyalty"),

        /**
         * Deals more damage to mobs that live in the ocean
         */
        IMPALING("impaling"),

        /**
         * When it is rainy, launches the player in the direction their trident is thrown
         */
        RIPTIDE("riptide"),

        /**
         * Strikes lightning when a mob is hit with a trident if conditions are
         * stormy
         */
        CHANNELING("channeling"),

        /**
         * Shoot multiple arrows from crossbows
         */
        MULTISHOT("multishot"),

        /**
         * Charges crossbows quickly
         */
        QUICK_CHARGE("quick_charge"),

        /**
         * Crossbow projectiles pierce entities
         */
        PIERCING("piercing"),

        /**
         * Allows mending the item using experience orbs
         */
        MENDING("mending"),

        /**
         * Item disappears instead of dropping
         */
        VANISHING_CURSE("vanishing_curse"),

        /**
         * Walk quicker on soul blocks
         */
        SOUL_SPEED("soul_speed"),

        /**
         * Walk quicker while sneaking
         */
        SWIFT_SNEAK("swift_sneak");

        private final String key;
        Enchantment(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }

    public static boolean isLegacy(){
        return Comp.isLegacy;
    }

    private static boolean defineIsLegacy(){
        List<String> legacy = Arrays.asList(
                "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "1.10", "1.11", "1.12");

        String version = Bukkit.getBukkitVersion().replace("-R0.1-SNAPSHOT", "");
        List<String> currentVersion = Arrays.stream(version.split(":")).collect(Collectors.toList());
        if (currentVersion.size() < 3){
            currentVersion.add("0");
        }

        for (String v : legacy){
            String[] ver = v.split(":");
            if (currentVersion.get(0).equals(ver[0]) && currentVersion.get(1).equals(ver[1])){
                return true;
            }
        }
        return false;
    }

    public static String getModernId(ItemStack item){
        HashMap<String, String> legacyToModern = ItemsLangAPI.getApi().getLegacyToModern();
        String legacyId = (item.getType() + "-" + item.getData().getData()).toLowerCase();

        if (legacyToModern.containsKey(legacyId)){
            return legacyToModern.get(legacyId);
        }
        return item.getType().toString();
    }
}
