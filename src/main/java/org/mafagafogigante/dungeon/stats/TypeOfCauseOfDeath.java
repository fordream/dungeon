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

package org.mafagafogigante.dungeon.stats;

/**
 * TypeOfCauseOfDeath enumerated type that defines the possible types of causes of death.
 */
public enum TypeOfCauseOfDeath {

  UNARMED("Unarmed"), WEAPON("Weapon"), SPELL("Spell");

  private final String stringRepresentation;

  TypeOfCauseOfDeath(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
