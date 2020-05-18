package com.scolastico.antimonstercrystal;

import com.scolastico.antimonstercrystal.api.AntiMonsterCrystalAPI;
import com.scolastico.antimonstercrystal.commands.AntiMonsterCrystalCommand;
import com.scolastico.antimonstercrystal.config.ConfigDataStore;
import com.scolastico.antimonstercrystal.config.ConfigHandler;
import com.scolastico.antimonstercrystal.config.CrystalDataStore;
import com.scolastico.antimonstercrystal.events.*;
import com.scolastico.antimonstercrystal.internal.ErrorHandler;
import com.scolastico.antimonstercrystal.internal.Language;
import com.scolastico.antimonstercrystal.internal.Updater;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;

public class AntiMonsterCrystal extends JavaPlugin {

    private static ConsoleCommandSender console;
    private static ConfigDataStore config = new ConfigDataStore();
    private static ConfigHandler configConfigHandler;
    private static CrystalDataStore crystalDataStore = new CrystalDataStore();
    private static ConfigHandler crystalDataStoreConfigHandler;
    private static final String pluginName = "AntiMonsterCrystal";
    private static Plugin plugin;
    private static Long currentTick = 0L;
    private static BukkitTask scheduler = null;
    private static ShapedRecipe recipe = null;
    private static NamespacedKey namespacedKey = null;
    private final Language language = Language.getInstance();
    private static final AntiMonsterCrystalAPI api = new AntiMonsterCrystalAPI();
    private static OnTick onTick;
    private static boolean reloading = false;

    public static void reloadConfigData() {
        reloadConfigData(null);
    }

