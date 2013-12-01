package de.minestar.mercurypuzzle.StructureInformation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.block.CraftSign;

import de.minestar.mercurypuzzle.Structure.StructureInformation;

public class InformationSign extends StructureInformation {

    private final String[] lines;

    public InformationSign(String[] lines) {
        this.lines = lines;
    }

    @Override
    public void pasteInformation(Block block) {
        if (!block.getType().equals(Material.WALL_SIGN) && !block.getType().equals(Material.SIGN_POST)) {
            return;
        }
        CraftSign cSign = (CraftSign) block.getState();
        for (int index = 0; index < lines.length; index++) {
            cSign.setLine(index, this.lines[index]);
        }
        cSign.update(true);
    }
}
