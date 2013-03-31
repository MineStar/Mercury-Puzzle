/*
 * Copyright (C) 2011-2012 GeMo
 * 
 * This file is part of FalseBook and FalseBookChat.
 * 
 * FalseBookChat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * FalseBookChat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with FalseBookChat.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.mercurypuzzle.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.mercurypuzzle.Core.MercuryPuzzleCore;
import de.minestar.mercurypuzzle.Manager.PlayerManager;
import de.minestar.mercurypuzzle.Manager.Selection;
import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class cmdFixSteps extends AbstractExtendedCommand {

    private PlayerManager playerManager;

    public cmdFixSteps(String pluginName, String syntax, String arguments, String node, PlayerManager playerManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Set the blocks to a specific blocktype";
        this.playerManager = playerManager;
    }

    @Override
    public void execute(String[] args, Player player) {
        if (!MercuryPuzzleCore.getInstance().isPluginEnabled()) {
            PlayerUtils.sendError(player, "NOPE, Chuck Testa!");
            return;
        }

        Selection selection = this.playerManager.getSelection(player.getName());
        if (selection == null || !selection.isValid()) {
            PlayerUtils.sendError(player, "Selection is not valid!");
            return;
        }

        int typeID = 43;
        byte oldSubID = 6;
        byte oldSubID2 = 6 + 8;
        byte newSubID = 7;
        byte newSubID2 = 7 + 8;

        Location min = selection.getMinCorner();
        Location max = selection.getMaxCorner();

        World world = min.getWorld();
        Block block;

        int count = 0;
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                    block = world.getBlockAt(x, y, z);
                    if (block.getTypeId() != typeID && block.getTypeId() != typeID + 1) {
                        continue;
                    }

                    if (block.getData() == oldSubID) {
                        block.setTypeIdAndData(block.getTypeId(), newSubID, false);
                        count++;
                    }
                    if (block.getData() == oldSubID2) {
                        block.setTypeIdAndData(block.getTypeId(), newSubID2, false);
                        count++;
                    }
                }
            }
        }

        PlayerUtils.sendSuccess(player, "Replaced blocks: " + count + "!");
    }
}
