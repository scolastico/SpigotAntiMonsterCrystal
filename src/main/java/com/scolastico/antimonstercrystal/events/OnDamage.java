package com.scolastico.antimonstercrystal.events;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnDamage implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.ENDER_CRYSTAL) {
            EnderCrystal crystal = (EnderCrystal) event.getDamager();
            if (crystal.getCustomName() != null) {
                if (crystal.getCustomName().equals("§4AntiMonsterCrystal")) {
                    if (!(event.getEntity() instanceof Player)) {
                        if (!AntiMonsterCrystal.getConfigDataStore().isDamageEntity()) {
                            event.setCancelled(true);
                        }
                    } else if (!AntiMonsterCrystal.getConfigDataStore().isDamagePlayer()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
