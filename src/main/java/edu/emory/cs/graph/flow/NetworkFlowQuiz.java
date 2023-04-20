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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @author Jinho D. Choi */
public class NetworkFlowQuiz {
    /**
     * Using depth-first traverse.
     * @param graph  a directed graph.
     * @param source the ource vertex.
     * @param target the target vertex.
     * @return a set of all augmenting paths between the specific source and target vertices in the graph.
     */
    HashSet<Subgraph> augmentingPaths = new HashSet<>();

    public Set<Subgraph> findAugmentingPaths(Graph graph, int source, int target) {
        int temp = target;
        target = source;
        source = temp;

        ArrayList<Edge> initialEdges = (ArrayList<Edge>) graph.getIncomingEdges(source);
        boolean[] visited = new boolean[graph.size()];

        for (boolean value : visited) {
            value = false;
        }

        for (int i = 0; i < initialEdges.size(); i++) {
            visited[source] = true;
            Subgraph subgraph = new Subgraph();
            subgraph.addEdge(initialEdges.get(i));
            depthFirstSearch(subgraph, graph, initialEdges.get(i), target, visited);
        }

        return augmentingPaths;
    }

    public void depthFirstSearch(Subgraph subgraph, Graph graph, Edge edge, int target, boolean[] visited) {
        if (edge.getSource() == target) {
            visited[target] = false;
            List<Edge> existingEdges = subgraph.getEdges();
            Subgraph newPath = new Subgraph();

            for (int index = 0; index < existingEdges.size(); index++) {
                newPath.addEdge(existingEdges.get(index));
            }

            augmentingPaths.add(newPath);
        }

        List<Edge> incomingEdges = graph.getIncomingEdges(edge.getSource());

        for (int i = 0; i < incomingEdges.size(); i++) {
            if (!visited[incomingEdges.get(i).getSource()]) {
                visited[incomingEdges.get(i).getSource()] = true;
                subgraph.addEdge(incomingEdges.get(i));
                depthFirstSearch(subgraph, graph, incomingEdges.get(i), target, visited);

                ArrayList<Edge> currentEdges = (ArrayList<Edge>) subgraph.getEdges();
                currentEdges.remove(incomingEdges.get(i));
                subgraph = new Subgraph();

                for (int j = 0; j < currentEdges.size(); j++) {
                    subgraph.addEdge(currentEdges.get(j));
                }
            }
        }

        visited[edge.getSource()] = false;
    }

    public static void main(String[] args) {
        Graph graph = new Graph(6);

        graph.setDirectedEdge(0, 1, 16);
        graph.setDirectedEdge(0, 2, 13);
        graph.setDirectedEdge(1, 2, 10);
        graph.setDirectedEdge(2, 1, 4);
        graph.setDirectedEdge(1, 3, 12);
        graph.setDirectedEdge(2, 4, 14);
        graph.setDirectedEdge(3, 2, 9);
        graph.setDirectedEdge(3, 5, 20);
        graph.setDirectedEdge(4, 3, 7);
        graph.setDirectedEdge(4, 5, 4);

        NetworkFlowQuiz networkFlowQuiz = new NetworkFlowQuiz();
        Set<Subgraph> augmentingPaths = networkFlowQuiz.findAugmentingPaths(graph, 0, 5);

        System.out.println("Augmenting paths:");
        for (Subgraph subgraph : augmentingPaths) {
            System.out.println("Path:");
            for (Edge edge : subgraph.getEdges()) {
                System.out.println(edge);
            }
        }
    }
}
