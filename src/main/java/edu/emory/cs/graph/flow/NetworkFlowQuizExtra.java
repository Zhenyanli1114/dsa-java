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
package edu.emory.cs.graph.flow;

import edu.emory.cs.graph.Edge;
import edu.emory.cs.graph.Graph;
import edu.emory.cs.graph.Subgraph;
import java.util.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @author Francis */
public class NetworkFlowQuizExtra {
    public Set<Subgraph> getAugmentingPaths(Graph graph, int source, int target) {
        int b = target;
        target =source;
        source = b;
        HashSet<Subgraph> result = new HashSet<>();
        List<Edge> es = graph.getIncomingEdges(source);
        Queue<Subgraph> queue = new LinkedList<>();
        for (int i = 0;i<es.size();i++){
            Subgraph c = new Subgraph();
            c.addEdge(es.get(i));
            queue.add(c);
        }
        while (!queue.isEmpty()){
            int size = queue.size();
            for (int i = 0;i<size;i++){
                Subgraph path = queue.poll();
                int lastnode = path.getEdges().get(path.getEdges().size()-1).getSource();
                if (lastnode==target){
                    result.add(path);
                    continue;
                }
                es = graph.getIncomingEdges(lastnode);

                for (int j = 0;j<=es.size()-1;j++){

                    Subgraph c = new Subgraph();
                    for (int k = 0;k<=path.getEdges().size()-1;k++){
                        c.addEdge(path.getEdges().get(k));
                    }
                    if (!path.getVertices().contains(es.get(j).getSource())){
                        c.addEdge(es.get(j));
                        queue.add(c);
                    }
                }
            }

        }
        return result;
    }
}