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

package org.mafagafogigante.dungeon.logging;

import org.mafagafogigante.dungeon.util.Messenger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DungeonLogger static class that provides the thread-safe logging methods that should be used throughout the
 * application.
 */
public final class DungeonLogger {

  private static final String LOG_FILE_PATH = "logs/";
  private static final String LOG_FILE_NAME = "log.txt";
  private static final Logger logger = Logger.getLogger("org.mafagafogigante.dungeon");

  static {
    logger.setUseParentHandlers(false);
    logger.setLevel(Level.ALL);
    try { // Try to add the file handler.
      Handler handler = new FileHandler(getCompleteLogFilePath(), true);
      handler.setFormatter(new DungeonFormatter());
      logger.addHandler(handler);
    } catch (IOException ignored) {
      // Couldn't add the file handler. There's nothing that can be done and this shouldn't stop the application.
    }
  }

  private DungeonLogger() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Logs a fine message. This should be used for tracing information.
   *
   * <p>If the file handler could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void fine(String message) {
    logger.fine(message);
  }

  /**
   * Logs a command rendering time.
   *
   * @param command the command string entered by the user
   * @param stopWatchString the string produced by the StopWatch used
   */
  public static void logCommandRendering(String command, String stopWatchString) {
    DungeonLogger.fine("Finished rendering '" + command + "' after " + stopWatchString + ".");
  }

  /**
   * Logs an info message. This should be used for application-related information.
   *
   * <p>If the file handler could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void info(String message) {
    logger.info(message);
  }

  /**
   * Logs a warning message. This should be used for non-fatal exceptions.
   *
   * <p>If the file handler could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void warning(String message) {
    logger.warning(message);
  }

  /**
   * Logs an unrecoverable error that cause application termination.
   *
   * <p>If the file handler could not be initialized, the message will be unceremoniously discarded.
   *
   * @param throwable the Throwable that needs to be logged
   */
  public static void logSevere(Throwable throwable) {
    logger.severe(getStackTraceString(throwable));
  }

  private static String getStackTraceString(Throwable throwable) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    throwable.printStackTrace(printWriter);
    return stringWriter.toString();
  }

  /**
   * Retrieves the path of a plain text file to be used to store logging messages. If the logging directory does not
   * exist, it will be created.
   *
   * @return the file path of a text file to be used by the FileHandler constructor.
   */
  private static String getCompleteLogFilePath() {
    File logFolder = new File(LOG_FILE_PATH);
    if (!logFolder.exists()) {
      if (!logFolder.mkdir()) {
        Messenger.printFailedToCreateDirectoryMessage(LOG_FILE_PATH);
      }
    }
    return LOG_FILE_PATH + LOG_FILE_NAME;
  }

}