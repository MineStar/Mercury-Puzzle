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

import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.mercurypuzzle.Core.MercuryPuzzleCore;
import de.minestar.mercurypuzzle.Enums.EnumDirection;
import de.minestar.mercurypuzzle.Manager.PlayerManager;
import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class cmdPaste extends AbstractExtendedCommand {

    private PlayerManager playerManager;

    public cmdPaste(String pluginName, String syntax, String arguments, String node, PlayerManager playerManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Paste the structure";
        this.playerManager = playerManager;
    }

    @Override
    public void execute(String[] args, Player player) {
        if (!MercuryPuzzleCore.getInstance().isPluginEnabled()) {
            PlayerUtils.sendError(player, "NOPE, Chuck Testa!");
            return;
        }

        // CHECK FOR RUNNING THREAD
        if (playerManager.hasRunningThread(player)) {
            ChatUtils.printError(player, pluginName, "You have a running thread. Please wait until it's finished.");
            return;
        }

        EnumDirection direction = EnumDirection.NORMAL;
        if (args.length == 1) {
            direction = EnumDirection.fromName(args[0]);
            if (direction == null) {
                ChatUtils.printError(player, pluginName, "Wrong direction-argument.");
                return;
            }
        }

        // PASTE
        playerManager.doPaste(player, direction);
    }
}
