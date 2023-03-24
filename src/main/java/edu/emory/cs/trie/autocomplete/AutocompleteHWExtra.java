package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.*;

public class AutocompleteHWExtra extends Autocomplete<List<AutocompleteHWExtra.Candidate>> {
    public AutocompleteHWExtra(String dict_file, int max) {
        super(dict_file, max);
    }

    static class Candidate implements Comparable<Candidate> {
        String word;
        int freq;
        int order;

        public Candidate(String word) {
            this.word = word;
            this.freq = 0;
            this.order = 0;
        }

        @Override
        public int compareTo(Candidate other) {
            if (freq != other.freq) {
                return Integer.compare(other.freq, freq);
            }
            return Integer.compare(other.order, order);
        }
    }

    private int countorder = 0;

    @Override
    public List<String> getCandidates(String prefix) {
        TrieNode<List<Candidate>> node = find(prefix.trim());
        List<Candidate> history = node == null ? null : node.getValue();
        Queue<Candidate> queue = new PriorityQueue<>(Collections.reverseOrder());

        if (node != null) {
            traverse(node, prefix, queue);
        }

        List<String> candidates = new ArrayList<>();

        if (history != null) {
            history.sort(Comparator.naturalOrder());
            for (Candidate candidate : history) {
                candidates.add(candidate.word);
            }
        }

        while (!queue.isEmpty() && candidates.size() < getMax()) {
            String candidate = queue.poll().word;
            if (!candidates.contains(candidate)) {
                candidates.add(candidate);
            }
        }

        return candidates;
    }

    private void traverse(TrieNode<List<Candidate>> node, String prefix, Queue<Candidate> queue) {
        if (node.isEndState()) {
            queue.add(new Candidate(prefix));
        }

        for (Character key : node.getChildrenMap().keySet()) {
            TrieNode<List<Candidate>> child = node.getChild(key);
            traverse(child, prefix + key, queue);
        }
    }

    @Override
    public void pickCandidate(String prefix, String candidate) {
        TrieNode<List<Candidate>> node = find(prefix.trim());

        if (node == null) {
            put(prefix, new ArrayList<>());
            node = find(prefix.trim());
        }

        List<Candidate> history = node.getValue();
        if (history == null) {
            history = new ArrayList<>();
            node.setValue(history);
        }

        Optional<Candidate> existC = Optional.empty();
        for (Candidate c : history) {
            if (c.word.equals(candidate)) {
                existC = Optional.of(c);
                break;
            }
        }
        if (existC.isPresent()) {
            existC.get().freq++;
        } else {
            Candidate newC = new Candidate(candidate);
            newC.freq = 1;
            newC.order = ++countorder;
            history.add(newC);
        }

        put(candidate, null);
    }


}
