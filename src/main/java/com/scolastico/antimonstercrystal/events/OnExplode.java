package com.scolastico.antimonstercrystal.events;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class OnExplode implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.ENDER_CRYSTAL) {
            EnderCrystal entity = (EnderCrystal) event.getEntity();
            if (entity.getCustomName() != null) {
                if (entity.getCustomName().equals("§4AntiMonsterCrystal")) {
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

}
