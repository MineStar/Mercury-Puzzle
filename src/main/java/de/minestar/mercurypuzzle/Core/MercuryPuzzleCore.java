/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of 'Illuminati'.
 * 
 * 'Illuminati' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * 'Illuminati' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 'Illuminati'.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package de.minestar.mercurypuzzle.Core;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.mercurypuzzle.Listener.PlayerListener;
import de.minestar.mercurypuzzle.Manager.PlayerManager;
import de.minestar.mercurypuzzle.commands.cmdCopy;
import de.minestar.mercurypuzzle.commands.cmdPaste;
import de.minestar.mercurypuzzle.commands.cmdSet;
import de.minestar.mercurypuzzle.commands.cmdToggle;
import de.minestar.mercurypuzzle.commands.cmdUndo;
import de.minestar.mercurypuzzle.statistics.SetStat;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.stats.StatisticHandler;

public class MercuryPuzzleCore extends JavaPlugin {

    private boolean pluginEnabled = false;
    public static String NAME = "Mercury";

    private PlayerManager playerManager;
    private PlayerListener playerListener;

    private CommandList cmdList;

    private static MercuryPuzzleCore instance;

    public void onDisable() {
        TextUtils.logInfo("Disabled!");
    }

    public boolean toggleEnabled() {
        this.pluginEnabled = !this.pluginEnabled;
        return this.pluginEnabled;
    }

    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    public void onEnable() {
        TextUtils.setPluginName(this.getDescription().getName());
        instance = this;

        // REGISTER MANAGERS
        File dataFolder = getDataFolder();
        dataFolder.mkdirs();
        playerManager = new PlayerManager();

        // REGISTER EVENTS
        playerListener = new PlayerListener(playerManager);
        Bukkit.getPluginManager().registerEvents(playerListener, this);

        // REGISTER COMMANDS
        this.initCommands();

        // register statistics
        this.registerStatistics();

        // PRINT INFO
        TextUtils.logInfo("Version " + getDescription().getVersion() + " enabled!");
    }

    private void initCommands() {
        /* @formatter:off */
        // Add an command to this list to register it in the plugin       
        AbstractCommand[] commands = new AbstractCommand[] {
                        new cmdToggle("[Mercury]", "/ctoggle", "", "mercury.enable"),
                        new cmdCopy("[Mercury]", "/ccopy", "", "mercury.copy", this.playerManager),
                        new cmdPaste("[Mercury]", "/cpaste", "", "mercury.copy", this.playerManager),
                        new cmdUndo("[Mercury]", "/cundo", "", "mercury.copy", this.playerManager),
                        new cmdSet("[Mercury]", "/set", "<TypeID> [SubID]", "mercury.set", this.playerManager),
        };
        /* @formatter:on */
        // store the commands in the hash map
        this.cmdList = new CommandList("[Mercury]", commands);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.cmdList.handleCommand(sender, label, args);
        return true;
    }

    protected boolean registerStatistics() {

        StatisticHandler.registerStatistic(SetStat.class);

        return true;
    }

    public static MercuryPuzzleCore getInstance() {
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
