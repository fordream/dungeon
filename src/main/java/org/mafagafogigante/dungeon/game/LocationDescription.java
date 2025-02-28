/*
 * Copyright (C) 2015 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mafagafogigante.dungeon.game;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.io.Serializable;

/**
 * The description of a Location object.
 */
public class LocationDescription implements Examinable, Serializable {

  private final char symbol;
  private final Color color;
  private String info = "You don't discover anything.";

  public LocationDescription(char symbol, Color color) {
    this.symbol = symbol;
    this.color = color;
  }

  public char getSymbol() {
    return symbol;
  }

  public Color getColor() {
    return color;
  }

  @NotNull
  @Override
  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  @Override
  public String toString() {
    return "LocationDescription{" +
        "symbol=" + symbol +
        ", color=" + color +
        ", info='" + info + '\'' +
        '}';
  }

}
