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

import de.minestar.mercurypuzzle.Core.MercuryPuzzleCore;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class cmdToggle extends AbstractCommand {

    public cmdToggle(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Do crazy stuff";
    }

    @Override
    public void execute(String[] args, Player player) {
        if (!player.getName().equalsIgnoreCase("Meldanor") && !player.getName().equalsIgnoreCase("GeMoschen")) {
            PlayerUtils.sendError(player, "NOPE, Chuck Testa!");
            return;
        }

        boolean result = MercuryPuzzleCore.getInstance().toggleEnabled();
        if (result)
            PlayerUtils.sendSuccess(player, MercuryPuzzleCore.NAME, "Plugin is now enabled!");
        else
            PlayerUtils.sendSuccess(player, MercuryPuzzleCore.NAME, "Plugin is now disabled!");
    }
}
