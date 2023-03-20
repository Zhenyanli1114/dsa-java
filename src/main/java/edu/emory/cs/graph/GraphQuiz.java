package edu.emory.cs.graph;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GraphQuiz extends Graph {

    List<ArrayList<Integer>> pathLine = new ArrayList<>();

    public GraphQuiz(int size) {
        super(size);
    }

    public GraphQuiz(Graph g) {
        super(g);
    }

    public int numberOfCycles() {
        HashSet<ArrayList<Integer>> all_cycles = new HashSet<>();
        List<Integer> pointList = getPointList();

        pointList.forEach(point -> {
            ArrayList<Integer> start = new ArrayList<>();
            start.add(point);
            pathLine.add(start);
            pathLine = pathLineFind(pathLine);

            List<ArrayList<Integer>> pathLineBak = new ArrayList<>();
            for (List<Integer> path : pathLine) {
                Set<Integer> hashSet = new HashSet<>(path);
                if (hashSet.size() < path.size()) {
                    int n = path.indexOf(path.get(path.size() - 1));
                    List<Integer> temp = path.subList(n, path.size() - 1);
                    int y = temp.indexOf(Collections.min(temp));
                    List<Integer> sorted = new ArrayList<>(temp.subList(y, temp.size()));
                    List<Integer> list = new ArrayList<>(temp.subList(0, y));
                    sorted.addAll(list);
                    all_cycles.add((ArrayList<Integer>) sorted);
                    continue;
                }
                pathLineBak.add((ArrayList<Integer>) path);
            }
            pathLine = pathLineBak;
        });

        return all_cycles.size();
    }


    public List<ArrayList<Integer>> pathLineFind(List<ArrayList<Integer>> forest) {

        ArrayList<ArrayList<Integer>> forest_copy = new ArrayList<>();

        for (int j = 0; j <= forest.size() - 1; j++) {
            int last = forest.get(j).get(forest.get(j).size() - 1);
            ArrayList<Edge> edges = (ArrayList<Edge>) this.getIncomingEdges(last);

            if (edges.size() > 1) {
                ArrayList<Integer> copied_lists = null;
                for (int k = 0; k <= edges.size() - 1; k++) {
                    copied_lists = (ArrayList<Integer>) forest.get(j).clone();
                    copied_lists.add(edges.get(k).getSource());
                    forest_copy.add(copied_lists);

                }
            } else if (edges.size() == 1) {
                ArrayList<Integer> copied_lists = null;
                copied_lists = (ArrayList<Integer>) forest.get(j).clone();
                copied_lists.add(edges.get(0).getSource());
                forest_copy.add(copied_lists);
            }
        }
        forest = forest_copy;

        return forest;
    }

    public List<Integer> getPointList() {
        return IntStream.range(0, size()).boxed().collect(Collectors.toCollection(ArrayList::new));
    }


}
