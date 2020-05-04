package com.scolastico.antimonstercrystal.internal;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class Language {

    public static final String DARK_RED = "&4";
    public static final String DARK_BLUE = "&1";
    public static final String GOLD = "&6";
    public static final String DARK_GREEN = "&2";
    public static final String DARK_PURPLE = "&5";
    public static final String WHITE = "&f";
    public static final String DARK_CYAN = "&3";
    public static final String CYAN = "&b";
    public static final String GREEN = "&a";
    public static final String YELLOW = "&e";
    public static final String RED = "&c";
    public static final String GRAY = "&7";
    public static final String DARK_GRAY = "&8";
    public static final String BLACK = "&0";
    public static final String PURPLE = "&d";
    public static final String BLUE = "&9";

    public static final String BOLD = "&l";
    public static final String MAGIC = "&k";
    public static final String UNDERLINE = "&n";
    public static final String STRIKE = "&m";
    public static final String ITALIC = "&o";
    public static final String RESET = "&r";

    private static Language instance = null;
    private String prefix = "&2[&a+&2]";
    private HashMap<String, String> strings = new HashMap<>();

    private Language() {}

    public static Language getInstance() {
        if (instance == null) instance = new Language();
        return instance;
    }

    public void sendColorMessage(String message, CommandSender to) {
        sendColorMessage(message, to, true);
    }

    public void sendColorMessage(String message, CommandSender to, boolean prefix) {
        if (prefix) message = this.prefix + " &r" + message;
        to.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendConfigMessage(String path, CommandSender to) {
        sendConfigMessage(path, to, true);
    }

    public void sendConfigMessage(String path, CommandSender to, boolean prefix) {
        sendConfigMessage(path, to, prefix, new HashMap<>());
    }

    public void sendConfigMessage(String path, CommandSender to, boolean prefix, HashMap<String, String> replace) {
        String message = strings.containsKey(path) ? strings.get(path) : Language.RED + "Sorry, this plugin is not correct configured. Please inform the staff that this setting is missing in the language config: '" + Language.BOLD + path + Language.RESET + Language.RED + "'";
        if (message.isEmpty()) return;
        for (String key:replace.keySet()) {
            message = message.replaceAll(key, replace.get(key));
        }
        if (prefix) message = this.prefix + " &r" + message;
        to.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public HashMap<String, String> getStrings() {
        return strings;
    }

    public void setStrings(HashMap<String, String> strings) {
        this.strings = strings;
    }

}
