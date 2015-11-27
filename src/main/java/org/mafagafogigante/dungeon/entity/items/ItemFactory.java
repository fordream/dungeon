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

package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.DungeonTimeParser;
import org.mafagafogigante.dungeon.entity.Integrity;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.Visibility;
import org.mafagafogigante.dungeon.entity.Weight;
import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.creatures.CreatureFactory;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.NameFactory;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.util.Percentage;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides methods to create different items for the game.
 */
public abstract class ItemFactory {

  private static final Map<Id, ItemPreset> itemPresets = new HashMap<>();

  /**
   * Loads all item presets that are not programmatically generated.
   */
  static {
    JsonObject objects = JsonObjectFactory.makeJsonObject("items.json");
    for (JsonValue value : objects.get("items").asArray()) {
      JsonObject itemObject = value.asObject();
      ItemPreset preset = new ItemPreset();
      preset.setId(new Id(itemObject.get("id").asString()));
      preset.setType(itemObject.get("type").asString());
      preset.setName(NameFactory.fromJsonObject(itemObject.get("name").asObject()));
      for (Item.Tag tag : tagSetFromArray(Item.Tag.class, itemObject.get("tags").asArray())) {
        preset.addTag(tag);
      }
      if (itemObject.get("decompositionPeriod") != null) {
        long seconds = DungeonTimeParser.parsePeriod(itemObject.get("decompositionPeriod").asString()).getSeconds();
        preset.setPutrefactionPeriod(seconds);
      }
      JsonObject integrity = itemObject.get("integrity").asObject();
      preset.setIntegrity(new Integrity(integrity.get("current").asInt(), integrity.get("maximum").asInt()));
      preset.setVisibility(new Visibility(Percentage.fromString(itemObject.get("visibility").asString())));
      if (itemObject.get("luminosity") != null) {
        preset.setLuminosity(new Luminosity(Percentage.fromString(itemObject.get("luminosity").asString())));
      }
      preset.setWeight(Weight.newInstance(itemObject.get("weight").asDouble()));
      preset.setDamage(itemObject.get("damage").asInt());
      preset.setHitRate(Percentage.fromString(itemObject.get("hitRate").asString()));
      preset.setIntegrityDecrementOnHit(itemObject.get("integrityDecrementOnHit").asInt());
      if (itemObject.get("nutrition") != null) {
        preset.setNutrition(itemObject.get("nutrition").asInt());
      }
      if (itemObject.get("integrityDecrementOnEat") != null) {
        preset.setIntegrityDecrementOnEat(itemObject.get("integrityDecrementOnEat").asInt());
      }
      if (preset.hasTag(Item.Tag.BOOK)) {
        preset.setText(itemObject.get("text").asString());
      }
      if (itemObject.get("spell") != null) {
        preset.setSpellId(itemObject.get("spell").asString());
      }
      addItemPreset(preset);
      CreatureFactory.makeCorpsePresets();
    }
    DungeonLogger.info("Loaded " + itemPresets.size() + " item presets.");
  }

  /**
   * Returns an unmodifiable view of the ItemPreset map.
   */
  private static Map<Id, ItemPreset> getItemPresets() {
    return Collections.unmodifiableMap(itemPresets);
  }

  /**
   * Attempts to create an item from the ItemPreset specified by an ID with the provided creation date. Throws an
   * IllegalArgumentException if an ItemPreset for this Id could not be found.
   *
   * @param id the ID of the preset, not null
   * @param date the creation date of the item, not null
   * @return an Item with the specified creation date
   */
  public static Item makeItem(@NotNull Id id, @NotNull Date date) {
    ItemPreset itemPreset = getItemPresets().get(id);
    if (itemPreset == null) {
      throw new IllegalArgumentException("id (" + id + ") does not correspond to an ItemPreset.");
    }
    return new Item(itemPreset, date);
  }

  /**
   * Adds a new ItemPreset to the factory.
   */
  public static void addItemPreset(ItemPreset preset) {
    if (itemPresets.containsKey(preset.getId())) {
      throw new IllegalArgumentException("factory already contains a preset with the Id " + preset.getId() + ".");
    } else {
      itemPresets.put(preset.getId(), preset);
    }
  }

  /**
   * Creates a Set of tags from an array of Strings.
   *
   * @param enumClass the Class of the enum
   * @param array a JSON array of strings
   * @param <E> an Enum type
   * @return a Set of Item.Tag
   */
  private static <E extends Enum<E>> Set<E> tagSetFromArray(Class<E> enumClass, JsonArray array) {
    Set<E> set = EnumSet.noneOf(enumClass);
    for (JsonValue value : array) {
      String tag = value.asString();
      try {
        set.add(Enum.valueOf(enumClass, tag));
      } catch (IllegalArgumentException fatal) {
        // Guarantee that bugged resource files are not going to make it to a release.
        String message = "invalid tag '" + tag + "' found.";
        throw new InvalidTagException(message, fatal);
      }
    }
    return set;
  }

  /**
   * Makes a corpse from the provided Creature. The creation Date of the corpse should be the Date of death.
   *
   * @param creature the Creature object
   * @param date the Date when the Creature died
   * @return an Item that represents the corpse of the Creature
   */
  public static Item makeCorpse(Creature creature, Date date) {
    if (!creature.hasTag(Creature.Tag.CORPSE)) {
      throw new AssertionError("Called makeCorpse for Creature that does not have the CORPSE tag!");
    }
    return makeItem(makeCorpseIdFromCreatureId(creature.getId()), date);
  }

  /**
   * Given a Creature ID, this method returns the corresponding corpse's ID.
   */
  public static Id makeCorpseIdFromCreatureId(Id id) {
    return new Id(id + "_CORPSE");
  }

  public static class InvalidTagException extends IllegalArgumentException {

    public InvalidTagException(String message, Throwable cause) {
      super(message, cause);
    }

  }

}
