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
/** @author Jinho D. Choi */
package edu.emory.cs.queue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class TernaryHeapQuiz<T extends Comparable<T>> extends AbstractPriorityQueue<T> {
    private final List<T> keys;

    public TernaryHeapQuiz() {
        this(Comparator.naturalOrder());
    }

    public TernaryHeapQuiz(Comparator<T> priority) {
        super(priority);
        keys = new ArrayList<>();
    }

    @Override
    public int size() {
        return keys.size();
    }

    private int compare(int i1, int i2) {
        return priority.compare(keys.get(i1), keys.get(i2));
    }

    @Override
    public void add(T key) {
        keys.add(key);
        swim(size()-1);
    }

    private void swim(int k) {
        while (k > 0) {
            int parent = k / 3;
            if (compare(k, parent) > 0) {
                Collections.swap(keys, k, parent);
                k = parent;
            } else {
                break;
            }
        }
    }

    @Override
    public T remove() {
        if (isEmpty()) return null;
        Collections.swap(keys, 0, size() - 1);
        T max = keys.remove(size() - 1);
        sink();
        return max;
    }
    private void sink() {
        int k = 0;
        int kid = 3 * k + 1;
        while (kid < size()) {
            int middle = kid + 1;
            int right = kid + 2;
            int maxKid = kid;
            if (middle < size() && compare(middle, maxKid) > 0) {
                maxKid = middle;
            }
            if (right < size() && compare(right, maxKid) > 0) {
                maxKid = right;
            }
            if (compare(k, maxKid) >= 0) {
                break;
            }
            Collections.swap(keys, k, maxKid);
            k = maxKid;
            kid = 3 * k + 1;
        }
    }
}
