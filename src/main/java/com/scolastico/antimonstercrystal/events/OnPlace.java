package com.scolastico.antimonstercrystal.events;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.api.AntiMonsterCrystalAPI;
import com.scolastico.antimonstercrystal.config.ConfigDataStore;
import com.scolastico.antimonstercrystal.internal.ErrorHandler;
import com.scolastico.antimonstercrystal.internal.Language;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class OnPlace implements Listener {

    AntiMonsterCrystalAPI api = new AntiMonsterCrystalAPI();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            if (event.useItemInHand() != Event.Result.DENY) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getItem() != null) {
                        ItemStack is = event.getItem();
                        ItemStack should = api.getItemStack();
                        ItemMeta isMeta = is.getItemMeta();
                        ItemMeta shouldMeta = should.getItemMeta();
                        if (isMeta != null && shouldMeta != null) {
                            List<String> isLore = isMeta.getLore();
                            List<String> shouldLore = shouldMeta.getLore();
                            if (isLore != null && shouldLore != null) {
                                if (is.getType() == should.getType() && isMeta.getDisplayName().equals(shouldMeta.getDisplayName()) && isLore.equals(shouldLore)) {
                                    event.setCancelled(true);
                                    if ((event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.CREATIVE) || (!AntiMonsterCrystal.getConfigDataStore().isGamemodeControl())) {
                                        if (event.getPlayer().hasPermission("antimonstercrystal.place")) {
                                            ConfigDataStore config = AntiMonsterCrystal.getConfigDataStore();
                                            if ((api.getPlacedAmount(event.getPlayer())<config.getMaxPerAccount() || config.getMaxPerAccount() < 0) || event.getPlayer().hasPermission("")) {
                                                if (event.getClickedBlock() != null) {
                                                    if (config.getWorldWhitelist().contains(event.getClickedBlock().getWorld().getName()) || !config.isEnableWorldWhiteList()) {
                                                        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                                                            is.setAmount(is.getAmount()-1);
                                                        }
                                                        BlockFace face = event.getBlockFace();
                                                        Block block = event.getClickedBlock();
                                                        api.spawnCrystal(event.getPlayer().getUniqueId().toString(), block.getX()+face.getModX()+0.5, block.getY()+face.getModY(), block.getZ()+face.getModZ()+0.5, event.getClickedBlock().getWorld());
                                                        Language.getInstance().sendConfigMessage("spawned", event.getPlayer());
                                                    } else {
                                                        Language.getInstance().sendConfigMessage("only_over_world", event.getPlayer());
                                                    }
                                                }
                                            } else {
                                                Language.getInstance().sendConfigMessage("max", event.getPlayer());
                                            }
                                        } else {
                                            Language.getInstance().sendConfigMessage("place_permission", event.getPlayer());
                                        }
                                    } else {
                                        Language.getInstance().sendConfigMessage("gamemode", event.getPlayer());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ErrorHandler.getInstance().handle(e);
        }
    }

}
