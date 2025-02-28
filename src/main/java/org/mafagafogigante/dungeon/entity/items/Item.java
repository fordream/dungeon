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

package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.Duration;
import org.mafagafogigante.dungeon.entity.Entity;
import org.mafagafogigante.dungeon.entity.LightSource;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.Weight;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.util.Percentage;

public final class Item extends Entity {

  private final ItemIntegrity integrity;
  private final Date dateOfCreation;
  private final long decompositionPeriod;
  private final TagSet<Tag> tagSet;
  private final LightSource lightSource;
  private WeaponComponent weaponComponent;
  private FoodComponent foodComponent;
  private ClockComponent clockComponent;
  private BookComponent bookComponent;
  /* The Inventory this Item is in. Should be null whenever this Item is not in an Inventory. */
  private BaseInventory inventory;

  /**
   * Constructs a new Item from the provided preset and with the specified creation date.
   */
  public Item(ItemPreset preset, Date date) {
    super(preset);

    tagSet = TagSet.copyTagSet(preset.getTagSet());
    dateOfCreation = date;

    decompositionPeriod = preset.getPutrefactionPeriod();

    integrity = ItemIntegrity.makeItemIntegrity(preset.getIntegrity(), this);

    lightSource = new LightSource(preset.getLuminosity());

    if (hasTag(Tag.WEAPON)) {
      int damage = preset.getDamage();
      Percentage hitRate = preset.getHitRate();
      int integrityDecrementOnHit = preset.getIntegrityDecrementOnHit();
      weaponComponent = new WeaponComponent(damage, hitRate, integrityDecrementOnHit);
    }
    if (hasTag(Tag.FOOD)) {
      foodComponent = new FoodComponent(preset.getNutrition(), preset.getIntegrityDecrementOnEat());
    }
    if (hasTag(Tag.CLOCK)) {
      clockComponent = new ClockComponent(this);
    }
    if (hasTag(Tag.BOOK)) {
      bookComponent = new BookComponent(preset.getSpellId(), preset.getText());
    }
  }

  @Override
  public Weight getWeight() {
    Weight weight = super.getWeight();
    if (hasTag(Tag.WEIGHT_PROPORTIONAL_TO_INTEGRITY)) {
      return weight.multiply(integrity.toPercentage());
    } else {
      return weight;
    }
  }

  /**
   * Returns how many seconds have passed since this Item was created.
   *
   * @return a long representing an amount of seconds
   */
  public long getAge() {
    Duration existence = new Duration(dateOfCreation, Game.getGameState().getWorld().getWorldDate());
    return existence.getSeconds();
  }

  /**
   * Returns the name of this Item preceded by its integrity state.
   */
  public String getQualifiedName() {
    String singularName = getName().getSingular();
    if (getIntegrity().getCurrent() == getIntegrity().getMaximum()) {
      return singularName;
    } else {
      return getIntegrityString() + " " + singularName;
    }
  }

  public boolean hasTag(Tag tag) {
    return tagSet.hasTag(tag);
  }

  public ItemIntegrity getIntegrity() {
    return integrity;
  }

  public WeaponComponent getWeaponComponent() {
    return weaponComponent;
  }

  public FoodComponent getFoodComponent() {
    return foodComponent;
  }

  public ClockComponent getClockComponent() {
    return clockComponent;
  }

  public BookComponent getBookComponent() {
    return bookComponent;
  }

  public BaseInventory getInventory() {
    return inventory;
  }

  public void setInventory(BaseInventory inventory) {
    this.inventory = inventory;
  }

  /**
   * Returns whether or not this item is broken.
   *
   * @return true if the current integrity is zero
   */
  public boolean isBroken() {
    return integrity.isBroken();
  }

  public void decrementIntegrityByHit() {
    integrity.decrementBy(weaponComponent.getIntegrityDecrementOnHit());
  }

  public void decrementIntegrityByEat() {
    integrity.decrementBy(foodComponent.getIntegrityDecrementOnEat());
  }

  public void decrementIntegrityToZero() {
    integrity.decrementBy(integrity.getCurrent());
  }

  /**
   * Returns a string representation of the integrity state of this item.
   */
  private String getIntegrityString() {
    return IntegrityState.getIntegrityState(getIntegrity().getCurrent(), getIntegrity().getMaximum()).toString();
  }

  public long getDecompositionPeriod() {
    return decompositionPeriod;
  }

  @Override
  public Luminosity getLuminosity() {
    return lightSource.getLuminosity();
  }

  @Override
  public String toString() {
    return getName().toString();
  }

  public enum Tag {WEAPON, FOOD, CLOCK, BOOK, DECOMPOSES, REPAIRABLE, WEIGHT_PROPORTIONAL_TO_INTEGRITY}

}
