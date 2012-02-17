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
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.mercurypuzzle.Listener.PlayerListener;
import de.minestar.mercurypuzzle.Manager.PlayerManager;

public class MercuryPuzzle extends JavaPlugin {

    private PlayerManager playerManager;
    private PlayerListener playerListener;

    private static MercuryPuzzle instance;

    public void onDisable() {
        TextUtils.logInfo("Disabled!");
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

        // PRINT INFO
        TextUtils.logInfo("Version " + getDescription().getVersion() + " enabled!");
    }

    public static MercuryPuzzle getInstance() {
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
