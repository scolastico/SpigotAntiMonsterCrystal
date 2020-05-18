package com.scolastico.antimonstercrystal.internal;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Location {

    private double x;
    private double y;
    private double z;
    private String world;

    public Location(double x, double y, double z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public org.bukkit.Location getLocation() {
        return new org.bukkit.Location(Bukkit.getWorld(world), x, y, z, 0, 0);
    }

    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getWorldName() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

}
