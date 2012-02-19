package de.minestar.mercurypuzzle.StructureInformation;

import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;

import de.minestar.mercurypuzzle.Structure.StructureInformation;

public class InformationNoteBlock extends StructureInformation {

    private final Note note;

    public InformationNoteBlock(NoteBlock noteBlock) {
        this.note = noteBlock.getNote();
    }

    @Override
    public void pasteInformation(Block block) {
        if (block.getTypeId() != Material.NOTE_BLOCK.getId()) {
            return;
        }
        ((NoteBlock) block.getState()).setNote(this.note);
    }
}
