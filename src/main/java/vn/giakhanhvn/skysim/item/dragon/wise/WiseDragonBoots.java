/*
 * Decompiled with CFR 0.152.
 */
package vn.giakhanhvn.skysim.item.dragon.wise;

import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.armor.LeatherArmorStatistics;

public class WiseDragonBoots
implements MaterialFunction,
LeatherArmorStatistics {
    @Override
    public double getBaseIntelligence() {
        return 75.0;
    }

    @Override
    public double getBaseHealth() {
        return 60.0;
    }

    @Override
    public double getBaseDefense() {
        return 90.0;
    }

    @Override
    public int getColor() {
        return 2748649;
    }

    @Override
    public String getDisplayName() {
        return "Wise Dragon Boots";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.BOOTS;
    }

    @Override
    public String getLore() {
        return null;
    }
}

