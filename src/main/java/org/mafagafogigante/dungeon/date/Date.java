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

package org.mafagafogigante.dungeon.date;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Dungeon Date class. Stores an immutable time stamp.
 *
 * <p>Dungeon's date system has years of 10 months, months of 10 days and days of 24 hours of 60 minutes of 60 seconds.
 * All the public constants made available by this class refer to this date system. Therefore they should not be used to
 * represent or take place into calculations of real world time.
 */
public class Date implements Comparable<Date>, Serializable {

  private static final long serialVersionUID = 732767578077968362L;

  /**
   * The time, in milliseconds.
   */
  private long time; // Supports 1067519911 full years.

  private Date(long millis) {
    time = millis;
  }

  /**
   * Constructs a date from the provided parameters.
   *
   * @param year the year, positive
   * @param month the month, positive, smaller than or equal to the number of months in a year
   * @param day the day, positive, smaller than or equal to the number of days in a month
   * @param hour the hour, nonnegative, smaller than the number of hours in a day
   * @param minute the minute, nonnegative, smaller than the number of minutes in an hour
   * @param second the second, nonnegative, smaller than the number of seconds in a minute
   */
  public Date(long year, long month, long day, long hour, long minute, long second) {
    this(year, month, day);
    if (hour < 0) {
      DungeonLogger.warning("Tried to construct Date with negative hour.");
      hour = 0;
    } else if (hour >= DungeonTimeUnit.DAY.as(DungeonTimeUnit.HOUR)) {
      DungeonLogger.warning("Tried to construct Date with nonexistent hour.");
      // First hour of the next day. Even if the code supplied this, log a warning as this is likely a bug.
      hour = DungeonTimeUnit.DAY.as(DungeonTimeUnit.HOUR);
    }
    if (minute < 0) {
      DungeonLogger.warning("Tried to construct Date with negative minute.");
      minute = 0;
    } else if (minute >= DungeonTimeUnit.HOUR.as(DungeonTimeUnit.MINUTE)) {
      DungeonLogger.warning("Tried to construct Date with nonexistent minute.");
      // First minute of the next hour. Even if the code supplied this, log a warning as this is likely a bug.
      minute = DungeonTimeUnit.HOUR.as(DungeonTimeUnit.MINUTE);
    }
    if (second < 0) {
      DungeonLogger.warning("Tried to construct Date with negative second.");
      second = 0;
    } else if (second >= DungeonTimeUnit.MINUTE.as(DungeonTimeUnit.SECOND)) {
      DungeonLogger.warning("Tried to construct Date with nonexistent second.");
      // First second of the next minute. Even if the code supplied this, log a warning as this is likely a bug.
      second = DungeonTimeUnit.MINUTE.as(DungeonTimeUnit.SECOND);
    }
    time += hour * DungeonTimeUnit.HOUR.milliseconds + minute * DungeonTimeUnit.MINUTE.milliseconds +
        second * DungeonTimeUnit.SECOND.milliseconds;
  }

  /**
   * Constructs a date from the provided parameters.
   *
   * @param year the year, positive
   * @param month the month, positive, smaller than or equal to the number of months in a year
   * @param day the day, positive, smaller than or equal to the number of days in a month
   */
  public Date(long year, long month, long day) {
    if (year <= 0) {
      DungeonLogger.warning("Tried to construct Date with nonpositive year.");
      year = 1;
    }
    if (month <= 0) {
      DungeonLogger.warning("Tried to construct Date with nonpositive month.");
      month = 1;
    } else if (month > DungeonTimeUnit.YEAR.as(DungeonTimeUnit.MONTH)) {
      DungeonLogger.warning("Tried to construct Date with nonexistent month.");
      month = DungeonTimeUnit.YEAR.as(DungeonTimeUnit.MONTH);
    }
    if (day <= 0) {
      DungeonLogger.warning("Tried to construct Date with nonpositive day.");
      day = 1;
    } else if (day > DungeonTimeUnit.MONTH.as(DungeonTimeUnit.DAY)) {
      DungeonLogger.warning("Tried to construct Date with nonexistent day.");
      day = DungeonTimeUnit.MONTH.as(DungeonTimeUnit.DAY);
    }
    time = DungeonTimeUnit.YEAR.milliseconds * (year - 1) + DungeonTimeUnit.MONTH.milliseconds * (month - 1) +
        DungeonTimeUnit.DAY.milliseconds * (day - 1);
  }

  /**
   * Returns the time since epoch in milliseconds.
   */
  public long getTime() {
    return time;
  }

  private long getSecond() {
    return (time % DungeonTimeUnit.MINUTE.milliseconds) / DungeonTimeUnit.SECOND.milliseconds;
  }

  private long getMinute() {
    return (time % DungeonTimeUnit.HOUR.milliseconds) / DungeonTimeUnit.MINUTE.milliseconds;
  }

  public long getHour() {
    return (time % DungeonTimeUnit.DAY.milliseconds) / DungeonTimeUnit.HOUR.milliseconds;
  }

  public long getDay() {
    return (time % DungeonTimeUnit.MONTH.milliseconds) / DungeonTimeUnit.DAY.milliseconds + 1;
  }

  public long getMonth() {
    return (time % DungeonTimeUnit.YEAR.milliseconds) / DungeonTimeUnit.MONTH.milliseconds + 1;
  }

  public long getYear() {
    return time / DungeonTimeUnit.YEAR.milliseconds + 1;
  }

  /**
   * Returns a new Date object corresponding to this Date plus the specified amount of time.
   *
   * @param amount a positive integer
   * @param unit a DungeonTimeUnit value, not null
   * @return a new Date object
   * @throws IllegalArgumentException if amount is not positive or if unit is null
   */
  public Date plus(int amount, DungeonTimeUnit unit) {
    if (amount <= 0) {
      throw new IllegalArgumentException("amount must be positive.");
    } else if (unit == null) {
      throw new IllegalArgumentException("unit should not be null.");
    } else {
      return new Date(time + amount * unit.milliseconds);
    }
  }

  /**
   * Returns a new Date object corresponding to this Date minus the specified amount of time.
   *
   * @param amount a positive integer
   * @param unit a DungeonTimeUnit value, not null
   * @return a new Date object
   * @throws IllegalArgumentException if amount is not positive or if unit is null
   */
  public Date minus(int amount, DungeonTimeUnit unit) {
    if (amount <= 0) {
      throw new IllegalArgumentException("amount must be positive.");
    } else if (unit == null) {
      throw new IllegalArgumentException("unit should not be null.");
    } else {
      return new Date(time - amount * unit.milliseconds);
    }
  }

  public String toDateString() {
    return "day " + getDay() + " of month " + getMonth() + " of the year " + getYear();
  }

  public String toTimeString() {
    return String.format("%02d:%02d:%02d", getHour(), getMinute(), getSecond());
  }

  @Override
  public int compareTo(@NotNull Date date) {
    if (time > date.time) {
      return 1;
    } else if (time == date.time) {
      return 0;
    } else {
      return -1;
    }
  }

  /**
   * Returns a String representation of this date, from year to second.
   *
   * @return a String
   */
  @Override
  public String toString() {
    String format = "%d-%02d-%02d %02d:%02d:%02d";
    return String.format(format, getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond());
  }

}