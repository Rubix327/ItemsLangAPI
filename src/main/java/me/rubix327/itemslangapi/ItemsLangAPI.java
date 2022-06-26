package me.rubix327.itemslangapi;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * When using {@link #translate(Object object, Lang lang)} <b>[Lang!]</b> and its modifications,
 * there are multiple cases to get <i>null</i>:
 * <ul>
 * <li> if the given object in [{@link #translate(Object object, Lang lang)}] is not translatable;</li>
 * <li> if the namespaced key in [{@link #translateExact(String namespacedKey, Lang lang)}] does not exist;</li>
 * <li> if no matches found while parsing the key in [{@link #translateAnything(String key, Lang lang)}].</li>
 * </ul>
 * When using {@link #translate(Object, String)} <b>[String!]</b> and its modifications, if you pass a non-existent
 * language as a [String lang], the {@link #defaultLang} will be used.
 * You can set the default language by {@link #setDefault(Lang)}.
 */
public class ItemsLangAPI {

    private static final ItemsLangAPI instance = new ItemsLangAPI();
    private static final String prefix = "[ItemsLangApi] ";

    /**
     * The warning message sent when trying to translate a message to a non-existent Lang.
     */
    private final String warning = prefix + "Some plugin is trying to translate an object to a non-existent language ($lang). " +
            "Object is now translated into English (en_us).";
    /**
     * Should we hide the {@link #warning}?
     */
    private boolean isHideWarnings = false;
    /**
     * The Lang to which a message will be translated if the first try is failed.
     */
    private Lang defaultLang = Lang.EN_US;
    /**
     * The path to a single language file.
     */
    private final String langPath = "/lang/$file.json";
    /**
     * The map containing all the loaded namespacedKeys and their translations.
     * Format:<br>
     * Lang : {key : translation}<br>
     * EN : {item.minecraft.clock : "Clock"}
     */
    private final HashMap<Lang, HashMap<String, String>> map = new HashMap<>();
    /**
     * The map containing legacy ids and their correspondent modern names.<br>
     * Example: 35:1 -> orange_wool
     */
    private final HashMap<String, String> legacyToModern = new HashMap<>();

    /**
     * Get the plugin API.
     * You must use it in the first place!
     * Please see {@link ItemsLangAPI} before getting started.
     */
    public static ItemsLangAPI getApi(){
        instance.loadOne(instance.langPath.replace("$file", instance.defaultLang.toString().toLowerCase()));
        return instance;
    }

    private ItemsLangAPI(){ }

    /**
     * Load all the 123 languages available in the game.<br>
     * If they all are not necessary, it is recommended using
     * {@link #load(Lang...)} to define what languages do you really need,
     * otherwise your RAM would be littered with unused data.
     */
    public void loadAll(){
        load(Lang.values());
    }

    /**
     * Load the specified languages.
     */
    public void load(Lang... langs) {
        Bukkit.getLogger().info(prefix + "Loading language files...");
        int counter = 0;
        for (Lang lang : langs){
            map.put(lang, loadOne(langPath.replace("$file", lang.toString().toLowerCase())));
            counter++;
        }
        if (Comp.isLegacy()){
            legacyToModern.putAll(loadOne("/legacy.yml"));
        }
        Bukkit.getLogger().info(prefix + "Loaded " +  counter + " language file(s) successfully.");
    }

    /**
     * Load a single file.
     */
    private HashMap<String, String> loadOne(String path) {
        InputStream file = getClass().getResourceAsStream(path);
        YamlConfiguration config = new YamlConfiguration();
        HashMap<String, String> items = new HashMap<>();
        BufferedReader reader;

        if (file == null){
            Bukkit.getLogger().warning(prefix + "File " + path + " does not exist in jar file.");
            return items;
        }

        reader = new BufferedReader(new InputStreamReader(file));

        try {
            config.load(reader);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }

        for (String key : config.getKeys(true)) {
            String value = config.getString(key);

            if (value == null) continue;
            if (value.startsWith("MemorySection")) continue;

            items.put(key, value);
        }

        return items;
    }

    /**
     * See {@link #translate(Object, Lang)}
     */
    public String translate(Object object, String lang){
        return translate(object, getLangFromString(lang));
    }

    /**
     * See {@link #translateExact(String, Lang)}
     */
    public String translateExact(String namespacedKey, String lang){
        return translateExact(namespacedKey, getLangFromString(lang));
    }

    /**
     * See {@link #translateAnything(String, Lang)}
     */
    public String translateAnything(String key, String lang){
        return translateAnything(key, getLangFromString(lang));
    }

    /**
     * Translates the name of the given object to the given language.
     * Only objects belonging to categories defined in {@link Category} are available.
     * @param object the object to translate
     * @param lang the required language
     * @return the translated word or sentence, or null
     */
    @Nullable
    public String translate(Object object, Lang lang){
        String namespacedKey = "none";
        for (Category cat : Category.values()){
            if (cat.getClazz().isInstance(object)){
                namespacedKey = cat.getNamespacedKey(object);
            }
        }
        return translateExact(namespacedKey, lang);
    }

    /**
     * Translates the exact localization key on the given language.
     * Example:<br>
     * translateExact("block.minecraft.stone", Lang.ES) returns "Roca".
     * @param namespacedKey the localization namespace + key
     * @param lang the required language
     * @return translated word or sentence, or null
     */
    @Nullable
    public String translateExact(String namespacedKey, Lang lang){
        return map.get(lang).get(namespacedKey.toLowerCase());
    }

    /**
     * Try to get a translation without knowing a namespace.<br>
     * It will loop through all the available namespaces defined in {@link Category} trying to find the given key.
     * @param key the key
     * @param lang the required language
     * @return translated word or sentence, or null
     */
    @Nullable
    public String translateAnything(String key, Lang lang){
        String translation = null;
        for (String namespace : Category.getAllNamespaces()){
            translation = map.get(lang).get(namespace + key.toLowerCase());
            if (translation != null) return translation;
        }
        return translation;
    }

    /**
     * Get a {@link Lang} from a string.
     * If no language with that name exists, returns the {@link #defaultLang}.
     * @param lang the language to parse
     * @return the Lang object
     */
    private Lang getLangFromString(String lang){
        Lang lang0 = this.defaultLang;
        try{
            lang0 = Lang.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException e){
            if (!this.isHideWarnings){
                Bukkit.getLogger().warning(warning.replace("$lang", lang));
            }
        }
        return lang0;
    }

    /**
     * Hide all warnings, for example {@link #warning}.
     */
    public final void hideWarnings(){
        this.isHideWarnings = true;
    }

    /**
     * Get the default Lang. Initially, it is {@link Lang#EN_US}.
     * @return the language
     */
    public final Lang getDefault(){
        return this.defaultLang;
    }

    /**
     * Set the default Lang. Initially, it is {@link Lang#EN_US}.
     * @param lang the language
     */
    public final void setDefault(Lang lang){
        this.defaultLang = lang;
    }

    /**
     * Get the {@link #map}.
     * @return the map
     */
    public final HashMap<Lang, HashMap<String, String>> getMap(){
        return this.map;
    }

    /**
     * Get the {@link #legacyToModern}
     * @return the map
     */
    public final HashMap<String, String> getLegacyToModern(){
        return this.legacyToModern;
    }

}
