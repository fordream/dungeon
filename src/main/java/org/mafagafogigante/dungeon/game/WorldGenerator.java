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

import org.mafagafogigante.dungeon.game.LocationPreset.Type;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The world generator. This class should be instantiated by a World object.
 */
class WorldGenerator implements Serializable {

  private static final int CHUNK_SIDE = 5;
  private final World world;
  private final RiverGenerator riverGenerator;
  private final DungeonDistributor dungeonDistributor = new DungeonDistributor();
  private final DungeonCreator dungeonCreator = new DungeonCreator(dungeonDistributor);
  private final int chunkSide;

  WorldGenerator(World world) {
    this.world = world;
    this.riverGenerator = new RiverGenerator();
    this.chunkSide = WorldGenerator.CHUNK_SIDE;
  }

  /**
   * Retrieves a random LocationPreset whose type is "Land".
   *
   * @return a LocationPreset
   */
  private static LocationPreset getRandomLandLocationPreset() {
    LocationPresetStore locationPresetStore = LocationPresetStore.getLocationPresetStore();
    return Random.select(locationPresetStore.getLocationPresetsByType(Type.LAND));
  }

  private Location createRandomRiverLocation(@NotNull final Point point) {
    LocationPresetStore locationPresetStore = LocationPresetStore.getLocationPresetStore();
    return new Location(Random.select(locationPresetStore.getLocationPresetsByType(Type.RIVER)), world, point);
  }

  private Location createRandomBridgeLocation(@NotNull final Point point) {
    LocationPresetStore locationPresetStore = LocationPresetStore.getLocationPresetStore();
    return new Location(Random.select(locationPresetStore.getLocationPresetsByType(Type.BRIDGE)), world, point);
  }

  public void expand(Point point) {
    riverGenerator.expand(point, chunkSide);
    Point currentPoint;
    LocationPreset currentLocationPreset = null;
    int remainingLocationsOfCurrentPreset = 0;
    int pX = point.getX();
    int pY = point.getY();
    // Get the closest smaller chunkSide multiple of x and y.
    // For instance, if chunkSide == 5, x == -2 and y == 1, then it makes xStart == -5 and yStart == 0.
    int xStart = pX < 0 ? chunkSide * (((pX + 1) / chunkSide) - 1) : chunkSide * (pX / chunkSide);
    int yStart = pY < 0 ? chunkSide * (((pY + 1) / chunkSide) - 1) : chunkSide * (pY / chunkSide);
    for (int x = xStart; x < xStart + chunkSide; x++) {
      for (int y = yStart; y < yStart + chunkSide; y++) {
        currentPoint = new Point(x, y, 0);
        if (!world.alreadyHasLocationAt(currentPoint)) {
          if (riverGenerator.isRiver(currentPoint)) {
            world.addLocation(createRandomRiverLocation(currentPoint), currentPoint);
          } else if (riverGenerator.isBridge(currentPoint)) {
            world.addLocation(createRandomBridgeLocation(currentPoint), currentPoint);
          } else if (dungeonDistributor.rollForDungeon(currentPoint)) {
            dungeonCreator.createDungeon(world, currentPoint);
          } else {
            if (currentLocationPreset == null || remainingLocationsOfCurrentPreset == 0) {
              currentLocationPreset = getRandomLandLocationPreset();
              remainingLocationsOfCurrentPreset = currentLocationPreset.getBlobSize();
            }
            world.addLocation(new Location(currentLocationPreset, world, currentPoint), currentPoint);
            remainingLocationsOfCurrentPreset--;
          }
        }
      }
    }
  }

}
