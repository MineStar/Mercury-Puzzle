package de.minestar.mercurypuzzle.StructureInformation;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

import de.minestar.mercurypuzzle.Structure.StructureInformation;

public class InformationFurnace extends StructureInformation {

    private final HashMap<Integer, ItemStack> items;
    private short burnTime, cookTime;

    public InformationFurnace(Furnace furnace) {
        this.burnTime = furnace.getBurnTime();
        this.cookTime = furnace.getCookTime();
        this.items = new HashMap<Integer, ItemStack>();
        for (int index = 0; index < furnace.getInventory().getSize(); index++) {
            if (furnace.getInventory().getItem(index) == null || furnace.getInventory().getItem(index).getType().equals(Material.AIR)) {
                continue;
            }
            this.items.put(index, furnace.getInventory().getItem(index).clone());
        }
    }

    @Override
    public void pasteInformation(Block block) {
        if (!block.getType().equals(Material.FURNACE) && !block.getType().equals(Material.BURNING_FURNACE)) {
            return;
        }
        for (Map.Entry<Integer, ItemStack> entry : this.items.entrySet()) {
            ((Furnace) block.getState()).getInventory().setItem(entry.getKey(), entry.getValue().clone());
        }
        ((Furnace) block.getState()).setBurnTime(this.burnTime);
        ((Furnace) block.getState()).setCookTime(this.cookTime);
    }
}
