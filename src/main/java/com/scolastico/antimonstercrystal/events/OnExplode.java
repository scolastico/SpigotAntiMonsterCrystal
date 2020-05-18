package com.scolastico.antimonstercrystal.events;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.api.AntiMonsterCrystalAPI;
import com.scolastico.antimonstercrystal.config.CrystalDataStore;
import org.bukkit.*;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;

public class OnExplode implements Listener {

    private AntiMonsterCrystalAPI api = new AntiMonsterCrystalAPI();

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.ENDER_CRYSTAL) {
            EnderCrystal entity = (EnderCrystal) event.getEntity();
            if (api.isCrystal(entity.getUniqueId())) {
                ArrayList<CrystalDataStore.CrystalData> data = new ArrayList<>();
                for (Entity nearby:entity.getNearbyEntities(15, 15, 15)) {
                    if (api.isCrystal(nearby.getUniqueId())) {
                        data.add(api.getDataFromCrystalUUID(nearby.getUniqueId().toString()));
                        nearby.remove();
                    }
                }
                Bukkit.getScheduler().runTaskLater(AntiMonsterCrystal.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        for (CrystalDataStore.CrystalData crystalData:data) {
                            api.spawnCrystal(crystalData.getPlacedByUUID(), crystalData.getLocation().getLocation(), crystalData.getLocation().getWorld());
                        }
                    }
                },5);
                if (!AntiMonsterCrystal.getConfigDataStore().isDamageBlock()) {
                    event.setCancelled(true);
                    World world = entity.getWorld();
                    world.playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, entity.getLocation(), 10);
                    entity.remove();
                }
            }
        }
    }

}
