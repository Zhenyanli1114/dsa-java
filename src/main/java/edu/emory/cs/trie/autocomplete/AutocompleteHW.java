package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.*;

public class AutocompleteHW extends Autocomplete<List<String>> {

    public AutocompleteHW(String dict_file, int max) {
        super(dict_file, max);
    }

public TrieNode<List<String>> findNode(String prefix) {
    TrieNode<List<String>> node = find(prefix);
    if (node == null) {
        put(prefix, new ArrayList<>());
        node = find(prefix);
        node.setEndState(false);
    } else if (node.getValue() == null) {
        node.setValue(new ArrayList<>());
    }
    return node;
}

    @Override
    public List<String> getCandidates(String prefix) {
        prefix = prefix.trim();
        TrieNode<List<String>> node = findNode(prefix);
        List<String> candidates = new ArrayList<>();

        Set<String> uniqueElements = new HashSet<>();
        if (node.getValue() != null) {
            for (String s : node.getValue()) {
                if (s != null && !uniqueElements.contains(s)) {
                    uniqueElements.add(s);
                    candidates.add(s);
                }
            }
        }

        // Add the prefix itself to the candidates list if it is a complete word
        if (node.isEndState() && !candidates.contains(prefix)) {
            candidates.add(prefix);
        }

        Queue<String> queue = new PriorityQueue<>(Comparator.comparing(String::length).thenComparing(String::compareTo));

            traverse(node, prefix, queue);

        while (!queue.isEmpty() && candidates.size() < getMax()) {
            String candidate = queue.poll();
            if (!candidates.contains(candidate) && !candidate.equals(prefix)) {
                candidates.add(candidate);
            }
        }

        return candidates;
    }



    private void traverse(TrieNode<List<String>> node, String prefix, Queue<String> queue) {
        if (node.isEndState()) {
            queue.add(prefix);
        }

        for (Character key : node.getChildrenMap().keySet()) {
            TrieNode<List<String>> child = node.getChild(key);
            traverse(child, prefix + key, queue);
        }
    }

    @Override
    public void pickCandidate(String prefix, String candidate) {
        prefix = prefix.trim();
        candidate = candidate.trim();
        TrieNode<List<String>> node = findNode(prefix);
        List<String> temp = node.getValue();

        if (temp == null) {
            temp = new ArrayList<>();
            node.setValue(temp);
        }
        temp.remove(candidate);
        temp.add(0, candidate);
        if (temp.size() > getMax()) {
            String lastCandidate = temp.remove(temp.size() - 1);
            remove(lastCandidate);
        }
        put(candidate, new ArrayList<>());
    }
}
