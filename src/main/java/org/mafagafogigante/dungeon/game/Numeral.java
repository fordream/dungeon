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

import org.mafagafogigante.dungeon.logging.DungeonLogger;

/**
 * Numeral enumerated type.
 */
public enum Numeral {

  ONE("One"), TWO("Two"), THREE("Three"), FOUR("Four"), FIVE("Five"), MORE_THAN_FIVE("A few");

  final String stringRepresentation;

  Numeral(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
  }

  /**
   * Returns a corresponding Numeral of an integer or null if there is not such Numeral.
   */
  public static Numeral getCorrespondingNumeral(int integer) {
    if (integer < 1) {
      DungeonLogger.warning("Tried to get nonpositive numeral.");
      return null;
    } else if (integer >= values().length) {
      return values()[values().length - 1];
    } else {
      return values()[integer - 1];
    }
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
