package com.scolastico.antimonstercrystal.events;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.api.AntiMonsterCrystalAPI;
import com.scolastico.antimonstercrystal.internal.ErrorHandler;
import com.scolastico.antimonstercrystal.internal.Language;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class OnCraft implements Listener {

    AntiMonsterCrystalAPI api = new AntiMonsterCrystalAPI();

    @EventHandler
    public void CraftItemEvent(CraftItemEvent event) {
        try {
            ItemStack is = event.getRecipe().getResult();
            ItemStack should = api.getItemStack();
            ItemMeta isMeta = is.getItemMeta();
            ItemMeta shouldMeta = should.getItemMeta();
            if (isMeta != null && shouldMeta != null) {
                List<String> isLore = isMeta.getLore();
                List<String> shouldLore = shouldMeta.getLore();
                if (isLore != null && shouldLore != null) {
                    if (is.getType() == should.getType() && isMeta.getDisplayName().equals(shouldMeta.getDisplayName()) && isLore.equals(shouldLore)) {
                        if (!event.getWhoClicked().hasPermission("antimonstercrystal.craft")) {
                            Language.getInstance().sendConfigMessage("craft_permission", event.getWhoClicked());
                            event.setCancelled(true);
                        } else {
                            if ((!AntiMonsterCrystal.getConfigDataStore().isAllowCraftingInNotWhitelistedWorld()) && (AntiMonsterCrystal.getConfigDataStore().isEnableWorldWhiteList())) {
                                if (AntiMonsterCrystal.getConfigDataStore().getWorldWhitelist().contains(event.getWhoClicked().getWorld().getName())) {
                                    Language.getInstance().sendConfigMessage("craft_permission", event.getWhoClicked());
                                    event.setCancelled(true);
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
