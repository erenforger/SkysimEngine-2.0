/*
 * Decompiled with CFR 0.152.
 */
package vn.giakhanhvn.skysim.item.armor.eleganttux;

import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.armor.LeatherArmorStatistics;

public class ElegantTuxBoots
implements LeatherArmorStatistics,
MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Elegant Tuxedo Oxfords";
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
    public double getBaseCritDamage() {
        return 0.5;
    }

    @Override
    public double getBaseIntelligence() {
        return 100.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.1;
    }

    @Override
    public int getColor() {
        return 0x191919;
    }
}

