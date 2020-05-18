package com.scolastico.antimonstercrystal.events;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.api.AntiMonsterCrystalAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnDamage implements Listener {

    private AntiMonsterCrystalAPI api = new AntiMonsterCrystalAPI();

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.ENDER_CRYSTAL) {
            EnderCrystal crystal = (EnderCrystal) event.getDamager();
            if (api.isCrystal(crystal.getUniqueId())) {
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
