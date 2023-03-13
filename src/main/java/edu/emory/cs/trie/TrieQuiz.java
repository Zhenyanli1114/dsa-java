/*
 * Copyright 2020 Emory University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.emory.cs.trie;
import java.util.ArrayList;
import java.util.List;

public class TrieQuiz extends Trie<Integer> {
    /**
     * PRE: this trie contains all country names as keys and their unique IDs as values
     * (e.g., this.get("United States") -> 0, this.get("South Korea") -> 1).
     * @param input the input string in plain text
     *              (e.g., "I was born in South Korea and raised in the United States").
     * @return the list of entities (e.g., [Entity(14, 25, 1), Entity(44, 57, 0)]).
     */
        List<Entity> getEntities(String input) {
            TrieNode<Integer> node = getRoot();
            List<Entity> entities = new ArrayList<>();
            int start = -1;
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                TrieNode<Integer> child = node.getChild(c);

                if (child != null) {
                    if (start == -1) {
                        start = i;
                    }
                    node = child;
                } else {
                    if (start != -1) {
                        entities.add(new Entity(start, i, node.getValue()));
                        start = -1;
                        node = getRoot();
                    }
                }
            }

            if (start != -1) {
                entities.add(new Entity(start, input.length(), node.getValue()));
            }

            return entities;
        }
    }

