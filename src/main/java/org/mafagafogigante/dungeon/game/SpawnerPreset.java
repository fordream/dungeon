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

package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.date.DungeonTimeUnit;

class SpawnerPreset {

  public final Id id;
  public final int population;
  public final int spawnDelay;

  /**
   * Default SpawnerPreset constructor.
   *
   * @param id the creature ID string
   * @param population the maximum population
   * @param delayInHours the spawn delay, in hours
   */
  public SpawnerPreset(String id, int population, int delayInHours) {
    this.id = new Id(id);
    this.population = population;
    this.spawnDelay = delayInHours * (int) DungeonTimeUnit.HOUR.milliseconds;
  }

}
