package de.minestar.mercurypuzzle.Structure;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import de.minestar.mercurypuzzle.Enums.EnumBlockType;
import de.minestar.mercurypuzzle.StructureInformation.InformationChest;
import de.minestar.mercurypuzzle.StructureInformation.InformationDispenser;
import de.minestar.mercurypuzzle.StructureInformation.InformationFurnace;
import de.minestar.mercurypuzzle.StructureInformation.InformationNoteBlock;
import de.minestar.mercurypuzzle.StructureInformation.InformationSign;

public class StructureBlock {
    private int x, y, z;
    private final int TypeID;
    private byte SubID;
    private EnumBlockType blockType;
    private StructureInformation extraInformation;

    public StructureBlock(int x, int y, int z, int TypeID, byte SubID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.TypeID = TypeID;
        this.SubID = SubID;
        this.blockType = EnumBlockType.NORMAL;
        this.checkBlockType();
    }

    private StructureBlock(int x, int y, int z, int TypeID, byte SubID, StructureInformation extraInformation) {
        this(x, y, z, TypeID, SubID);
        this.extraInformation = extraInformation;
    }

    public StructureBlock updateExtraInformation(World world, int x, int y, int z) {
        if (this.TypeID == Material.WALL_SIGN.getId() || this.TypeID == Material.SIGN_POST.getId()) {
            this.extraInformation = new InformationSign(((Sign) world.getBlockAt(x, y, z).getState()).getLines());
        } else if (this.TypeID == Material.CHEST.getId()) {
            this.extraInformation = new InformationChest(((Chest) world.getBlockAt(x, y, z).getState()).getInventory());
        } else if (this.TypeID == Material.DISPENSER.getId()) {
            this.extraInformation = new InformationDispenser(((Dispenser) world.getBlockAt(x, y, z).getState()).getInventory());
        } else if (this.TypeID == Material.FURNACE.getId() || this.TypeID == Material.BURNING_FURNACE.getId()) {
            this.extraInformation = new InformationFurnace(((Furnace) world.getBlockAt(x, y, z).getState()));
        } else if (this.TypeID == Material.NOTE_BLOCK.getId()) {
            this.extraInformation = new InformationNoteBlock(((NoteBlock) world.getBlockAt(x, y, z).getState()));
        }
        return this;
    }

    private void checkBlockType() {
        if (this.TypeID == Material.WOOD_STAIRS.getId() || this.TypeID == Material.COBBLESTONE_STAIRS.getId() || this.TypeID == Material.SMOOTH_STAIRS.getId() || this.TypeID == Material.BRICK_STAIRS.getId() || this.TypeID == Material.NETHER_BRICK_STAIRS.getId()) {
            /** STAIRS */
            this.blockType = EnumBlockType.STAIR;
        } else if (this.TypeID == Material.PUMPKIN.getId() || this.TypeID == Material.JACK_O_LANTERN.getId()) {
            /** PUMPKINS */
            this.blockType = EnumBlockType.PUMPKIN;
        } else if (this.TypeID == Material.LADDER.getId() || this.TypeID == Material.WALL_SIGN.getId() || this.TypeID == Material.FURNACE.getId() || this.TypeID == Material.BURNING_FURNACE.getId() || this.TypeID == Material.DISPENSER.getId() || this.TypeID == Material.CHEST.getId()) {
            /** LADDER , WALLSIGN, FURNACE, CHEST, DISPENSER */
            this.blockType = EnumBlockType.WALLSIGN;
        } else if (this.TypeID == Material.RAILS.getId() || this.TypeID == Material.DETECTOR_RAIL.getId() || this.TypeID == Material.POWERED_RAIL.getId()) {
            /** RAILS */
            this.blockType = EnumBlockType.RAIL;
        } else if (this.TypeID == Material.LEVER.getId() || this.TypeID == Material.STONE_BUTTON.getId()) {
            /** LEVER , BUTTON */
            this.blockType = EnumBlockType.BUTTON;
        } else if (this.TypeID == Material.TORCH.getId() || this.TypeID == Material.REDSTONE_TORCH_ON.getId() || this.TypeID == Material.REDSTONE_TORCH_OFF.getId()) {
            /** TORCHES */
            this.blockType = EnumBlockType.TORCH;
        }
    }

    // ///////////////////////////////////
    //
    // ROTATE METHODS
    //
    // ///////////////////////////////////

