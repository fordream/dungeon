/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.entity.creatures;

/**
 * HealthState enum that defines the six stages of healthiness.
 */
public enum HealthState {

  UNINJURED("Uninjured"),
  BARELY_INJURED("Barely Injured"),
  INJURED("Injured"),
  BADLY_INJURED("Badly Injured"),
  NEAR_DEATH("Near Death"),
  DEAD("Dead");

  private final String stringRepresentation;

  HealthState(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
