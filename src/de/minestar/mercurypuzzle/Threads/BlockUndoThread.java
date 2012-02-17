package de.minestar.mercurypuzzle.Threads;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.mercurypuzzle.Core.MercuryPuzzle;
import de.minestar.mercurypuzzle.Core.Settings;
import de.minestar.mercurypuzzle.Units.StructureBlock;

public class BlockUndoThread implements Runnable {

    private final World world;
    private int TaskID = -9999;
    private int counter = 0;
    private final ArrayList<StructureBlock> blockList, queuedBlocks;
    private final String playerName;
    private final boolean firstUndo;

    public BlockUndoThread(Player player, ArrayList<StructureBlock> blockList) {
        this.world = player.getWorld();
        this.blockList = blockList;
        this.playerName = player.getName();
        this.queuedBlocks = new ArrayList<StructureBlock>();
        this.firstUndo = true;
    }

    public BlockUndoThread(World world, String playerName, ArrayList<StructureBlock> blockList, boolean firstUndo) {
        this.world = world;
        this.blockList = blockList;
        this.playerName = playerName;
        this.queuedBlocks = new ArrayList<StructureBlock>();
        this.firstUndo = firstUndo;
    }

    public void initTask(int TaskID) {
        this.TaskID = TaskID;
    }

    @Override
    public void run() {
        if (TaskID == -9999)
            return;
        StructureBlock thisBlock = null;
        Player thisPlayer = Bukkit.getPlayerExact(this.playerName);
        Block thisWorldBlock;
        int index = 0;
        for (int i = 0; i < Settings.getMaxBlockxReplaceAtOnce(); i++) {
            index = blockList.size() - 1 - counter;
            if (index >= 0) {
                thisBlock = blockList.get(index);
                thisWorldBlock = world.getBlockAt(thisBlock.getX(), thisBlock.getY(), thisBlock.getZ());
                if (BlockUtils.isComplexBlock(thisBlock.getTypeID()) || !firstUndo) {
                    if (!firstUndo || thisBlock.getTypeID() != thisWorldBlock.getTypeId() || thisBlock.getSubID() != thisWorldBlock.getData() || BlockUtils.isComplexBlock(thisWorldBlock.getTypeId())) {
                        thisWorldBlock.setTypeIdAndData(thisBlock.getTypeID(), thisBlock.getSubID(), false);
                    }
                } else {
                    this.queuedBlocks.add(thisBlock);
                }
            }
            counter++;
            if (counter >= blockList.size()) {
                // UNDO QUEUED BLOCKS
                BlockUndoThread thisThread = new BlockUndoThread(world, this.playerName, this.queuedBlocks, false);
                thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(MercuryPuzzle.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));

                // UPDATE PHYSICS
                net.minecraft.server.World nativeWorld = ((CraftWorld) world).getHandle();
                for (int j = 0; j < blockList.size(); j++) {
                    thisBlock = blockList.get(j);
                    thisWorldBlock = world.getBlockAt(thisBlock.getX(), thisBlock.getY(), thisBlock.getZ());
                    nativeWorld.notify(thisBlock.getX(), thisBlock.getY(), thisBlock.getZ());
                    nativeWorld.applyPhysics(thisBlock.getX(), thisBlock.getY(), thisBlock.getZ(), thisWorldBlock.getTypeId());
                }

                // CANCEL TASK & PRINT INFO
                if (!firstUndo)
                    MercuryPuzzle.getInstance().getPlayerManager().removeRunningThread(this.playerName);

                Bukkit.getScheduler().cancelTask(this.TaskID);
                if (thisPlayer != null) {
                    ChatUtils.printSuccess(thisPlayer, MercuryPuzzle.getInstance().getDescription().getName(), "Undo done!");
                }
                break;
            }
        }

        if (thisPlayer != null) {
            ChatUtils.printInfo(thisPlayer, MercuryPuzzle.getInstance().getDescription().getName(), ChatColor.GRAY, counter + " blocks of " + this.blockList.size() + " undone.");
        }
        thisBlock = null;
    }
}
