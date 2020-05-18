package com.scolastico.antimonstercrystal.config;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigDataStore {

    private String version = "0";
    private String crystalName = "&b&cAntiMonsterCrystal";
    private Long checkAllTicks = 15L;
    private int maxPerAccount = 5;
    private int radius = 30;
    private Crafting craftingRecipe = new Crafting();
    private boolean gamemodeControl = true;
    private boolean enableWorldWhiteList = true;
    private boolean allowCraftingInNotWhitelistedWorld = false;
    private boolean damagePlayer = true;
    private boolean damageEntity = false;
    private boolean damageBlock = false;
    private boolean giveItemBackOnRemoveWithCommand = false;
    private boolean showCrystalName = true;
    private boolean dropItemsOnKill = false;
    private boolean lightSource = false;
    private Animations animations = new Animations();
    private int lightCheckAll = 100;

    private ArrayList<String> lore = new ArrayList<String>() {
        {
            add("Place to auto defend your base!");
            add("Does no block damage if it explodes!");
        }
    };

    private ArrayList<String> worldWhitelist = new ArrayList<String>() {
        {
            add("world");
        }
    };

    private ArrayList<String> monsters = new ArrayList<String>() {
        {
            add("spider");
            add("zombie");
            add("enderman");
            add("creeper");
            add("skeleton");
            add("phantom");
            add("husk");
            add("stray");
        }
    };

    private HashMap<String, String> language = new HashMap<String, String>() {
        {
            put("prefix", "&3[&bAntiMonsterCrystal&3]");
            put("permission", "&cSorry, you dont have the permission to do that!");
            put("craft_permission", "&cSorry, you cant craft this item!");
            put("place_permission", "&cSorry, you cant place this item!");
            put("done", "&aDone!");
            put("error", "&cOops... That didnt worked! Look at the console for more information!");
            put("spawned", "&aSuccessfully created an anti monster crystal!");
            put("gamemode", "&cPlacing is only in survival or creative allowed!");
            put("max", "&2You spawned the max amount of anti monster crystals!");
            put("not_a_player", "&2Sorry, but you are not a player!");
            put("help_give", "&2/AntiMonsterCrystal give <name> <amount> - Give an player a AntiMonsterCrystal.");
            put("help_delete", "&2/AntiMonsterCrystal delete - Remove the next AntiMonsterCrystal from you in a 5 block radius.");
            put("help_delete_all", "&2/AntiMonsterCrystal delete-all - Remove all AntiMonsterCrystals from you.");
            put("help_reset", "&2/AntiMonsterCrystal reset - Reload the config.");
            put("help_reload", "&2/AntiMonsterCrystal reload - Reload the config.");
            put("help_help", "&2/AntiMonsterCrystal help - Shows this help site.");
            put("only_over_world", "&cSorry, you cant place this in this world!");
            put("confirm", "&cAre you sure? This cant be reverted! Enter '%command%' in the next 15 seconds again to confirm!");
            put("int_error", "&cSorry, only a value between 1 and 64 is allowed!");
            put("not_online", "&cPlayer seems to be not online.");
            put("already_reloading", "&cAlready reloading please wait a few seconds!");
            put("reloading", "&aReloading...");
            put("not_found", "&cPlayer not found.");
            put("help_delete_player", "&2/AntiMonsterCrystal delete-all <player> - Deletes all AntiMonsterCrystals from a player.");
        }
    };

    public int getLightCheckAll() {
        return lightCheckAll;
    }

    public void setLightCheckAll(int lightCheckAll) {
        this.lightCheckAll = lightCheckAll;
    }

    public boolean isLightSource() {
        return lightSource;
    }

    public void setLightSource(boolean lightSource) {
        this.lightSource = lightSource;
    }

    public boolean isDropItemsOnKill() {
        return dropItemsOnKill;
    }

    public void setDropItemsOnKill(boolean dropItemsOnKill) {
        this.dropItemsOnKill = dropItemsOnKill;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public void setLore(ArrayList<String> lore) {
        this.lore = lore;
    }

    public String getCrystalName() {
        return crystalName;
    }

    public void setCrystalName(String crystalName) {
        this.crystalName = crystalName;
    }

    public boolean isShowCrystalName() {
        return showCrystalName;
    }

    public void setShowCrystalName(boolean showCrystalName) {
        this.showCrystalName = showCrystalName;
    }

    public boolean isAllowCraftingInNotWhitelistedWorld() {
        return allowCraftingInNotWhitelistedWorld;
    }

    public void setAllowCraftingInNotWhitelistedWorld(boolean allowCraftingInNotWhitelistedWorld) {
        this.allowCraftingInNotWhitelistedWorld = allowCraftingInNotWhitelistedWorld;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isEnableWorldWhiteList() {
        return enableWorldWhiteList;
    }

    public void setEnableWorldWhiteList(boolean enableWorldWhiteList) {
        this.enableWorldWhiteList = enableWorldWhiteList;
    }

    public ArrayList<String> getWorldWhitelist() {
        return worldWhitelist;
    }

    public void setWorldWhitelist(ArrayList<String> worldWhitelist) {
        this.worldWhitelist = worldWhitelist;
    }

    public boolean isDamageEntity() {
        return damageEntity;
    }

    public void setDamageEntity(boolean damageEntity) {
        this.damageEntity = damageEntity;
    }

    public boolean isDamageBlock() {
        return damageBlock;
    }

    public void setDamageBlock(boolean damageBlock) {
        this.damageBlock = damageBlock;
    }

    public boolean isGiveItemBackOnRemoveWithCommand() {
        return giveItemBackOnRemoveWithCommand;
    }

    public void setGiveItemBackOnRemoveWithCommand(boolean giveItemBackOnRemoveWithCommand) {
        this.giveItemBackOnRemoveWithCommand = giveItemBackOnRemoveWithCommand;
    }

    public Animations getAnimations() {
        return animations;
    }

    public void setAnimations(Animations animations) {
        this.animations = animations;
    }

    public boolean isDamagePlayer() {
        return damagePlayer;
    }

    public void setDamagePlayer(boolean damagePlayer) {
        this.damagePlayer = damagePlayer;
    }

    public boolean isGamemodeControl() {
        return gamemodeControl;
    }

    public void setGamemodeControl(boolean gamemodeControl) {
        this.gamemodeControl = gamemodeControl;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ArrayList<String> getMonsters() {
        return monsters;
    }

    public void setMonsters(ArrayList<String> monsters) {
        this.monsters = monsters;
    }

    public Long getCheckAllTicks() {
        return checkAllTicks;
    }

    public void setCheckAllTicks(Long checkAllTicks) {
        this.checkAllTicks = checkAllTicks;
    }

    public int getMaxPerAccount() {
        return maxPerAccount;
    }

    public void setMaxPerAccount(int maxPerAccount) {
        this.maxPerAccount = maxPerAccount;
    }

    public Crafting getCraftingRecipe() {
        return craftingRecipe;
    }

    public void setCraftingRecipe(Crafting craftingRecipe) {
        this.craftingRecipe = craftingRecipe;
    }

    public HashMap<String, String> getLanguage() {
        return language;
    }

    public void setLanguage(HashMap<String, String> language) {
        this.language = language;
    }

    public static class Animations {
        private int untilKill = 10;
        private int untilBeamReset = 14;
        private int particleAmount = 30;
        private String particlesAtKill = "PORTAL";
        private String soundAtBeam = "ENTITY_GHAST_SHOOT";
        private String soundAtKill = "ENTITY_ENDERMAN_TELEPORT";
        private boolean showBeam = true;

        public int getParticleAmount() {
            return particleAmount;
        }

        public void setParticleAmount(int particleAmount) {
            this.particleAmount = particleAmount;
        }

        public int getUntilKill() {
            return untilKill;
        }

        public void setUntilKill(int untilKill) {
            this.untilKill = untilKill;
        }

        public int getUntilBeamReset() {
            return untilBeamReset;
        }

        public void setUntilBeamReset(int untilBeamReset) {
            this.untilBeamReset = untilBeamReset;
        }

        public String getParticlesAtKill() {
            return particlesAtKill;
        }

        public void setParticlesAtKill(String particlesAtKill) {
            this.particlesAtKill = particlesAtKill;
        }

        public String getSoundAtBeam() {
            return soundAtBeam;
        }

        public void setSoundAtBeam(String soundAtBeam) {
            this.soundAtBeam = soundAtBeam;
        }

        public String getSoundAtKill() {
            return soundAtKill;
        }

        public void setSoundAtKill(String soundAtKill) {
            this.soundAtKill = soundAtKill;
        }

        public boolean isShowBeam() {
            return showBeam;
        }

        public void setShowBeam(boolean showBeam) {
            this.showBeam = showBeam;
        }
    }

    public static class Crafting {

        private String topLeft = "";
        private String topMiddle = "tnt";
        private String topRight = "";
        private String middleLeft = "diamond";
        private String middleMiddle = "redstone_block";
        private String middleRight = "diamond";
        private String downLeft = "";
        private String downMiddle = "end_crystal";
        private String downRight = "";

        public String getTopLeft() {
            return topLeft;
        }

        public void setTopLeft(String topLeft) {
            this.topLeft = topLeft;
        }

        public String getTopMiddle() {
            return topMiddle;
        }

        public void setTopMiddle(String topMiddle) {
            this.topMiddle = topMiddle;
        }

        public String getTopRight() {
            return topRight;
        }

        public void setTopRight(String topRight) {
            this.topRight = topRight;
        }

        public String getMiddleLeft() {
            return middleLeft;
        }

        public void setMiddleLeft(String middleLeft) {
            this.middleLeft = middleLeft;
        }

        public String getMiddleMiddle() {
            return middleMiddle;
        }

        public void setMiddleMiddle(String middleMiddle) {
            this.middleMiddle = middleMiddle;
        }

        public String getMiddleRight() {
            return middleRight;
        }

        public void setMiddleRight(String middleRight) {
            this.middleRight = middleRight;
        }

        public String getDownLeft() {
            return downLeft;
        }

        public void setDownLeft(String downLeft) {
            this.downLeft = downLeft;
        }

        public String getDownMiddle() {
            return downMiddle;
        }

        public void setDownMiddle(String downMiddle) {
            this.downMiddle = downMiddle;
        }

        public String getDownRight() {
            return downRight;
        }

        public void setDownRight(String downRight) {
            this.downRight = downRight;
        }

    }
}