    public void flipX(Vector sizeVector) {
        this.x = -this.x;
        this.flipBlockX();
    }

    public void flipZ(Vector sizeVector) {
        this.z = -this.z;
        this.flipBlockZ();
    }

    public void rotate180(Vector sizeVector) {
        this.flipX(sizeVector);
        this.flipZ(sizeVector);
    }

    public void rotate270(Vector sizeVector) {
        int oldX = this.x;
        this.x = this.z;
        this.z = -oldX;
        this.rotateBlock270();
    }

    public void rotate90(Vector sizeVector) {
        int oldX = this.x;
        this.x = -this.z;
        this.z = oldX;
        this.rotateBlock90();
    }

    // ///////////////////////////////////
    //
    // BLOCKRELATED ROTATE METHODS
    //
    // ///////////////////////////////////

    private void rotateBlock90() {
        // EAST <> WEST
        if (this.blockType == EnumBlockType.NORMAL) {
            /** NORMAL BLOCK */
            return;
        } else if (this.blockType == EnumBlockType.STAIR) {
            /** STAIRS */
            if (this.SubID == 0x0) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x1) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x1;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x0;
                return;
            }
        } else if (this.blockType == EnumBlockType.PUMPKIN) {
            /** PUMPKINS */
            if (this.SubID == 0x0) {
                this.SubID = 0x1;
                return;
            }
            if (this.SubID == 0x1) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x0;
                return;
            }
        } else if (this.blockType == EnumBlockType.WALLSIGN) {
            /** LADDER , WALLSIGN, FURNACE, CHEST, DISPENSER */
            if (this.SubID == 0x2) {
                this.SubID = 0x5;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x5) {
                this.SubID = 0x3;
                return;
            }
        } else if (this.blockType == EnumBlockType.RAIL) {
            /** RAILS */
            if (this.SubID == 0x0) {
                this.SubID = 0x1;
                return;
            }
            if (this.SubID == 0x1) {
                this.SubID = 0x0;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x5;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x5) {
                this.SubID = 0x3;
                return;
            }
            // CORNERS
            if (this.SubID == 0x6) {
                this.SubID = 0x7;
                return;
            }
            if (this.SubID == 0x7) {
                this.SubID = 0x8;
                return;
            }
            if (this.SubID == 0x8) {
                this.SubID = 0x9;
                return;
            }
            if (this.SubID == 0x9) {
                this.SubID = 0x6;
                return;
            }
        } else if (this.blockType == EnumBlockType.BUTTON) {
            /** LEVER , BUTTON */
            if (this.SubID == 0x1) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x1;
                return;
            }
        } else if (this.blockType == EnumBlockType.TORCH) {
            /** TORCHES */
            if (this.SubID == 0x1) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x1;
                return;
            }
        }
        // TODO: SIGN-POSTS
    }

    private void rotateBlock270() {
        // EAST <> WEST
        if (this.blockType == EnumBlockType.NORMAL) {
            /** NORMAL BLOCK */
            return;
        } else if (this.blockType == EnumBlockType.STAIR) {
            /** STAIRS */
            if (this.SubID == 0x0) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x1) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x0;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x1;
                return;
            }
        } else if (this.blockType == EnumBlockType.PUMPKIN) {
            /** PUMPKINS */
            if (this.SubID == 0x0) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x1) {
                this.SubID = 0x0;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x1;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x2;
                return;
            }
        } else if (this.blockType == EnumBlockType.WALLSIGN) {
            /** LADDER , WALLSIGN, FURNACE, CHEST, DISPENSER */
            if (this.SubID == 0x2) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x5;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x5) {
                this.SubID = 0x2;
                return;
            }
        } else if (this.blockType == EnumBlockType.RAIL) {
            /** RAILS */
            if (this.SubID == 0x0) {
                this.SubID = 0x1;
                return;
            }
            if (this.SubID == 0x1) {
                this.SubID = 0x0;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x5;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x5) {
                this.SubID = 0x2;
                return;
            }
            // CORNERS
            if (this.SubID == 0x6) {
                this.SubID = 0x9;
                return;
            }
            if (this.SubID == 0x7) {
                this.SubID = 0x6;
                return;
            }
            if (this.SubID == 0x8) {
                this.SubID = 0x7;
                return;
            }
            if (this.SubID == 0x9) {
                this.SubID = 0x8;
                return;
            }
        } else if (this.blockType == EnumBlockType.BUTTON) {
            /** LEVER , BUTTON */
            if (this.SubID == 0x1) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x1;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x2;
                return;
            }
        } else if (this.blockType == EnumBlockType.TORCH) {
            /** TORCHES */
            if (this.SubID == 0x1) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x1;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x2;
                return;
            }
        }
        // TODO: SIGN-POSTS
    }

    private void flipBlockX() {
        // EAST <> WEST
        if (this.blockType == EnumBlockType.NORMAL) {
            /** NORMAL BLOCK */
            return;
        } else if (this.blockType == EnumBlockType.STAIR) {
            /** STAIRS */
            if (this.SubID == 0x0) {
                this.SubID = 0x1;
                return;
            }
            if (this.SubID == 0x1) {
                this.SubID = 0x0;
                return;
            }
        } else if (this.blockType == EnumBlockType.PUMPKIN) {
            /** PUMPKINS */
            if (this.SubID == 0x1) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x1;
                return;
            }
        } else if (this.blockType == EnumBlockType.WALLSIGN) {
            /** LADDER , WALLSIGN, FURNACE, CHEST, DISPENSER */
            if (this.SubID == 0x4) {
                this.SubID = 0x5;
                return;
            }
            if (this.SubID == 0x5) {
                this.SubID = 0x4;
                return;
            }
        } else if (this.blockType == EnumBlockType.RAIL) {
            /** RAILS */
            if (this.SubID == 0x2) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x6) {
                this.SubID = 0x7;
                return;
            }
            if (this.SubID == 0x7) {
                this.SubID = 0x6;
                return;
            }
        } else if (this.blockType == EnumBlockType.BUTTON) {
            /** LEVER , BUTTON */
            if (this.SubID == 0x1) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x1;
                return;
            }
        } else if (this.blockType == EnumBlockType.TORCH) {
            /** TORCHES */
            if (this.SubID == 0x1) {
                this.SubID = 0x2;
                return;
            }
            if (this.SubID == 0x2) {
                this.SubID = 0x1;
                return;
            }
        }
        // TODO: SIGN-POSTS
    }

    private void flipBlockZ() {
        // SOUTH <> NORTH
        if (this.blockType == EnumBlockType.NORMAL) {
            /** NORMAL BLOCK */
            return;
        } else if (this.blockType == EnumBlockType.STAIR) {
            /** STAIRS */
            if (this.SubID == 0x2) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x2;
                return;
            }
        } else if (this.blockType == EnumBlockType.PUMPKIN) {
            /** PUMPKINS */
            if (this.SubID == 0x2) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x2;
                return;
            }
        } else if (this.blockType == EnumBlockType.WALLSIGN) {
            /** LADDER , WALLSIGN, FURNACE, CHEST, DISPENSER */
            if (this.SubID == 0x2) {
                this.SubID = 0x3;
                return;
            }
            if (this.SubID == 0x3) {
                this.SubID = 0x2;
                return;
            }
        } else if (this.blockType == EnumBlockType.RAIL) {
            /** RAILS */
            if (this.SubID == 0x4) {
                this.SubID = 0x5;
                return;
            }
            if (this.SubID == 0x5) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x8) {
                this.SubID = 0x9;
                return;
            }
            if (this.SubID == 0x9) {
                this.SubID = 0x8;
                return;
            }
        } else if (this.blockType == EnumBlockType.BUTTON) {
            /** LEVER , BUTTON */
            if (this.SubID == 0x3) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x3;
                return;
            }
        } else if (this.blockType == EnumBlockType.TORCH) {
            /** TORCHES */
            if (this.SubID == 0x3) {
                this.SubID = 0x4;
                return;
            }
            if (this.SubID == 0x4) {
                this.SubID = 0x3;
                return;
            }
        }
        // TODO: SIGN-POSTS
    }

    // ///////////////////////////////////
    //
    // GETTER
    //
    // ///////////////////////////////////

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getTypeID() {
        return TypeID;
    }

    public byte getSubID() {
        return SubID;
    }

    public StructureInformation getExtraInformation() {
        return extraInformation;
    }

    public StructureBlock clone() {
        return new StructureBlock(x, y, z, TypeID, SubID, this.extraInformation);
    }
}
