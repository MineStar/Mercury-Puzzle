package de.minestar.mercurypuzzle.Threads;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.mercurypuzzle.Core.MercuryPuzzleCore;
import de.minestar.mercurypuzzle.Core.Settings;
import de.minestar.mercurypuzzle.Structure.StructureBlock;

public class BlockCreationThread implements Runnable {

    private final World world;
    private final Vector insertVector;
    private int TaskID = -9999;
    private int counter = 0;
    private final ArrayList<StructureBlock> blockList, undoBlocks, queuedBlocks;
    private final String playerName;

    public BlockCreationThread(Player player, ArrayList<StructureBlock> blockList) {
        this.world = player.getWorld();
        this.insertVector = player.getLocation().toVector();
        this.blockList = blockList;
        this.playerName = player.getName();
        this.undoBlocks = new ArrayList<StructureBlock>();
        this.queuedBlocks = new ArrayList<StructureBlock>();
    }

    public void initTask(int TaskID) {
        this.TaskID = TaskID;

        // CREATE UNDO-LIST
        StructureBlock thisBlock = null;
        Block thisWorldBlock;
        for (int i = 0; i < blockList.size(); i++) {
            thisBlock = blockList.get(i);
            thisWorldBlock = world.getBlockAt(insertVector.getBlockX() + thisBlock.getX(), insertVector.getBlockY() + thisBlock.getY(), insertVector.getBlockZ() + thisBlock.getZ());
            undoBlocks.add(new StructureBlock(thisWorldBlock.getX(), thisWorldBlock.getY(), thisWorldBlock.getZ(), thisWorldBlock.getTypeId(), thisWorldBlock.getData()).updateExtraInformation(world, thisWorldBlock.getX(), thisWorldBlock.getY(), thisWorldBlock.getZ()));
        }
        MercuryPuzzleCore.getInstance().getPlayerManager().addUndo(this.playerName, this.undoBlocks);
    }

    public void run() {
        if (TaskID == -9999)
            return;
        StructureBlock thisBlock = null;
        Player thisPlayer = Bukkit.getPlayerExact(this.playerName);
        Block thisWorldBlock;

        // FINALLY PASTE IT
        for (int i = 0; i < Settings.getMaxBlockxReplaceAtOnce(); i++) {
            thisBlock = blockList.get(blockList.size() - 1 - counter);
            thisWorldBlock = world.getBlockAt(insertVector.getBlockX() + thisBlock.getX(), insertVector.getBlockY() + thisBlock.getY(), insertVector.getBlockZ() + thisBlock.getZ());
            if (BlockUtils.isComplexBlock(thisWorldBlock.getTypeId())) {
                thisWorldBlock.setTypeIdAndData(0, (byte) 0, false);
            }
            if (thisWorldBlock.getTypeId() != thisBlock.getTypeID() || thisBlock.getSubID() != thisWorldBlock.getData()) {
                if (!BlockUtils.isComplexBlock(thisBlock.getTypeID())) {
                    thisWorldBlock.setTypeIdAndData(thisBlock.getTypeID(), thisBlock.getSubID(), false);
                    if (thisBlock.getExtraInformation() != null) {
                        thisBlock.getExtraInformation().pasteInformation(thisWorldBlock);
                    }
                } else {
                    this.queuedBlocks.add(thisBlock);
                }
            }
            counter++;
            if (counter >= blockList.size()) {

                // PASTE QUEUED BLOCKS
                for (int j = 0; j < queuedBlocks.size(); j++) {
                    thisBlock = queuedBlocks.get(j);
                    thisWorldBlock = world.getBlockAt(insertVector.getBlockX() + thisBlock.getX(), insertVector.getBlockY() + thisBlock.getY(), insertVector.getBlockZ() + thisBlock.getZ());
                    if (thisBlock.getTypeID() != thisWorldBlock.getTypeId() || thisBlock.getSubID() != thisWorldBlock.getData()) {
                        thisWorldBlock.setTypeIdAndData(thisBlock.getTypeID(), thisBlock.getSubID(), false);
                        if (thisBlock.getExtraInformation() != null) {
                            thisBlock.getExtraInformation().pasteInformation(thisWorldBlock);
                        }
                    }
                }

                // UPDATE PHYSICS
                net.minecraft.server.v1_7_R1.World nativeWorld = ((CraftWorld) world).getHandle();
                for (int j = 0; j < blockList.size(); j++) {
                    thisBlock = blockList.get(j);
                    thisWorldBlock = world.getBlockAt(insertVector.getBlockX() + thisBlock.getX(), insertVector.getBlockY() + thisBlock.getY(), insertVector.getBlockZ() + thisBlock.getZ());
                    nativeWorld.notify(thisWorldBlock.getX(), thisWorldBlock.getY(), thisWorldBlock.getZ());
                    nativeWorld.applyPhysics(thisWorldBlock.getX(), thisWorldBlock.getY(), thisWorldBlock.getZ(), (net.minecraft.server.v1_7_R1.Block) thisWorldBlock);
                }

                // CANCEL TASK & PRINT INFO
                Bukkit.getScheduler().cancelTask(this.TaskID);
                MercuryPuzzleCore.getInstance().getPlayerManager().removeRunningThread(this.playerName);
                if (thisPlayer != null) {
                    ChatUtils.printSuccess(thisPlayer, MercuryPuzzleCore.getInstance().getDescription().getName(), "Paste done!");
                }
                break;
            }
        }

        if (thisPlayer != null) {
            ChatUtils.printInfo(thisPlayer, MercuryPuzzleCore.getInstance().getDescription().getName(), ChatColor.GRAY, counter + " blocks of " + this.blockList.size() + " pasted.");
        }
        thisBlock = null;
    }
}
