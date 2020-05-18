package com.scolastico.antimonstercrystal.internal;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.api.AntiMonsterCrystalAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import ru.beykerykt.lightapi.LightAPI;
import ru.beykerykt.lightapi.LightType;

import java.util.ArrayList;

public class LightHandler {

    private ArrayList<Location> locations = new ArrayList<>();
    private static LightHandler instance = null;
    private boolean enabled = false;
    private AntiMonsterCrystalAPI api = new AntiMonsterCrystalAPI();

    public static LightHandler getInstance() {
        if (instance == null) {
            instance = new LightHandler();
        }
        return instance;
    }

    private LightHandler() {}

    public void enable() {
        if (AntiMonsterCrystal.isLightSource()) {
            if (!enabled) {
                enabled = true;
                for (Location location:locations) {
                    LightAPI.createLight(location, LightType.BLOCK, 15, false);
                }
            }
        }
    }

    public void disable() {
        if (AntiMonsterCrystal.isLightSource()) {
            if (enabled) {
                enabled = false;
                for (Location location:locations) {
                    LightAPI.deleteLight(location, LightType.BLOCK, false);
                }
            }
        }
    }

    public void create(Location location) {
        if (location.getWorld() != null) {
            locations.add(location);
            if (AntiMonsterCrystal.isLightSource()) {
                if (enabled) {
                    LightAPI.createLight(location, LightType.BLOCK, 15, false);
                }
            }
        }
    }

    public void check() {
        ArrayList<Location> toDelete = new ArrayList<>();
        for (Location location:locations) {
            World world = location.getWorld();
            if (world != null) {
                if (location.getChunk().isLoaded()) {
                    boolean nearbyCrystal = false;
                    for (Entity entity:world.getNearbyEntities(location, 1, 1, 1)) {
                        if (entity.getType() == EntityType.ENDER_CRYSTAL) {
                            if (api.isCrystal(entity.getUniqueId())) {
                                nearbyCrystal = true;
                            }
                        }
                    }
                    if (!nearbyCrystal) {
                        toDelete.add(location);
                    }
                }
            } else {
                ErrorHandler.getInstance().handle(new Exception("World is null in LightHandler."));
            }
        }
        for (Location location:toDelete) {
            locations.remove(location);
            if (AntiMonsterCrystal.isLightSource()) {
                if (enabled) {
                    LightAPI.deleteLight(location, LightType.BLOCK, false);
                }
            }
        }
    }

}
