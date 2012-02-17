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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.mercurypuzzle.Manager.PlayerManager;

public class cmdCopy extends Command {

    private PlayerManager playerManager;

    public cmdCopy(String pluginName, String syntax, String arguments, String node, PlayerManager playerManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Copy the selection";
        this.playerManager = playerManager;
    }

    @Override
    public void execute(String[] args, CommandSender sender) {
        // ONLY PLAYERS ARE ALLOWED
        if (!(sender instanceof Player)) {
            ChatUtils.printError(sender, pluginName, "This is only an ingame command.");
            return;
        }

        // GET PLAYER
        Player player = (Player) sender;
        // CHECK PERMISSION
        if (!UtilPermissions.playerCanUseCommand(player, "wefake.copy")) {
            ChatUtils.printError(player, pluginName, "You are not allowed to use this command.");
            return;
        }
        
        // CHECK FOR RUNNING THREAD
        if(playerManager.hasRunningThread(player)) {
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
