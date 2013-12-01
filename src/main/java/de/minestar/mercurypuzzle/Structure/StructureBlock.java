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
        Material mat = Material.matchMaterial(Integer.toString(TypeID));
        switch (mat) {
            case WALL_SIGN :
            case SIGN_POST :
                this.extraInformation = new InformationSign(((Sign) world.getBlockAt(x, y, z).getState()).getLines());
                break;
            case CHEST :
                this.extraInformation = new InformationChest(((Chest) world.getBlockAt(x, y, z).getState()).getBlockInventory());
                break;
            case DISPENSER :
                this.extraInformation = new InformationDispenser(((Dispenser) world.getBlockAt(x, y, z).getState()).getInventory());
                break;
            case FURNACE :
            case BURNING_FURNACE :
                this.extraInformation = new InformationFurnace(((Furnace) world.getBlockAt(x, y, z).getState()));
                break;
            case NOTE_BLOCK :
                this.extraInformation = new InformationNoteBlock(((NoteBlock) world.getBlockAt(x, y, z).getState()));
                break;
            default :
                break;
        }
        return this;
    }

    private void checkBlockType() {

        Material mat = Material.matchMaterial(Integer.toString(TypeID));
        switch (mat) {
        // Stairs
            case WOOD_STAIRS :
            case COBBLESTONE_STAIRS :
            case SMOOTH_STAIRS :
            case BRICK_STAIRS :
            case NETHER_BRICK_STAIRS :
                this.blockType = EnumBlockType.STAIR;
                break;

            // Pumpkins
            case PUMPKIN :
            case JACK_O_LANTERN :
                this.blockType = EnumBlockType.PUMPKIN;
                break;

            // Other stuff sticking together (GeMo has the clue)
            case LADDER :
            case WALL_SIGN :
            case FURNACE :
            case BURNING_FURNACE :
            case DISPENSER :
            case CHEST :
                this.blockType = EnumBlockType.WALLSIGN;
                break;

            // Rails
            case RAILS :
            case DETECTOR_RAIL :
            case POWERED_RAIL :
                this.blockType = EnumBlockType.RAIL;
                break;

            // Interactable Buttons
            case LEVER :
            case STONE_BUTTON :
                this.blockType = EnumBlockType.BUTTON;
                break;

            // Torches
            case TORCH :
            case REDSTONE_TORCH_ON :
            case REDSTONE_TORCH_OFF :
                this.blockType = EnumBlockType.TORCH;
                break;

            default :
                // do nothing
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
