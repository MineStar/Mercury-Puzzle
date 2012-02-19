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

public class cmdCopy extends AbstractCommand {

    private PlayerManager playerManager;

    public cmdCopy(String pluginName, String syntax, String arguments, String node, PlayerManager playerManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Copy the selection";
        this.playerManager = playerManager;
    }

    @Override
    public void execute(String[] args, Player player) {
        // CHECK FOR RUNNING THREAD
        if (playerManager.hasRunningThread(player)) {
            ChatUtils.printError(player, pluginName, "You have a running thread. Please wait until it's finished.");
            return;
        }

        // COPY
        if (playerManager.doCopy(player)) {
            ChatUtils.printSuccess(player, pluginName, "Region copied!");
        } else {
            ChatUtils.printError(player, pluginName, "Something went wrong...");
        }
    }
}
