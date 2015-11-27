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

package org.mafagafogigante.dungeon.wiki;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.util.CounterMap;
import org.mafagafogigante.dungeon.util.Matches;
import org.mafagafogigante.dungeon.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Uninstantiable WikiSearcher class used to retrieve articles from the Wiki.
 */
public final class WikiSearcher {

  private WikiSearcher() {
    throw new AssertionError();
  }

  /**
   * Searches the wiki and writes the matching contents to the screen. This method triggers the wiki initialization.
   *
   * @param arguments an array of arguments that will determine the search
   */
  public static void search(String[] arguments) {
    if (arguments.length != 0) {
      Matches<Article> matches = Utils.findBestMatches(Wiki.getArticles(), arguments);
      if (matches.size() == 0) {
        deepSearch(arguments);
      } else if (matches.size() == 1) {
        Writer.write(matches.getMatch(0).toString());
      } else {
        DungeonString string = new DungeonString();
        string.append("The following article titles match your query:\n");
        for (int i = 0; i < matches.size(); i++) {
          string.append(toArticleListingEntry(matches.getMatch(i)));
          string.append("\n");
        }
        string.append("Be more specific.");
        Writer.write(string);
      }
    } else {
      writeArticleList();
    }
  }

  /**
   * Searches the wiki by looking at the content of the articles.
   *
   * @param arguments an array of arguments that will determine the search
   */
  private static void deepSearch(String[] arguments) {
    CounterMap<Article> counter = new CounterMap<>();
    for (Article article : Wiki.getArticles()) {
      int matches = 0;
      for (String argument : arguments) {
        String lowerCaseContent = article.getContent().toLowerCase(Locale.ENGLISH);
        matches += StringUtils.countMatches(lowerCaseContent, argument.toLowerCase(Locale.ENGLISH));
      }
      if (matches != 0) {
        counter.incrementCounter(article, matches);
      }
    }
    DungeonString string = new DungeonString();
    if (counter.isNotEmpty()) {
      string.append("The following articles contain text that matches your query:\n");
      for (Article article : counter) {
        String matchCount = counter.getCounter(article) + (counter.getCounter(article) > 1 ? " matches" : " match");
        string.append(toArticleListingEntry(article) + " (" + matchCount + ")\n");
      }
    } else {
      string.append("No article matches your query.");
    }
    Writer.write(string);
  }

  /**
   * Writes the article count and a list with the titles of the {@code Articles} in the {@code articleList}.
   */
  private static void writeArticleList() {
    DungeonString string = new DungeonString();
    string.append("The wiki has the following ");
    string.append(String.valueOf(Wiki.getArticles().size()));
    string.append(" articles:\n");
    for (Article article : Wiki.getArticles()) {
      string.append(toArticleListingEntry(article));
      string.append("\n");
    }
    Writer.write(string);
  }

  private static String toArticleListingEntry(Article article) {
    return "  " + article.getName();
  }

}
