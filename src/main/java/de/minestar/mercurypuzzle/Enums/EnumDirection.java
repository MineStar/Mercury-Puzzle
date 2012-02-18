package de.minestar.mercurypuzzle.Enums;

public enum EnumDirection {
    NORMAL("Normal"),

    FLIP_X("FlipX"),

    FLIP_Z("FlipZ"),

    ROTATE_90("Rotate90"),

    ROTATE_180("Rotate180"),

    ROTATE_270("Rotate270");

    private String name;

    private EnumDirection(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EnumDirection fromName(String name) {
        for (EnumDirection dir : EnumDirection.values()) {
            if (name.equalsIgnoreCase(dir.getName()))
                return dir;
        }
        return null;
    }
}
