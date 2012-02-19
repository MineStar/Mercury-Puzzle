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

import de.minestar.mercurypuzzle.Manager.PlayerManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;

public class cmdUndo extends AbstractCommand {

    private PlayerManager playerManager;

    public cmdUndo(String pluginName, String syntax, String arguments, String node, PlayerManager playerManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Undo the last paste";
        this.playerManager = playerManager;
    }

    @Override
    public void execute(String[] args, Player player) {
        // CHECK FOR RUNNING THREAD
        if (playerManager.hasRunningThread(player)) {
            ChatUtils.printError(player, pluginName, "You have a running thread. Please wait until it's finished.");
            return;
        }

        // UNDO
        if (playerManager.doUndo(player)) {
            ChatUtils.printSuccess(player, pluginName, "Undo started!");
        } else {
            ChatUtils.printError(player, pluginName, "Something went wrong...");
        }
    }
}
