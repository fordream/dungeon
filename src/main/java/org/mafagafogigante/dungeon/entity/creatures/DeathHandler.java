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

import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.entity.items.ItemFactory;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

final class DeathHandler {

  private DeathHandler() {
    throw new AssertionError();
  }

  public static void handleDeath(@NotNull Creature creature) {
    if (creature.getHealth().isAlive()) {
      throw new IllegalStateException("creature is alive.");
    }
    Location defeatedLocation = creature.getLocation();
    defeatedLocation.removeCreature(creature);
    if (creature.hasTag(Creature.Tag.CORPSE)) {
      // If the creature has the CORPSE tag, its corpse should be found among the presets, so there is no need to use a
      // try-catch statement here.
      Item item = ItemFactory.makeCorpse(creature, defeatedLocation.getWorld().getWorldDate());
      defeatedLocation.addItem(item);
    }
    creature.getDropper().dropEverything();
    DungeonLogger.fine("Disposed of " + creature.getName() + " at " + creature.getLocation() + ".");
  }

}
