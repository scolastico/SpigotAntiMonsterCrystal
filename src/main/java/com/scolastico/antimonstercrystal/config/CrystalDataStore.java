package com.scolastico.antimonstercrystal.config;

import com.scolastico.antimonstercrystal.internal.Location;

import java.util.ArrayList;

public class CrystalDataStore {

    private ArrayList<CrystalData> crystalData = new ArrayList<>();

    public ArrayList<CrystalData> getCrystalData() {
        return crystalData;
    }

    public void setCrystalData(ArrayList<CrystalData> crystalData) {
        this.crystalData = crystalData;
    }

    public void addCrystalData(CrystalData crystalData) {
        this.crystalData.add(crystalData);
    }

    public static class CrystalData {
        private String placedByUUID;
        private String crystalUUID;
        private Location location;

        public CrystalData(String placedByUUID, String crystalUUID, Location location) {
            this.placedByUUID = placedByUUID;
            this.crystalUUID = crystalUUID;
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getPlacedByUUID() {
            return placedByUUID;
        }

        public void setPlacedByUUID(String placedByUUID) {
            this.placedByUUID = placedByUUID;
        }

        public String getCrystalUUID() {
            return crystalUUID;
        }

        public void setCrystalUUID(String crystalUUID) {
            this.crystalUUID = crystalUUID;
        }
    }

}
