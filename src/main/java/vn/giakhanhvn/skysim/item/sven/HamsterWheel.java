/*
 * Decompiled with CFR 0.152.
 */
package vn.giakhanhvn.skysim.item.sven;

import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.MaterialStatistics;
import vn.giakhanhvn.skysim.item.Rarity;

public class HamsterWheel
implements MaterialStatistics,
MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Hamster Wheel";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }
}

