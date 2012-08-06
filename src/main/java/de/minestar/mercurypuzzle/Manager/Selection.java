package de.minestar.mercurypuzzle.Manager;

import org.bukkit.Location;

public class Selection {
    private Location corner1 = null, corner2 = null;

    public Selection(Location corner1) {
        this.corner1 = corner1;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public Location getMinCorner() {
        Location loc = corner1.clone();
        loc.setX(Math.min(corner1.getBlockX(), corner2.getBlockX()));
        loc.setY(Math.min(corner1.getBlockY(), corner2.getBlockY()));
        loc.setZ(Math.min(corner1.getBlockZ(), corner2.getBlockZ()));
        return loc;
    }

    public Location getMaxCorner() {
        Location loc = corner1.clone();
        loc.setX(Math.max(corner1.getBlockX(), corner2.getBlockX()));
        loc.setY(Math.max(corner1.getBlockY(), corner2.getBlockY()));
        loc.setZ(Math.max(corner1.getBlockZ(), corner2.getBlockZ()));
        return loc;
    }

    public boolean isValid() {
        return this.corner1 != null && this.corner2 != null;
    }
}
