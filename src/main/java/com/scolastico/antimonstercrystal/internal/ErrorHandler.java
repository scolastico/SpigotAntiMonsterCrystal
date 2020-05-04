package com.scolastico.antimonstercrystal.internal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorHandler {

    private static ErrorHandler instance = null;
    private String name = "%plugin_name%";
    private String email = "support@scolasti.co";
    private ErrorHandler() {}
    public static ErrorHandler getInstance() {
        if (instance == null) {
            instance = new ErrorHandler();
        }
        return instance;
    }

    public void handle(Exception e) {
        outputErrorInfo(e);
    }

    public void handleFatal(Exception e) {
        System.err.println("[" + name + "] [ERROR] FATAL ERROR! SHUTTING DOWN!");
        outputErrorInfo(e);
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin != null) Bukkit.getPluginManager().disablePlugin(plugin);
    }

    private void outputErrorInfo(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String exceptionAsString = stringWriter.toString();
        System.err.println("[" + name + "] [ERROR] Oops... That should not happen... Please send this message to: " + email);
        try {
            System.err.println("[" + name + "] [ERROR] Server: " + Bukkit.getServer().getVersion());
        } catch (Exception ignored) {}
        try {
            System.err.println("[" + name + "] [ERROR] System: " + System.getProperty("os.name"));
        } catch (Exception ignored) {}
        try {
            System.err.println("[" + name + "] [ERROR] Java: " + System.getProperty("java.version"));
        } catch (Exception ignored) {}
        System.err.println("[" + name + "] [ERROR] Message: " + e.getMessage());
        System.err.println("[" + name + "] [ERROR] StackTrace:");
        System.err.println(exceptionAsString);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}