    public static void reloadConfigData(Runnable runnable) {
        try {
            if (!reloading) {
                reloading = true;
                scheduler.cancel();
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            api.deleteAllCrystals();
                            configConfigHandler.reloadConfigFile();
                            crystalDataStoreConfigHandler.reloadConfigFile();
                            config = (ConfigDataStore) configConfigHandler.getConfigObject();
                            crystalDataStore = (CrystalDataStore) crystalDataStoreConfigHandler.getConfigObject();
                            AntiMonsterCrystal.reloadRecipe();
                            api.spawnCrystalsFromConfig();
                            scheduler = getScheduler();
                            reloading = false;
                            if (runnable != null) runnable.run();
                        } catch (Exception e) {
                            ErrorHandler.getInstance().handleFatal(e);
                        }
                    }
                }, config.getCheckAllTicks()*2);
            }
        } catch (Exception e) {
            ErrorHandler.getInstance().handleFatal(e);
        }
    }

    public static void reloadRecipe() {
        AntiMonsterCrystalAPI api = new AntiMonsterCrystalAPI();
        try {
            if (namespacedKey == null) {
                namespacedKey = new NamespacedKey(plugin, "AntiMonsterCrystal");
            }

            if (recipe != null) {
                Bukkit.getServer().removeRecipe(namespacedKey);
            }

            recipe = new ShapedRecipe(namespacedKey, api.getItemStack());

            Material a = Material.getMaterial(config.getCraftingRecipe().getTopLeft().toUpperCase());
            Material b = Material.getMaterial(config.getCraftingRecipe().getTopMiddle().toUpperCase());
            Material c = Material.getMaterial(config.getCraftingRecipe().getTopRight().toUpperCase());
            Material d = Material.getMaterial(config.getCraftingRecipe().getMiddleLeft().toUpperCase());
            Material e = Material.getMaterial(config.getCraftingRecipe().getMiddleMiddle().toUpperCase());
            Material f = Material.getMaterial(config.getCraftingRecipe().getMiddleRight().toUpperCase());
            Material g = Material.getMaterial(config.getCraftingRecipe().getDownLeft().toUpperCase());
            Material h = Material.getMaterial(config.getCraftingRecipe().getDownMiddle().toUpperCase());
            Material i = Material.getMaterial(config.getCraftingRecipe().getDownRight().toUpperCase());

            if (!config.getCraftingRecipe().getTopLeft().equals("") && a == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getTopLeft() + "'", console);
            if (!config.getCraftingRecipe().getTopMiddle().equals("") && b == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getTopMiddle() + "'", console);
            if (!config.getCraftingRecipe().getTopRight().equals("") && c == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getTopRight() + "'", console);
            if (!config.getCraftingRecipe().getMiddleLeft().equals("") && d == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getMiddleLeft() + "'", console);
            if (!config.getCraftingRecipe().getMiddleMiddle().equals("") && e == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getMiddleMiddle()+ "'", console);
            if (!config.getCraftingRecipe().getMiddleRight().equals("") && f == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getMiddleRight() + "'", console);
            if (!config.getCraftingRecipe().getDownLeft().equals("") && g == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getDownLeft() + "'", console);
            if (!config.getCraftingRecipe().getDownMiddle().equals("") && h == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getDownMiddle() + "'", console);
            if (!config.getCraftingRecipe().getDownRight().equals("") && i == null) Language.getInstance().sendColorMessage(Language.RED + "Recipe Error! Item not found: '" + config.getCraftingRecipe().getDownRight() + "'", console);

            recipe.shape(
                    (a != null ? "A" : " ") + (b != null ? "B" : " ") + (c != null ? "C" : " "),
                    (d != null ? "D" : " ") + (e != null ? "E" : " ") + (f != null ? "F" : " "),
                    (g != null ? "G" : " ") + (h != null ? "H" : " ") + (i != null ? "I" : " ")
            );

            if (a != null) recipe.setIngredient('A', a);
            if (b != null) recipe.setIngredient('B', b);
            if (c != null) recipe.setIngredient('C', c);
            if (d != null) recipe.setIngredient('D', d);
            if (e != null) recipe.setIngredient('E', e);
            if (f != null) recipe.setIngredient('F', f);
            if (g != null) recipe.setIngredient('G', g);
            if (h != null) recipe.setIngredient('H', h);
            if (i != null) recipe.setIngredient('I', i);

            Bukkit.getServer().addRecipe(recipe);
        } catch (Exception e) {
            ErrorHandler.getInstance().handleFatal(e);
        }
    }

    @Override
    public void onEnable() {
        try {

            // Set Vars
            ErrorHandler.getInstance().setName(pluginName);
            console = Bukkit.getServer().getConsoleSender();
            plugin = Bukkit.getPluginManager().getPlugin(AntiMonsterCrystal.pluginName);
            if (plugin == null) ErrorHandler.getInstance().handleFatal(new Exception("Can not get plugin while init."));

            // Install s.Tools
            installSTools();

            // Load Config
            language.setPrefix(Language.DARK_CYAN + "[" + Language.CYAN + pluginName + Language.DARK_CYAN + "]");
            language.sendColorMessage(Language.DARK_GREEN + "Loading Config...", console);
            File pluginFolder = new File("plugins/" + pluginName + "/");
            pluginFolder.mkdirs();
            configConfigHandler = new ConfigHandler(config, "plugins/" + pluginName + "/config.json");
            config = (ConfigDataStore) configConfigHandler.getConfigObject();
            crystalDataStoreConfigHandler = new ConfigHandler(crystalDataStore, "plugins/" + pluginName + "/data.json");
            crystalDataStore = (CrystalDataStore) crystalDataStoreConfigHandler.getConfigObject();
            HashMap<String, String> languageData = config.getLanguage();
            if (languageData.containsKey("prefix")) language.setPrefix(languageData.get("prefix"));
            language.setStrings(languageData);
            language.sendColorMessage(Language.DARK_GREEN + "Config " + Language.GREEN + Language.BOLD + "[OK]", console);

            // Run Updater
            Updater updater = new Updater();
            updater.update(configConfigHandler.isFresh());

            // Spawn Crystals
            api.deleteAllCrystalsFromLocations();
            api.spawnCrystalsFromConfig();

            // Register Events
            PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvents(new OnCraft(), this);
            pluginManager.registerEvents(new OnPlace(), this);
            pluginManager.registerEvents(new OnExplode(), this);
            pluginManager.registerEvents(new OnDamage(), this);

            // Register Scheduler
            onTick = new OnTick();
            scheduler = getScheduler();

            // Register Commands
            PluginCommand command = this.getCommand("AntiMonsterCrystal");
            if (command != null) {
                AntiMonsterCrystalCommand cmd = new AntiMonsterCrystalCommand();
                command.setExecutor(cmd);
                command.setTabCompleter(cmd);
            } else {
                ErrorHandler.getInstance().handleFatal(new Exception("Can not get command 'ChangeLog' while init."));
            }

            reloadRecipe();
        } catch (Exception e) {
            ErrorHandler.getInstance().handleFatal(e);
        }
    }

    @Override
    public void onDisable() {
        if (scheduler != null) scheduler.cancel();
        api.deleteAllCrystals();
        Language.getInstance().sendColorMessage(Language.CYAN + "AntiMonsterCrystal disabled!", console);
    }

    private static BukkitTask getScheduler() {
        return Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    if (currentTick >= config.getCheckAllTicks()) {
                        currentTick = 0L;
                        onTick.onTick();
                    }
                    currentTick++;
                } catch (Exception e) {
                    ErrorHandler.getInstance().handleFatal(e);
                }
            }
        }, 1, 1);
    }

    public static boolean isReloading() {
        return reloading;
    }

    public static ShapedRecipe getRecipe() {
        return recipe;
    }

    public Language getLanguage() {
        return language;
    }

    public static String getPluginName() {
        return pluginName;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static ConfigDataStore getConfigDataStore() {
        return config;
    }

    public static void setConfigDataStore(ConfigDataStore config) {
        AntiMonsterCrystal.config = config;
    }

    public static ConfigHandler getConfigConfigHandler() {
        return configConfigHandler;
    }

    public static CrystalDataStore getCrystalDataStore() {
        return crystalDataStore;
    }

    public static void setCrystalDataStore(CrystalDataStore crystalDataStore) {
        AntiMonsterCrystal.crystalDataStore = crystalDataStore;
    }

    public static ConfigHandler getCrystalDataStoreConfigHandler() {
        return crystalDataStoreConfigHandler;
    }

    public static void setCrystalDataStoreConfigHandler(ConfigHandler crystalDataStoreConfigHandler) {
        AntiMonsterCrystal.crystalDataStoreConfigHandler = crystalDataStoreConfigHandler;
    }

    private static void installSTools() {
        Plugin sTools = Bukkit.getServer().getPluginManager().getPlugin("sTools");
        if (sTools == null) {
            Language.getInstance().sendColorMessage(Language.RED + "The library plugin 's.Tools' is missing!", console);
            Language.getInstance().sendColorMessage(Language.RED + "Installing the plugin automatically... Please wait...", console);
            try {
                URL url = new URL("https://github.com/scolastico/Spigot-s.Tools/releases/latest/download/sTools.jar");
                ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream("plugins/sTools.jar");
                FileChannel fileChannel = fileOutputStream.getChannel();
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                fileChannel.close();
                fileOutputStream.close();
                readableByteChannel.close();
                File file = new File("plugins/sTools.jar");
                if (!file.exists()) throw new Exception();
                Bukkit.getServer().getPluginManager().loadPlugin(file);
                sTools = Bukkit.getServer().getPluginManager().getPlugin("sTools");
                if (sTools == null) throw new Exception();
                Bukkit.getServer().getPluginManager().enablePlugin(sTools);
                Language.getInstance().sendColorMessage(Language.GREEN + "Successfully installed tool library s.Tools!", console);
            } catch (Exception ignored) {
                Language.getInstance().sendColorMessage(Language.RED + "Automatically downloading failed!", console);
                Language.getInstance().sendColorMessage(Language.RED + "Please try to install manually s.Tools!", console);
                Language.getInstance().sendColorMessage(Language.RED + "You can download it from here:", console);
                Language.getInstance().sendColorMessage(Language.RED + "https://github.com/scolastico/Spigot-s.Tools/releases", console);
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
    }

}
