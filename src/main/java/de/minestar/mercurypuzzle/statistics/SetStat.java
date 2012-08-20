/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of Contao2.
 * 
 * Contao2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * Contao2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Contao2.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.mercurypuzzle.statistics;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Location;

import de.minestar.mercurypuzzle.Core.MercuryPuzzleCore;
import de.minestar.minestarlibrary.stats.Statistic;
import de.minestar.minestarlibrary.stats.StatisticType;

public class SetStat implements Statistic {

    private String player;
    private String corner1, corner2;
    private int TypeID, SubID;

    public SetStat() {
        // EMPTY CONSTRUCTOR FOR REFLECTION ACCESS
    }

    public SetStat(String player, Location minCorner, Location maxCorner, int TypeID, int SubID) {
        this.player = player;
        this.corner1 = minCorner.toString();
        this.corner2 = maxCorner.toString();
        this.TypeID = TypeID;
        this.SubID = SubID;
    }

    @Override
    public String getPluginName() {
        return MercuryPuzzleCore.NAME;
    }

    @Override
    public String getName() {
        return "Set";
    }

    @Override
    public LinkedHashMap<String, StatisticType> getHead() {

        LinkedHashMap<String, StatisticType> head = new LinkedHashMap<String, StatisticType>();

        head.put("player", StatisticType.STRING);
        head.put("corner1", StatisticType.STRING);
        head.put("corner2", StatisticType.STRING);
        head.put("TypeID", StatisticType.INT);
        head.put("SubID", StatisticType.INT);

        return head;
    }

    @Override
    public Queue<Object> getData() {

        Queue<Object> data = new LinkedList<Object>();

        data.add(player);
        data.add(corner1);
        data.add(corner2);
        data.add(TypeID);
        data.add(SubID);

        return data;
    }
}
