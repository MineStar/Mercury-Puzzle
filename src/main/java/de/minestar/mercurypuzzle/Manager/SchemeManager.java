package de.minestar.mercurypuzzle.Manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.bukkit.util.Vector;

import de.minestar.mercurypuzzle.Structure.Structure;
import de.minestar.mercurypuzzle.Structure.StructureBlock;

public class SchemeManager {

    public static Structure load(String schemeName) {
        try {
            String fileName = "plugins/MercuryPuzzle/" + schemeName.toLowerCase() + ".bin";

            DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));

            Vector distanceVector = new Vector(reader.readInt(), reader.readInt(), reader.readInt());
            Vector sizeVector = new Vector(reader.readInt(), reader.readInt(), reader.readInt());
            int dataSize = reader.readInt();

            System.out.println("datasize: " + dataSize);

            int[] idData = new int[dataSize];
            int[] xData = new int[dataSize];
            int[] yData = new int[dataSize];
            int[] zData = new int[dataSize];
            byte[] subIdData = new byte[dataSize];

            for (int i = 0; i < dataSize; i++) {
                idData[i] = reader.readInt();
            }

            for (int i = 0; i < dataSize; i++) {
                subIdData[i] = reader.readByte();
            }

            for (int i = 0; i < dataSize; i++) {
                xData[i] = reader.readInt();
            }

            for (int i = 0; i < dataSize; i++) {
                yData[i] = reader.readInt();
            }

            for (int i = 0; i < dataSize; i++) {
                zData[i] = reader.readInt();
            }

            reader.close();

            ArrayList<StructureBlock> pasteList = new ArrayList<StructureBlock>();
            for (int i = 0; i < dataSize; i++) {
                pasteList.add(new StructureBlock(xData[i], yData[i], zData[i], idData[i], subIdData[i]));
            }
            return new Structure(pasteList, distanceVector, sizeVector);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void save(String schemeName, ArrayList<StructureBlock> pasteList, Vector distanceVector, Vector sizeVector) {
        try {
            String fileName = "plugins/MercuryPuzzle/" + schemeName.toLowerCase() + ".bin";

            DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));

            int[] idData = new int[pasteList.size()];
            int[] xData = new int[pasteList.size()];
            int[] yData = new int[pasteList.size()];
            int[] zData = new int[pasteList.size()];
            byte[] subIdData = new byte[pasteList.size()];
            int counter = 0;
            for (StructureBlock block : pasteList) {
                idData[counter] = block.getTypeID();
                subIdData[counter] = block.getSubID();
                xData[counter] = block.getX();
                yData[counter] = block.getY();
                zData[counter] = block.getZ();
                counter++;
            }

            writer.writeInt(distanceVector.getBlockX());
            writer.writeInt(distanceVector.getBlockY());
            writer.writeInt(distanceVector.getBlockZ());

            writer.writeInt(sizeVector.getBlockX());
            writer.writeInt(sizeVector.getBlockY());
            writer.writeInt(sizeVector.getBlockZ());

            writer.writeInt(pasteList.size());
            System.out.println("datasize: " + pasteList.size());

            for (int d : idData) {
                writer.writeInt(d);
            }

            for (byte d : subIdData) {
                writer.writeByte(d);
            }

            for (int d : xData) {
                writer.writeInt(d);
            }

            for (int d : yData) {
                writer.writeInt(d);
            }

            for (int d : zData) {
                writer.writeInt(d);
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
