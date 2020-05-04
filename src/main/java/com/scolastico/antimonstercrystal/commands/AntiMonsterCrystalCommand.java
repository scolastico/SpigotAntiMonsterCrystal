package com.scolastico.antimonstercrystal.commands;

import com.scolastico.antimonstercrystal.AntiMonsterCrystal;
import com.scolastico.antimonstercrystal.api.AntiMonsterCrystalAPI;
import com.scolastico.antimonstercrystal.config.CrystalDataStore;
import com.scolastico.antimonstercrystal.internal.ErrorHandler;
import com.scolastico.antimonstercrystal.internal.Language;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AntiMonsterCrystalCommand implements CommandExecutor, TabCompleter {

    private HashMap<Long, String> confirm = new HashMap<>();

    private final AntiMonsterCrystalAPI api = new AntiMonsterCrystalAPI();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (String arg:args) {
                stringBuilder.append(" ").append(arg);
            }
            String argsAsString = stringBuilder.toString();
            if (args.length == 0) {
                Language.getInstance().sendColorMessage(Language.CYAN + "AntiMonsterCrystal", commandSender);
                Language.getInstance().sendColorMessage(Language.CYAN + "Defend your base with crystals!", commandSender);
                Language.getInstance().sendColorMessage(Language.CYAN + "Plugin by https://scolasti.co/", commandSender);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (commandSender.hasPermission("antimonstercrystal.admin.reload")) {
                        AntiMonsterCrystal.reloadConfigData();
                        Language.getInstance().sendConfigMessage("done", commandSender);
                    } else Language.getInstance().sendConfigMessage("permission", commandSender);
                    return true;
                } else if (args[0].equalsIgnoreCase("give")) {
                    if (commandSender.hasPermission("antimonstercrystal.give")) {
                        if (commandSender instanceof Player) {
                            api.givePlayerCrystal((Player) commandSender, 1);
                            Language.getInstance().sendConfigMessage("done", commandSender);
                        } else {
                            Language.getInstance().sendConfigMessage("not_a_player", commandSender);
                        }
                    } else Language.getInstance().sendConfigMessage("permission", commandSender);
                    return true;
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (commandSender.hasPermission("antimonstercrystal.delete.range")) {
                        if (commandSender instanceof Player) {
                            Player player = (Player) commandSender;
                            clearConfirm();
                            if (confirm.containsValue("delete-" + player.getUniqueId().toString())) {
                                deleteFromConfirm("delete-" + player.getUniqueId().toString());
                                ArrayList<CrystalDataStore.CrystalData> data = api.getDataFromPlacedUUID(player.getUniqueId().toString());
                                ArrayList<String> uuid = new ArrayList<>();
                                for (CrystalDataStore.CrystalData crystalData:data) {
                                    uuid.add(crystalData.getCrystalUUID());
                                }
                                for (Entity entity:player.getNearbyEntities(5,5,5)) {
                                    if (entity instanceof EnderCrystal) {
                                        EnderCrystal crystal = (EnderCrystal) entity;
                                        if (crystal.getLocation().distance(player.getLocation()) <= 5) {
                                            if (crystal.getName().equals("§4AntiMonsterCrystal")) {
                                                if (uuid.contains(crystal.getUniqueId().toString())) {
                                                    crystal.remove();
                                                    if (AntiMonsterCrystal.getConfigDataStore().isGiveItemBackOnRemoveWithCommand()) {
                                                        api.givePlayerCrystal(player, 1);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                Language.getInstance().sendConfigMessage("done", commandSender);
                            } else {
                                confirm.put(getUnixTimeStamp()+15, "delete-" + player.getUniqueId().toString());
                                HashMap<String, String> placeholder = new HashMap<>();
                                placeholder.put("%command%", command.toString() + argsAsString);
                                Language.getInstance().sendConfigMessage("confirm", commandSender, true, placeholder);
                            }
                        } else {
                            Language.getInstance().sendConfigMessage("not_a_player", commandSender);
                        }
                    } else Language.getInstance().sendConfigMessage("permission", commandSender);
                    return true;
                } else if (args[0].equalsIgnoreCase("delete-all")) {
                    if (commandSender.hasPermission("antimonstercrystal.delete.all")) {
                        if (commandSender instanceof Player) {
                            Player player = (Player) commandSender;
                            clearConfirm();
                            if (confirm.containsValue("delete-all-" + player.getUniqueId().toString())) {
                                deleteFromConfirm("delete-all-" + player.getUniqueId().toString());
                                ArrayList<CrystalDataStore.CrystalData> data = api.getDataFromPlacedUUID(player.getUniqueId().toString());
                                for (CrystalDataStore.CrystalData crystalData:data) {
                                    Entity entity = Bukkit.getEntity(UUID.fromString(crystalData.getCrystalUUID()));
                                    if (entity != null) {
                                        entity.remove();
                                        if (AntiMonsterCrystal.getConfigDataStore().isGiveItemBackOnRemoveWithCommand()) {
                                            api.givePlayerCrystal(player, 1);
                                        }
                                    }
                                }
                                Language.getInstance().sendConfigMessage("done", commandSender);
                            } else {
                                confirm.put(getUnixTimeStamp()+15, "delete-all-" + player.getUniqueId().toString());
                                HashMap<String, String> placeholder = new HashMap<>();
                                placeholder.put("%command%", command.toString() + argsAsString);
                                Language.getInstance().sendConfigMessage("confirm", commandSender, true, placeholder);
                            }
                        } else {
                            Language.getInstance().sendConfigMessage("not_a_player", commandSender);
                        }
                    } else Language.getInstance().sendConfigMessage("permission", commandSender);
                    return true;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (commandSender.hasPermission("antimonstercrystal.admin.reset")) {
                        String sender = "console";
                        if (commandSender instanceof Player) sender = ((Player) commandSender).getUniqueId().toString();
                        clearConfirm();
                        if (confirm.containsValue("reset-" + sender)) {
                            deleteFromConfirm("reset-" + sender);
                            for (CrystalDataStore.CrystalData crystalData:AntiMonsterCrystal.getCrystalDataStore().getCrystalData()) {
                                Entity entity = Bukkit.getEntity(UUID.fromString(crystalData.getCrystalUUID()));
                                if (entity != null) {
                                    entity.remove();
                                }
                            }
                            Language.getInstance().sendConfigMessage("done", commandSender);
                        } else {
                            confirm.put(getUnixTimeStamp()+15, "reset-" + sender);
                            HashMap<String, String> placeholder = new HashMap<>();
                            placeholder.put("%command%", command.toString() + argsAsString);
                            Language.getInstance().sendConfigMessage("confirm", commandSender, true, placeholder);
                        }
                    } else Language.getInstance().sendConfigMessage("permission", commandSender);
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (commandSender.hasPermission("antimonstercrystal.give")) {
                        for (Player player:Bukkit.getServer().getOnlinePlayers()) {
                            if (player.getName().equalsIgnoreCase(args[1])) {
                                api.givePlayerCrystal((Player) commandSender, 1);
                                Language.getInstance().sendConfigMessage("done", commandSender);
                                return true;
                            }
                        }
                        Language.getInstance().sendConfigMessage("not_online", commandSender);
                    } else Language.getInstance().sendConfigMessage("permission", commandSender);
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (commandSender.hasPermission("antimonstercrystal.give")) {
                        int amount = 0;
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException ignored) {}
                        if (amount > 0 && amount < 65) {
                            for (Player player:Bukkit.getServer().getOnlinePlayers()) {
                                if (player.getName().equalsIgnoreCase(args[1])) {
                                    api.givePlayerCrystal((Player) commandSender, amount);
                                    Language.getInstance().sendConfigMessage("done", commandSender);
                                    return true;
                                }
                            }
                            Language.getInstance().sendConfigMessage("not_online", commandSender);
                            return true;
                        }
                        Language.getInstance().sendConfigMessage("int_error", commandSender);
                    } else Language.getInstance().sendConfigMessage("permission", commandSender);
                    return true;
                }
            }
            Language.getInstance().sendConfigMessage("help_help", commandSender);
            if (commandSender.hasPermission("antimonstercrystal.give")) Language.getInstance().sendConfigMessage("help_give", commandSender);
            if (commandSender.hasPermission("antimonstercrystal.delete.range")) Language.getInstance().sendConfigMessage("help_delete", commandSender);
            if (commandSender.hasPermission("antimonstercrystal.delete.all")) Language.getInstance().sendConfigMessage("help_delete_all", commandSender);
            if (commandSender.hasPermission("antimonstercrystal.admin.reset")) Language.getInstance().sendConfigMessage("help_reset", commandSender);
            if (commandSender.hasPermission("antimonstercrystal.admin.reload")) Language.getInstance().sendConfigMessage("help_reload", commandSender);
        } catch (Exception e) {
            ErrorHandler.getInstance().handle(e);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        if (args.length == 1) {
            tabComplete.add("help");
            if (commandSender.hasPermission("antimonstercrystal.give")) tabComplete.add("give");
            if (commandSender.hasPermission("antimonstercrystal.delete.range")) tabComplete.add("delete");
            if (commandSender.hasPermission("antimonstercrystal.delete.all")) tabComplete.add("delete-all");
            if (commandSender.hasPermission("antimonstercrystal.admin.reset")) tabComplete.add("reset");
            if (commandSender.hasPermission("antimonstercrystal.admin.reload")) tabComplete.add("reload");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                for (Player player:Bukkit.getOnlinePlayers()) {
                    tabComplete.add(player.getName());
                }
            }
        }
        List<String> ret = new ArrayList<>();
        if (args.length > 0) {
            for (String string:tabComplete) {
                if (string.startsWith(args[args.length-1])) {
                    ret.add(string);
                }
            }
        }
        return ret;
    }

    private void clearConfirm() {
        ArrayList<Long> toDelete = new ArrayList<>();
        for (Long time:confirm.keySet()) {
            if (getUnixTimeStamp()>time) {
                toDelete.add(time);
            }
        }
        for (Long delete:toDelete) {
            confirm.remove(delete);
        }
    }

    private void deleteFromConfirm(String string) {
        ArrayList<Long> toDelete = new ArrayList<>();
        for (Long time:confirm.keySet()) {
            if (confirm.get(time).equals(string)) toDelete.add(time);
        }
        for (Long delete:toDelete) {
            confirm.remove(delete);
        }
    }

    private long getUnixTimeStamp() {
        return System.currentTimeMillis() / 1000L;
    }
}
