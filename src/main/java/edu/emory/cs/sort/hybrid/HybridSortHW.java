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
package edu.emory.cs.sort.hybrid;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.*;
import edu.emory.cs.sort.AbstractSort;
import edu.emory.cs.sort.comparison.ShellSortKnuth;
import edu.emory.cs.sort.divide_conquer.IntroSort;
/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class HybridSortHW<T extends Comparable<T>> implements HybridSort<T> {

    private final IntroSort<T> engine2 = new IntroSort<>(new ShellSortKnuth<T>());
    private final AbstractSort<T> engine1 = new ShellSortKnuth<>();

    private T[] mergeRecursively(T[][] arrays, int begin, int end) {
        if (begin == end) {
            // base case 1: only one array in the range
            return arrays[begin];
        } else if (begin + 1 == end) {
            // base case 2: two arrays in the range
            return merge(arrays[begin], arrays[end]);
        } else {
            // recursive case: more than two arrays in the range
            int mid = (begin + end) / 2;
            T[] left = mergeRecursively(arrays, begin, mid);
            T[] right = mergeRecursively(arrays, mid + 1, end);
            return merge(left, right);
        }
    }
    public T[] sort(T[][] input) {
        Class<?> classtype = input[0][0].getClass();
        int size = Arrays.stream(input).mapToInt(t -> t.length).sum();
        T[] output = (T[]) Array.newInstance(classtype, size);

        for (T[] arr : input) {
            if (arr.length <= 1) {
                continue;
            }
            int countAscending = 0;
            int countDescending = 0;
            boolean isAscending = true;
            boolean isDescending = true;
            for (int j = 0; j < arr.length - 1; j++) {
                int z = j + 1;
                if (arr[j].compareTo(arr[z]) < 0) {
                    countAscending++;
                    isDescending = false;
                } else if (arr[j].compareTo(arr[z]) > 0) {
                    countDescending++;
                    isAscending = false;
                }
            }
            if (isAscending) {
                if (countAscending != arr.length - 1) {
                    engine1.sort(arr);
                }
            } else if (isDescending) {Collections.reverse(Arrays.asList(arr));
                if (countDescending != arr.length - 1) {
                    engine1.sort(arr);
                }
            } else {
                engine2.sort(arr);
            }
        }
        //recurssion
        T[] merged = mergeRecursively(input, 0, input.length - 1);
        System.arraycopy(merged, 0, output, 0, merged.length);
        return output;
    }
    private T[] merge(T[] a, T[] b) {
        int i = 0, j = 0, k = 0;
       T[] merged = (T[]) new Comparable[a.length + b.length];
        while (i < a.length && j < b.length) {
            if (a[i].compareTo(b[j]) < 0) {
                merged[k++] = a[i++];
            } else {
                merged[k++] = b[j++];
            }
        }
        while (i < a.length) {
            merged[k++] = a[i++];
        }
        while (j < b.length) {
            merged[k++] = b[j++];
        }
        return merged;
    }
}
