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

package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.*;

/**
 * @author ZhenYan Li ({@code zli735@emory.edu})
 */
public class AutocompleteHW extends Autocomplete<List<String>> {
    public AutocompleteHW(String dict_file, int max) {
        super(dict_file, max);
    }

    @Override
    public List<String> getCandidates(String prefix) {
        prefix = prefix.trim().toLowerCase();//trim empty space & convert to lower case
        TrieNode<List<String>> node = find(prefix);
        // use pq save candidate by length and alphabet order

        Queue<String> queue = new PriorityQueue<>(Comparator.comparing(String::length).thenComparing(String::compareTo));
        if (node != null) { //traverse
            dfs(node, prefix, queue);
        }

        List<String> candidates = new ArrayList<>();
        TrieNode<List<String>> pfnode = find(prefix);

        List<String> lishi = null;
        if (pfnode!=null && (lishi = pfnode.getValue()) != null) {
            candidates.addAll(lishi);
        }

        // Add candidates to the list
        while (!queue.isEmpty() && candidates.size() < getMax()) {
            String candidate = queue.poll();
            if (!candidates.contains(candidate)) {
                candidates.add(candidate);// 没有就加
            }
        }
        return candidates;
    }


    private void dfs(TrieNode<List<String>> node, String prefix, Queue<String> queue) {
        prefix = prefix.trim().toLowerCase();
        if (node.isEndState()) {
            queue.add(prefix);
        }

        for (Character key : node.getChildrenMap().keySet()) {
            TrieNode<List<String>> child = node.getChild(key);
            dfs(child, prefix + key, queue);
        }
    }

    @Override
    public void pickCandidate(String prefix, String candidate) {
        prefix = prefix.trim().toLowerCase();
        TrieNode<List<String>> node = find(prefix);

        if (node == null) {
            put(prefix, new ArrayList<>());
            node = find(prefix);
            // 当前前缀节点不为结束节点
            node.setEndState(false);
        }

        List<String> lishi = node.getValue();
        if (lishi == null) {
            lishi = new ArrayList<>();
            node.setValue(lishi);
        }

       lishi.remove(candidate);
       lishi.add(0, candidate);
        node = find(candidate);
        // 不要扔掉历史数据，还要塞
        List<String> candidateVal = node == null ? null : node.getValue();
        put(candidate, candidateVal);
    }
}
