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

package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.Writer;

/**
 * Messenger helper class that defines several static methods to print uniform warning messages.
 */
public class Messenger {

  private Messenger() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  public static void printInvalidNumberFormatOrValue() {
    Writer.write("Invalid number format or value.");
  }

  /**
   * Prints a warning that a command requires arguments.
   */
  public static void printMissingArgumentsMessage() {
    Writer.write("This command requires arguments.");
  }

  public static void printAmbiguousSelectionMessage() {
    Writer.write("Provided input is ambiguous.");
  }

  /**
   * Prints a warning that a directory creation failed.
   */
  public static void printFailedToCreateDirectoryMessage(String directory) {
    Writer.write("Failed to create the '" + directory + "' directory.");
  }

}
