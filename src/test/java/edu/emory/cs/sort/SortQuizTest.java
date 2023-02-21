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

package edu.emory.cs.sort;
import edu.emory.cs.sort.comparison.*;
import edu.emory.cs.sort.distribution.LSDRadixSort;
import edu.emory.cs.sort.distribution.RadixSortQuiz;
import org.junit.jupiter.api.Test;

/** @author Jinho D. Choi */
public class SortQuizTest extends SortTest {

    @Test
    public void testRobustness() {
        testRobustness(new ShellSortQuiz<>());
        testRobustness(new RadixSortQuiz());
    }
    @Test
    public void testRuntime() {
        testRuntime(new ShellSortKnuth<>(), new ShellSortQuiz<>());
        testRuntime(new LSDRadixSort(), new RadixSortQuiz());
    }
}

