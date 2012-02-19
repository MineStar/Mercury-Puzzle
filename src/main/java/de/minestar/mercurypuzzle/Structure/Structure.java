package de.minestar.mercurypuzzle.Structure;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.minestar.mercurypuzzle.Core.Core;
import de.minestar.mercurypuzzle.Core.Settings;
import de.minestar.mercurypuzzle.Enums.EnumDirection;
import de.minestar.mercurypuzzle.Threads.BlockCreationThread;

public class Structure {
    private ArrayList<StructureBlock> BlockList;
    private Vector distanceVector, sizeVector;

    public Structure(Location pastePoint, Vector vector1, Vector vector2) {
        Vector minVector = new Vector(Math.min(vector1.getBlockX(), vector2.getBlockX()), Math.min(vector1.getBlockY(), vector2.getBlockY()), Math.min(vector1.getBlockZ(), vector2.getBlockZ()));
        Vector maxVector = new Vector(Math.max(vector1.getBlockX(), vector2.getBlockX()), Math.max(vector1.getBlockY(), vector2.getBlockY()), Math.max(vector1.getBlockZ(), vector2.getBlockZ()));

        this.sizeVector = new Vector(maxVector.getBlockX() - minVector.getBlockX(), maxVector.getBlockY() - minVector.getBlockY(), maxVector.getBlockZ() - minVector.getBlockZ());
        this.distanceVector = new Vector(pastePoint.getBlockX() - minVector.getBlockX(), pastePoint.getBlockY() - minVector.getBlockY(), pastePoint.getBlockZ() - minVector.getBlockZ());

        this.BlockList = new ArrayList<StructureBlock>();
        World world = pastePoint.getWorld();
        for (int y = minVector.getBlockY(); y <= maxVector.getBlockY(); y++) {
            for (int x = minVector.getBlockX(); x <= maxVector.getBlockX(); x++) {
                for (int z = minVector.getBlockZ(); z <= maxVector.getBlockZ(); z++) {
                    BlockList.add(new StructureBlock(x - pastePoint.getBlockX(), y - pastePoint.getBlockY(), z - pastePoint.getBlockZ(), world.getBlockTypeIdAt(x, y, z), world.getBlockAt(x, y, z).getData()).updateExtraInformation(pastePoint.getWorld(), x, y, z));
                }
            }
        }
    }

    public void pasteStructure(EnumDirection direction, Player player) {
        if (this.BlockList == null)
            return;

        // INIT LIST
        ArrayList<StructureBlock> pasteList = this.BlockList;

        // FLIP X || ROTATE 180
        if (direction == EnumDirection.FLIP_X || direction == EnumDirection.ROTATE_180) {
            pasteList = this.flipX(pasteList);
        }

        // FLIP Z || ROTATE 180
        if (direction == EnumDirection.FLIP_Z || direction == EnumDirection.ROTATE_180) {
            pasteList = this.flipZ(pasteList);
        }

        // ROTATE 90
        if (direction == EnumDirection.ROTATE_90) {
            pasteList = this.rotate90();
        }

        // ROTATE 270
        if (direction == EnumDirection.ROTATE_270) {
            pasteList = this.rotate270();
        }

        BlockCreationThread thisThread = new BlockCreationThread(player, pasteList);
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
    }

    private ArrayList<StructureBlock> flipX(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(BlockList);
        for (StructureBlock block : newList) {
            block.flipX(this.sizeVector);
        }
        return newList;
    }

    private ArrayList<StructureBlock> flipZ(ArrayList<StructureBlock> BlockList) {
        if (BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(BlockList);
        for (StructureBlock block : newList) {
            block.flipZ(this.sizeVector);
        }
        return newList;
    }

    private ArrayList<StructureBlock> rotate270() {
        if (this.BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(this.BlockList);
        for (StructureBlock block : newList) {
            block.rotate270(sizeVector);
        }
        return newList;
    }

    private ArrayList<StructureBlock> rotate90() {
        if (this.BlockList == null)
            return null;
        ArrayList<StructureBlock> newList = this.cloneList(this.BlockList);
        for (StructureBlock block : newList) {
            block.rotate90(sizeVector);
        }
        return newList;
    }

    private ArrayList<StructureBlock> cloneList(ArrayList<StructureBlock> list) {
        ArrayList<StructureBlock> clone = new ArrayList<StructureBlock>(list.size());
        for (StructureBlock item : list)
            clone.add(item.clone());
        return clone;
    }

    public Vector getDistanceVector() {
        return distanceVector;
    }
}
