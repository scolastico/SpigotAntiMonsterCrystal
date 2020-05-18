package com.scolastico.antimonstercrystal.events;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.config.ConfigDataStore;
import com.scolastico.antimonstercrystal.config.ConfigHandler;
import com.scolastico.antimonstercrystal.config.CrystalDataStore;
import com.scolastico.antimonstercrystal.internal.ErrorHandler;
import org.bukkit.*;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import ru.beykerykt.lightapi.LightAPI;
import ru.beykerykt.lightapi.LightType;

import java.util.ArrayList;
import java.util.UUID;

public class OnTick {

    public void onTick() {

        CrystalDataStore dataStore = AntiMonsterCrystal.getCrystalDataStore();
        ArrayList<CrystalDataStore.CrystalData> store = new ArrayList<>();
        int radius = AntiMonsterCrystal.getConfigDataStore().getRadius();
        ConfigDataStore.Animations animations = AntiMonsterCrystal.getConfigDataStore().getAnimations();

        Sound soundAtBeam = null;
        Sound soundAtKill = null;
        Particle particleAtKill = null;

        try {
            if (!animations.getSoundAtBeam().isEmpty()) {
                soundAtBeam = Sound.valueOf(animations.getSoundAtBeam());
            }
            if (!animations.getSoundAtKill().isEmpty()) {
                soundAtKill = Sound.valueOf(animations.getSoundAtKill());
            }
            if (!animations.getParticlesAtKill().isEmpty()) {
                particleAtKill = Particle.valueOf(animations.getParticlesAtKill());
            }
        } catch (Exception e) {
            ErrorHandler.getInstance().handle(e);
        }

        try {
            for (CrystalDataStore.CrystalData data:dataStore.getCrystalData()) {
                if (data.getLocation().getLocation().getChunk().isLoaded()) {
                    Entity entity = Bukkit.getEntity(UUID.fromString(data.getCrystalUUID()));

                    if (entity != null) {
                        if (entity.getType() == EntityType.ENDER_CRYSTAL) {
                            EnderCrystal crystal = (EnderCrystal) entity;
                            store.add(data);
                            boolean _break = false;
                            for (Entity enemy:crystal.getNearbyEntities(radius, radius, radius)) {
                                if (!_break) {
                                    if (checkForEntityType(enemy.getType().toString())) {
                                        if (crystal.getLocation().distance(enemy.getLocation()) <= radius) {
                                            if (!enemy.isDead()) {

                                                World worldCrystal = crystal.getWorld();
                                                if (soundAtBeam != null) {
                                                    worldCrystal.playSound(crystal.getLocation(), soundAtBeam, 1, 1);
                                                }

                                                final Sound finalSoundAtKill = soundAtKill;
                                                final Particle finalParticleAtKill = particleAtKill;

                                                if (animations.isShowBeam()) crystal.setBeamTarget(enemy.getLocation());

                                                Bukkit.getScheduler().runTaskLater(AntiMonsterCrystal.getPlugin(), new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        World worldEnemy = enemy.getWorld();
                                                        if (finalSoundAtKill != null) worldEnemy.playSound(enemy.getLocation(), finalSoundAtKill, 1, 1);
                                                        if (finalParticleAtKill != null) worldEnemy.spawnParticle(finalParticleAtKill, enemy.getLocation(), animations.getParticleAmount());

                                                        if (AntiMonsterCrystal.getConfigDataStore().isDropItemsOnKill()) {
                                                            try {
                                                                ((LivingEntity) enemy).setHealth(0);
                                                            } catch (Exception ignored) {
                                                                enemy.remove();
                                                            }
                                                        } else {
                                                            enemy.remove();
                                                        }
                                                    }
                                                }, animations.getUntilKill());

                                                Bukkit.getScheduler().runTaskLater(AntiMonsterCrystal.getPlugin(), new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        crystal.setBeamTarget(null);
                                                    }
                                                }, animations.getUntilBeamReset());

                                                _break = true;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            entity.remove();
                        }
                    }
                }
            }

            try {
                if (store.size() != dataStore.getCrystalData().size()) {
                    dataStore.setCrystalData(store);
                    ConfigHandler handler = AntiMonsterCrystal.getCrystalDataStoreConfigHandler();
                    handler.setConfigObject(dataStore);
                    handler.saveConfigObject();
                    AntiMonsterCrystal.setCrystalDataStore(dataStore);
                }
            } catch (Exception e) {
                ErrorHandler.getInstance().handleFatal(e);
            }
        } catch (Exception e) {
            ErrorHandler.getInstance().handle(e);
        }

    }

    private boolean checkForEntityType(String type) {
        ArrayList<String> list = AntiMonsterCrystal.getConfigDataStore().getMonsters();
        for (String monster:list) {
            if (monster.equalsIgnoreCase(type)) return true;
        }
        return false;
    }

}
