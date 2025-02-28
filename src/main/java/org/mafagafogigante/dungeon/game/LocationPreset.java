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

import org.mafagafogigante.dungeon.util.Percentage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The LocationPreset class that serves as a recipe for Locations.
 */
public final class LocationPreset {

  private final Id id;
  private final Type type;
  private final Name name;
  private final BlockedEntrances blockedEntrances = new BlockedEntrances();
  private final List<SpawnerPreset> spawners = new ArrayList<SpawnerPreset>();
  private final Map<Id, Percentage> items = new HashMap<Id, Percentage>();
  private Percentage lightPermittivity;
  private int blobSize;
  private LocationDescription description;

  LocationPreset(Id id, Type type, Name name) {
    this.id = id;
    this.type = type;
    this.name = name;
  }

  public Id getId() {
    return id;
  }

  public Type getType() {
    return type;
  }

  public Name getName() {
    return name;
  }

  public LocationDescription getDescription() {
    return description;
  }

  public void setDescription(LocationDescription description) {
    this.description = description;
  }

  public List<SpawnerPreset> getSpawners() {
    return spawners;
  }

  /**
   * Adds a Spawner to this Location based on a SpawnerPreset.
   *
   * @param preset the SpawnerPreset
   */
  public void addSpawner(SpawnerPreset preset) {
    this.spawners.add(preset);
  }

  public Set<Entry<Id, Percentage>> getItems() {
    return items.entrySet();
  }

  /**
   * Adds an Item to this Location based on an ItemFrequencyPair.
   *
   * @param id the ID string of the item
   * @param probability the probability of the item appearing
   */
  public void addItem(String id, Double probability) {
    items.put(new Id(id), new Percentage(probability));
  }

  public BlockedEntrances getBlockedEntrances() {
    return new BlockedEntrances(blockedEntrances);
  }

  /**
   * Blocks exiting and entering into the location by a given direction.
   *
   * @param direction a Direction to be blocked.
   */
  public void block(Direction direction) {
    blockedEntrances.block(direction);
  }

  public Percentage getLightPermittivity() {
    return lightPermittivity;
  }

  public void setLightPermittivity(double lightPermittivity) {
    this.lightPermittivity = new Percentage(lightPermittivity);
  }

  public int getBlobSize() {
    return blobSize;
  }

  public void setBlobSize(int blobSize) {
    this.blobSize = blobSize;
  }

  enum Type {RIVER, BRIDGE, DUNGEON_ENTRANCE, DUNGEON_STAIRWAY, DUNGEON_ROOM, DUNGEON_CORRIDOR, LAND}

}
