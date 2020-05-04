package com.scolastico.antimonstercrystal.api;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.config.ConfigHandler;
import com.scolastico.antimonstercrystal.config.CrystalDataStore;
import com.scolastico.antimonstercrystal.internal.ErrorHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AntiMonsterCrystalAPI {

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
                Entity entity = Bukkit.getEntity(UUID.fromString(data.getCrystalUUID()));
                if(entity != null) entity.remove();
            }
        }
    }

    public void deleteAllCrystals() {
        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
            Entity entity = Bukkit.getEntity(UUID.fromString(data.getCrystalUUID()));
            if(entity != null) entity.remove();
        }
    }

    public void spawnCrystal(String uuid, double x, double y, double z, World world) {
        Location location = new Location(world, x, y, z, 0, 0);
        spawnCrystal(uuid, location, world);
    }

    public void spawnCrystal(String uuid, Location location, World world) {
        try {
            CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
            Entity entity = world.spawnEntity(location, EntityType.ENDER_CRYSTAL);
            entity.setCustomNameVisible(true);
            entity.setCustomName("§4AntiMonsterCrystal");
            EnderCrystal enderCrystal = (EnderCrystal) entity;
            enderCrystal.setShowingBottom(false);
            CrystalDataStore.CrystalData entry = new CrystalDataStore.CrystalData(uuid, entity.getUniqueId().toString());
            dataStore.addCrystalData(entry);
            ConfigHandler handler = AntiMonsterCrystal.getCrystalDataStoreConfigHandler();
            handler.setConfigObject(dataStore);
            handler.saveConfigObject();
            AntiMonsterCrystal.setCrystalDataStore(dataStore);
        } catch (Exception e) {
            ErrorHandler.getInstance().handleFatal(e);
        }
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
        meta.setDisplayName("§1§d§cAntiMonsterCrystal");
        List<String> lore = new ArrayList<>();
        lore.add("Place to auto defend your base!");
        lore.add("Does no block damage if it explodes!");
        meta.setLore(lore);
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
