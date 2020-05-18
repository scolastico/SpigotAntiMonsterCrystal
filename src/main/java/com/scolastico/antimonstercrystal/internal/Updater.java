package com.scolastico.antimonstercrystal.internal;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.config.ConfigDataStore;
import com.scolastico.antimonstercrystal.config.ConfigHandler;
import com.scolastico.antimonstercrystal.config.CrystalDataStore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.UUID;

public class Updater {

    public void update(boolean fresh) {
        try {
            if (!AntiMonsterCrystal.getPlugin().getDescription().getVersion().equals(AntiMonsterCrystal.getConfigDataStore().getVersion())) {

                ConfigDataStore config = AntiMonsterCrystal.getConfigDataStore();
                if (fresh) config.setVersion(AntiMonsterCrystal.getPlugin().getDescription().getVersion());
                int version = 0;

                try {
                    version = Integer.parseInt(config.getVersion().replaceAll("\\.", ""));
                } catch (Exception ignored) {}

                if (version < 110) {
                    config = upgrade110(config);
                }

                config.setVersion(AntiMonsterCrystal.getPlugin().getDescription().getVersion());
                AntiMonsterCrystal.setConfigDataStore(config);
                ConfigHandler handler = AntiMonsterCrystal.getConfigConfigHandler();
                handler.setConfigObject(config);
                handler.saveConfigObject();

            }
        } catch (Exception e) {
            ErrorHandler.getInstance().handleFatal(e);
        }
    }

    private ConfigDataStore upgrade110(ConfigDataStore config) {

        Language.getInstance().sendColorMessage(Language.GOLD + "Updating from version '<1.1.0'.", Bukkit.getConsoleSender());

        HashMap<String, String> language = config.getLanguage();
        language.put("already_reloading", "&cAlready reloading please wait a few seconds!");
        language.put("reloading", "&aReloading...");
        language.put("not_found", "&cPlayer not found.");
        language.put("help_delete_player", "&2/AntiMonsterCrystal delete-all <player> - Deletes all AntiMonsterCrystals from a player.");
        config.setLanguage(language);

        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        CrystalDataStore newDataStore = new CrystalDataStore();

        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            Entity entity = Bukkit.getEntity(UUID.fromString(data.getCrystalUUID()));
            if(entity != null) {
                Location location = new Location(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getWorld().getName());
                CrystalDataStore.CrystalData newData = new CrystalDataStore.CrystalData(data.getPlacedByUUID(), data.getCrystalUUID(), location);
                newDataStore.addCrystalData(newData);
                entity.remove();
            }
        }

        for (World world:Bukkit.getWorlds()) {
            for (Entity entity:world.getEntities()) {
                if (entity.getType() == EntityType.ENDER_CRYSTAL) {
                    if (entity.getCustomName() != null) {
                        if (entity.getCustomName().equals("§4AntiMonsterCrystal")) {
                            Location location = new Location(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getWorld().getName());
                            CrystalDataStore.CrystalData newData = new CrystalDataStore.CrystalData("069a79f4-44e9-4726-a5be-fca90e38aaf5", entity.getUniqueId().toString(), location);
                            newDataStore.addCrystalData(newData);
                            entity.remove();
                        }
                    }
                }
            }
        }

        AntiMonsterCrystal.setCrystalDataStore(newDataStore);

        ConfigHandler handler = AntiMonsterCrystal.getCrystalDataStoreConfigHandler();
        handler.setConfigObject(newDataStore);

        try {
            handler.saveConfigObject();
        } catch (Exception e) {
            ErrorHandler.getInstance().handleFatal(e);
        }

        return config;

    }

}
