package de.minestar.mercurypuzzle.StructureInformation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import de.minestar.mercurypuzzle.Structure.StructureInformation;

public class InformationSign extends StructureInformation {

    private final String[] lines;

    public InformationSign(String[] lines) {
        this.lines = lines;
    }

    @Override
    public void pasteInformation(Block block) {
        if (block.getTypeId() != Material.WALL_SIGN.getId() && block.getTypeId() != Material.SIGN_POST.getId()) {
            return;
        }
        for (int index = 0; index < lines.length; index++) {
            ((Sign) block.getState()).setLine(index, this.lines[index]);
        }
    }
}
