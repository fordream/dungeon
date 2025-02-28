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

package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.util.DungeonMath;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

/**
 * An implementation of AttackAlgorithm that takes into account the increased brutality of Orcs when endangered.
 *
 * <p>The critical chance increases as the creature gets closer to dying.
 */
class OrcAttackAlgorithm extends SimpleAttackAlgorithm {

  private static final double MIN_CRITICAL_CHANCE = 0.1;
  private static final double MAX_CRITICAL_CHANCE = 0.5;

  @Override
  Percentage getCriticalChance(@NotNull Creature creature) {
    Percentage healthiness = creature.getHealth().toPercentage();
    return new Percentage(DungeonMath.weightedAverage(MAX_CRITICAL_CHANCE, MIN_CRITICAL_CHANCE, healthiness));
  }

}
