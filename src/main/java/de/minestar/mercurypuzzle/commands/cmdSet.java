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

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.BlockUtils;

import de.minestar.mercurypuzzle.Core.MercuryPuzzleCore;
import de.minestar.mercurypuzzle.Manager.PlayerManager;
import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class cmdSet extends AbstractExtendedCommand {

    private PlayerManager playerManager;

    public cmdSet(String pluginName, String syntax, String arguments, String node, PlayerManager playerManager) {
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

        int TypeID = -1;
        byte SubID = 0;

        // GET TYPEID
        try {
            TypeID = Integer.valueOf(args[0]);
        } catch (Exception e) {
            TypeID = BlockUtils.getItemIDFromName(args[0]);
            if (TypeID < 0) {
                PlayerUtils.sendError(player, "TypeID not valid!");
                return;
            }
        }

        // GET SUBID
        if (args.length > 1) {
            try {
                SubID = Byte.valueOf(args[1]);
            } catch (Exception e) {
                PlayerUtils.sendError(player, "SubID not valid!");
                return;
            }
        }

        Material material = Material.getMaterial(TypeID);
        if (material == null) {
            PlayerUtils.sendError(player, "Material not found!");
            return;
        }

        if (!material.isBlock()) {
            PlayerUtils.sendError(player, "Material is not a block!");
            return;
        }

        // SET BLOCKS
        this.playerManager.prepareSetCommand(player, TypeID, SubID);
        playerManager.setBlocks(player, TypeID, SubID);
    }
}
