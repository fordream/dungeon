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

package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class ShuffledRangeTest {

  @Test
  public void testShuffle() throws Exception {
    // The special case of a ShuffledRange of a single element.
    ShuffledRange shuffledRange = new ShuffledRange(1, 2);
    int oldLast = shuffledRange.get(shuffledRange.getSize() - 1);
    shuffledRange.shuffle();
    int newFirst = shuffledRange.get(0);
    Assert.assertEquals(oldLast, newFirst);
    // ShuffledRanges of different lengths, checking one of the properties of the shuffle method a few times.
    for (int rangeEnd = 3; rangeEnd <= 5; rangeEnd++) {
      shuffledRange = new ShuffledRange(1, rangeEnd);
      for (int i = 0; i < 100; i++) {
        oldLast = shuffledRange.get(shuffledRange.getSize() - 1);
        shuffledRange.shuffle();
        newFirst = shuffledRange.get(0);
        Assert.assertNotEquals(oldLast, newFirst);
      }
    }
  }

}