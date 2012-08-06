package de.minestar.mercurypuzzle.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.mercurypuzzle.Core.MercuryPuzzleCore;
import de.minestar.mercurypuzzle.Core.Settings;
import de.minestar.mercurypuzzle.Enums.EnumDirection;
import de.minestar.mercurypuzzle.Structure.Structure;
import de.minestar.mercurypuzzle.Structure.StructureBlock;
import de.minestar.mercurypuzzle.Threads.BlockUndoThread;

public class PlayerManager {
    private HashSet<String> runningThreads = new HashSet<String>();

    private HashMap<String, ArrayList<StructureBlock>> undoList = new HashMap<String, ArrayList<StructureBlock>>();
    private HashMap<String, Structure> structureList = new HashMap<String, Structure>();
    private HashMap<String, Selection> selectionList = new HashMap<String, Selection>();

    public boolean hasRunningThread(Player player) {
        return this.runningThreads.contains(player.getName());
    }

    public void removeRunningThread(String playerName) {
        this.runningThreads.remove(playerName);
    }

    public boolean doCopy(Player player) {
        if (!selectionList.containsKey(player.getName()))
            return false;

        Selection thisSelection = this.selectionList.get(player.getName());
        if (!thisSelection.isValid())
            return false;

        this.structureList.put(player.getName(), new Structure(player.getLocation(), thisSelection.getCorner1().toVector(), thisSelection.getCorner2().toVector()));
        return true;
    }

    public boolean setBlocks(Player player, int TypeID, byte SubID) {
        if (!selectionList.containsKey(player.getName()))
            return false;

        Selection thisSelection = this.selectionList.get(player.getName());
        if (!thisSelection.isValid())
            return false;

        Location minCorner = thisSelection.getMinCorner();
        Location maxCorner = thisSelection.getMaxCorner();

        // CREATE UNDO-LIST
        ArrayList<StructureBlock> undoBlocks = new ArrayList<StructureBlock>();
        Block thisBlock;
        World world = minCorner.getWorld();
        for (int y = minCorner.getBlockY(); y <= maxCorner.getBlockY(); y++) {
            for (int x = minCorner.getBlockX(); x <= maxCorner.getBlockX(); x++) {
                for (int z = minCorner.getBlockZ(); z <= maxCorner.getBlockZ(); z++) {
                    thisBlock = world.getBlockAt(x, y, z);
                    undoBlocks.add(new StructureBlock(thisBlock.getX(), thisBlock.getY(), thisBlock.getZ(), thisBlock.getTypeId(), thisBlock.getData()).updateExtraInformation(world, thisBlock.getX(), thisBlock.getY(), thisBlock.getZ()));
                }
            }
        }
        MercuryPuzzleCore.getInstance().getPlayerManager().addUndo(player.getName(), undoBlocks);

        // SET BLOCKS
        for (int y = minCorner.getBlockY(); y <= maxCorner.getBlockY(); y++) {
            for (int x = minCorner.getBlockX(); x <= maxCorner.getBlockX(); x++) {
                for (int z = minCorner.getBlockZ(); z <= maxCorner.getBlockZ(); z++) {
                    world.getBlockAt(x, y, z).setTypeIdAndData(TypeID, SubID, true);
                }
            }
        }
        return true;
    }

    public void addUndo(String playerName, ArrayList<StructureBlock> blockList) {
        this.undoList.put(playerName, blockList);
    }

    public boolean doPaste(Player player) {
        return this.doPaste(player, EnumDirection.NORMAL);
    }

    public boolean doPaste(Player player, EnumDirection direction) {
        if (!structureList.containsKey(player.getName()))
            return false;
        runningThreads.add(player.getName());
        this.structureList.get(player.getName()).pasteStructure(direction, player);
        return true;
    }

    public boolean doUndo(Player player) {
        if (!undoList.containsKey(player.getName()))
            return false;
        runningThreads.add(player.getName());
        BlockUndoThread thisThread = new BlockUndoThread(player, this.undoList.get(player.getName()));
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(MercuryPuzzleCore.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
        return true;
    }

    public boolean doSelection(Player player, Location location) {
        Selection thisSelection = this.selectionList.get(player.getName());
        if (thisSelection == null || thisSelection.getCorner2() != null) {
            thisSelection = new Selection(location);
            this.selectionList.put(player.getName(), thisSelection);
            return false;
        } else {
            thisSelection.setCorner2(location);
            return true;
        }
    }
}
