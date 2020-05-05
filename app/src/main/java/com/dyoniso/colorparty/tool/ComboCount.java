package com.dyoniso.colorparty.tool;

import java.util.concurrent.atomic.AtomicInteger;

public class ComboCount {
    private String zName;
    private AtomicInteger zCount = new AtomicInteger(1);

    public void addCombo() {
        zCount.getAndAdd(1);
    }

    public void removeCombo() {
        zCount.set(1);
    }

    public int getCount() {
        return zCount.get();
    }

    public String getComboName() {
        switch (getCount()) {
            case 2:
                return getCount() + "x, Great!";
            case 3:
                return getCount() + "x, Good";
            case 4:
                return getCount() + "x, Invincible";
            case 5:
                return getCount() + "x, Invincible+";
            case 6:
                return getCount() + "x, Invincible++";
            case 7:
                return getCount() + "x, Invincible+++";
            case 8:
                return getCount() + "x, Color Master";
            case 9:
                return getCount() + "x, Color Master+";
            case 10:
                return getCount() + "x, Color Master++";
            default:
                if (getCount() > 10) {
                    return getCount()+"x, Color Master+++";
                } else {
                    return "Ok";
                }
        }
    }
}
