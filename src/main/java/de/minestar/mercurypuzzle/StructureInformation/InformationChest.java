package de.minestar.mercurypuzzle.StructureInformation;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.minestar.mercurypuzzle.Structure.StructureInformation;

public class InformationChest extends StructureInformation {

    private final HashMap<Integer, ItemStack> items;

    public InformationChest(Inventory inventory) {
        this.items = new HashMap<Integer, ItemStack>();
        for (int index = 0; index < inventory.getSize(); index++) {
            if (inventory.getItem(index) == null || inventory.getItem(index).getTypeId() == Material.AIR.getId()) {
                continue;
            }
            this.items.put(index, inventory.getItem(index).clone());
        }
    }

    @Override
    public void pasteInformation(Block block) {
        if (block.getTypeId() != Material.CHEST.getId()) {
            return;
        }
        for (Map.Entry<Integer, ItemStack> entry : this.items.entrySet()) {
            ((Chest) block.getState()).getInventory().setItem(entry.getKey(), entry.getValue().clone());
        }
    }
}
