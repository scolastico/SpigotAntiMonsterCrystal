package com.scolastico.antimonstercrystal.api;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.config.ConfigHandler;
import com.scolastico.antimonstercrystal.config.CrystalDataStore;
import com.scolastico.antimonstercrystal.internal.ErrorHandler;
import com.scolastico.antimonstercrystal.internal.Language;
import com.scolastico.antimonstercrystal.internal.LightHandler;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class AntiMonsterCrystalAPI {

    public boolean isCrystal(UUID uuid) {
        return isCrystal(uuid.toString());
    }

    public boolean isCrystal(String uuid) {
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            if (data.getCrystalUUID().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<CrystalDataStore.CrystalData> getDataFromPlacedUUID(String uuid) {
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        ArrayList<CrystalDataStore.CrystalData> ret = new ArrayList<>();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            if (data.getPlacedByUUID().equals(uuid)) {
                ret.add(data);
            }
        }
        return ret;
    }

    public CrystalDataStore.CrystalData getDataFromCrystalUUID(String uuid) {
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            if (data.getCrystalUUID().equals(uuid)) {
                return data;
            }
        }
        return null;
    }

    public void deleteAllCrystalsFromUUID(String uuid) {
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            if (data.getPlacedByUUID().equals(uuid)) {
                Chunk chunk = data.getLocation().getLocation().getChunk();
                chunk.load();
                if (chunk.isLoaded()) {
                    Entity entity = Bukkit.getEntity(UUID.fromString(data.getCrystalUUID()));
                    if(entity != null) entity.remove();
                }
                chunk.unload();
            }
        }
    }

    public void deleteAllCrystals() {
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            Chunk chunk = data.getLocation().getLocation().getChunk();
            chunk.load();
            if (chunk.isLoaded()) {
                Entity entity = Bukkit.getEntity(UUID.fromString(data.getCrystalUUID()));
                if(entity != null) entity.remove();
            }
            chunk.unload();
        }
    }

    public void deleteAllCrystalsFromLocations() {
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            if (data.getLocation().getWorld() != null) {
                Chunk chunk = data.getLocation().getLocation().getChunk();
                chunk.load();
                if (chunk.isLoaded()) {
                    for (Entity entity:data.getLocation().getWorld().getNearbyEntities(data.getLocation().getLocation(), 1, 1, 1)) {
                        if (entity.getType() == EntityType.ENDER_CRYSTAL) entity.remove();
                    }
                }
                chunk.unload();
            }
        }
    }

    public void spawnCrystalsFromConfig() {
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        CrystalDataStore newDataStore = new CrystalDataStore();
        ArrayList<String> notFoundWorlds = new ArrayList<>();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            if (data.getLocation().getWorld() != null) {
                Chunk chunk = data.getLocation().getLocation().getChunk();
                chunk.load();
                if (chunk.isLoaded()) {
                    UUID uuid = spawnCrystal(data.getLocation().getLocation(), data.getLocation().getWorld());
                    CrystalDataStore.CrystalData newData = new CrystalDataStore.CrystalData(data.getPlacedByUUID(), uuid.toString(), data.getLocation());
                    newDataStore.addCrystalData(newData);
                } else {
                    Language.getInstance().sendColorMessage(Color.RED + "Cant load chunk ", Bukkit.getConsoleSender());
                }
                chunk.unload();
            } else {
                if (!notFoundWorlds.contains(data.getLocation().getWorldName())) {
                    notFoundWorlds.add(data.getLocation().getWorldName());
                    Language.getInstance().sendColorMessage(Color.RED + "Cant find world '" + data.getLocation().getWorldName() + "'. Deleting crystals from data.", Bukkit.getConsoleSender());
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
    }

    public void spawnCrystal(String uuid, double x, double y, double z, World world) {
        Location location = new Location(world, x, y, z, 0, 0);
        spawnCrystal(uuid, location, world);
    }

    public void spawnCrystal(String uuid, Location location, World world) {
        try {
            CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
            com.scolastico.antimonstercrystal.internal.Location loc = new com.scolastico.antimonstercrystal.internal.Location(location.getX(), location.getY(), location.getZ(), world.getName());
            CrystalDataStore.CrystalData entry = new CrystalDataStore.CrystalData(uuid, spawnCrystal(location, world).toString(), loc);
            dataStore.addCrystalData(entry);
            ConfigHandler handler = AntiMonsterCrystal.getCrystalDataStoreConfigHandler();
            handler.setConfigObject(dataStore);
            handler.saveConfigObject();
            AntiMonsterCrystal.setCrystalDataStore(dataStore);
        } catch (Exception e) {
            ErrorHandler.getInstance().handleFatal(e);
        }
    }

    private UUID spawnCrystal(Location location, World world) {
        location.setWorld(world);
        LightHandler.getInstance().create(location);
        Entity entity = world.spawnEntity(location, EntityType.ENDER_CRYSTAL);
        entity.setCustomNameVisible(AntiMonsterCrystal.getConfigDataStore().isShowCrystalName());
        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', AntiMonsterCrystal.getConfigDataStore().getCrystalName()));
        ((EnderCrystal) entity).setShowingBottom(false);
        return entity.getUniqueId();
    }

    public int getPlacedAmount(Player player) {
        int ret = 0;
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            if (data.getPlacedByUUID().equals(player.getUniqueId().toString())) ret++;
        }
        return ret;
    }

    public void givePlayerCrystal(Player player) throws PlayerNotOnlineException, ItemStackFactoryException {
        givePlayerCrystal(player, 1);
    }

    public void givePlayerCrystal(Player player, int amount) throws PlayerNotOnlineException, ItemStackFactoryException {
        if (!player.isOnline()) throw new PlayerNotOnlineException();
        player.getInventory().addItem(getItemStack(amount));
    }

    public ItemStack getItemStack() throws ItemStackFactoryException {
        return getItemStack(1);
    }

    public ItemStack getItemStack(int amount) throws ItemStackFactoryException {
        ItemStack itemStack = new ItemStack(Material.END_CRYSTAL);
        itemStack.setAmount(amount);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            ItemStackFactoryException exception = new ItemStackFactoryException("Cant get item meta in item factory.");
            ErrorHandler.getInstance().handleFatal(exception);
            throw exception;
        }
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', AntiMonsterCrystal.getConfigDataStore().getCrystalName()));
        meta.setLore(AntiMonsterCrystal.getConfigDataStore().getLore());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static class ItemStackFactoryException extends Exception {
        private ItemStackFactoryException() {}
        private ItemStackFactoryException(String message) {
            super(message);
        }
    }

    public static class PlayerNotOnlineException extends Exception {
        private PlayerNotOnlineException() {}
    }

}
