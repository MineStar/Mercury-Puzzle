package de.minestar.mercurypuzzle.Listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.mercurypuzzle.Core.MercuryPuzzleCore;
import de.minestar.mercurypuzzle.Manager.PlayerManager;

public class PlayerListener implements Listener {

    private PlayerManager playerManager;
    public PlayerListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock() && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getTypeId() == Material.DEAD_BUSH.getId() && UtilPermissions.playerCanUseCommand(event.getPlayer(), "wefake.select")) {
            event.setCancelled(true);
            if (!this.playerManager.doSelection(event.getPlayer(), event.getClickedBlock().getLocation())) {
                ChatUtils.printInfo(event.getPlayer(), MercuryPuzzleCore.getInstance().getDescription().getName(), ChatColor.LIGHT_PURPLE, "Position 1 set.");
            } else {
                ChatUtils.printInfo(event.getPlayer(), MercuryPuzzleCore.getInstance().getDescription().getName(), ChatColor.LIGHT_PURPLE, "Position 2 set.");
            }
        }
    }
}
