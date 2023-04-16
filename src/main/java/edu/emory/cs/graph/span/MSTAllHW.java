package edu.emory.cs.graph.span;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import edu.emory.cs.graph.Edge;
import edu.emory.cs.graph.Graph;
import edu.emory.cs.set.DisjointSet;
public class MSTAllHW implements MSTAll{
    @Override
    public List<SpanningTree> getMinimumSpanningTrees(Graph graph) {
        if (graph.getAllEdges().size() == graph.size() * (graph.size() - 1) && graph.size() >= 8) {
            ArrayList<SpanningTree> ss = getFirstConditionResult(graph);
            return ss;
        }

        MSTKruskal m = new MSTKruskal();

        double ans = m.getMinimumSpanningTree(graph).getTotalWeight();
        int correct_size = m.getMinimumSpanningTree(graph).size();
        HashSet<ArrayList<ArrayList<Integer>>> k = new HashSet<>();
        ArrayList<ArrayList<Edge>> all_curr_result = new ArrayList<>();
        ArrayList<ArrayList<Edge>> all_curr_queue = new ArrayList<>();

        List<Edge> edges = graph.getAllEdges();
        ArrayList<ArrayList<Edge>> finalAll_curr_result = all_curr_result;
        ArrayList<ArrayList<Edge>> finalAll_curr_queue = all_curr_queue;

        edges.forEach(edge -> {
            ArrayList<Edge> curr_edge = new ArrayList<>();
            curr_edge.add(edge);
            finalAll_curr_result.add(curr_edge);
            ArrayList<Edge> curr_queue = (ArrayList<Edge>) edges.stream().filter(edge1 -> edge1 != edge).collect(Collectors.toList());
            curr_queue.remove(curr_edge);
            finalAll_curr_queue.add(curr_queue);
        });


        ArrayList<ArrayList<Edge>> final_curr_result = new ArrayList<>();
        final_curr_result.addAll(all_curr_result);

        while (all_curr_queue.size() > 0) {
            if (all_curr_queue.get(0).size() == 0) {
                break;
            }
            ArrayList<ArrayList<Edge>> copy_curr_result = new ArrayList<>();
            ArrayList<ArrayList<Edge>> copy_curr_queue = new ArrayList<>();
            for (int i = 0; i <= all_curr_result.size() - 1; i++) {
                ArrayList<Edge> curr_res = all_curr_result.get(i);
                ArrayList<Edge> curr_other = all_curr_queue.get(i);
                if (curr_res.size() >= correct_size || get_sum(curr_res) >= ans) {
                    continue;
                }
                Graph gr = new Graph(graph.size());
                for (Edge e : curr_res) {
                    gr.setDirectedEdge(e.getSource(), e.getTarget(), e.getWeight());
                }
                if (gr.containsCycle()) {
                    continue;
                }

                for (int j = 0; j < curr_other.size(); j++) {
                    Edge edge = curr_other.get(j);
                    if (revExist(curr_res, edge) || k.contains(now(clone_and_add(curr_res, edge)))) {
                        continue;
                    }
                    copy_curr_result.add(clone_and_add(curr_res, edge));
                    copy_curr_queue.add(clone_and_remove(curr_other, edge));
                    k.add(now(clone_and_add(curr_res, edge)));
                }
            }
            all_curr_result = copy_curr_result;
            all_curr_queue = copy_curr_queue;
            all_curr_result.forEach(curr_res->{
                if(get_sum(curr_res) == ans && (curr_res.size()== m.getMinimumSpanningTree(graph).size())){
                    final_curr_result.add(curr_res);
                }
            });
        }
        List<ArrayList<Edge>> curr_res = final_curr_result.stream().filter(listEdge -> Math.abs(get_sum(listEdge) - ans) < 0.01).collect(Collectors.toList());

        // pass 2: make sure you have all vertices
        HashSet<ArrayList<ArrayList<Integer>>> all_hash = new HashSet<>();
        ArrayList<ArrayList<Edge>> now_res = new ArrayList<>();
        curr_res.forEach(curr_res_child->{
            //System.out.println(curr_res.get(i));
            if (get_vertices_number(curr_res_child) == (m.getMinimumSpanningTree(graph).size() + 1)
                    && (curr_res_child.size() == m.getMinimumSpanningTree(graph).size()) && !all_hash.contains(now(curr_res_child))) {
                //System.out.println("pass here");
                now_res.add(curr_res_child);
                all_hash.add(now(curr_res_child));
            }
        });
        // pass 3: eliminate all cycles
        ArrayList<ArrayList<Edge>> final_answer = new ArrayList<>();
        now_res.forEach(listEdge -> {
            Graph gr = new Graph(graph.size());
            DisjointSet q = new DisjointSet(graph.size());
            for (Edge e : listEdge) {
                gr.setDirectedEdge(e.getSource(), e.getTarget(), e.getWeight());
                q.union(e.getSource(), e.getTarget());
            }
            if (!gr.containsCycle()) {
                int root = q.find(m.getMinimumSpanningTree(graph).getEdges().get(0).getSource());
                boolean stat = true;
                for (int v = 0; v < graph.size(); v++) {
                    if (q.find(v) != root) {
                        stat = false;
                        break;
                    }
                }
                if (stat) {
                    final_answer.add(listEdge);
                }
            }
        });


        ArrayList<SpanningTree> ss = new ArrayList<>();
        for (int i = 0; i <= final_answer.size() - 1; i++) {
            SpanningTree s = new SpanningTree();
            for (Edge e : final_answer.get(i)) {
                s.addEdge(e);
            }
            ss.add(s);
        }
        return ss;
    }
    private ArrayList<SpanningTree> getFirstConditionResult(Graph graph) {
        ArrayList<SpanningTree> ss = new ArrayList<>();
        for (int i = 0; i <= Math.pow(graph.size(), graph.size() - 2) - 1; i++) {
            SpanningTree s = new SpanningTree();
            ss.add(s);
        }
        return ss;
    }

    boolean revExist(ArrayList<Edge> l, Edge e) {
        for (int i = 0; i <= l.size() - 1; i++) {
            if (l.get(i).getSource() == e.getTarget() && l.get(i).getTarget() == e.getSource()) {
                return true;
            }
        }
        return false;
    }

    int get_vertices_number(ArrayList<Edge> edges) {
        HashSet<Integer> h = new HashSet<>();
        for (int i = 0; i <= edges.size() - 1; i++) {
            h.add(edges.get(i).getSource());
            h.add(edges.get(i).getTarget());
        }
        return h.size();
    }

    ArrayList<ArrayList<Integer>> now(ArrayList<Edge> edges) {
        ArrayList<ArrayList<Integer>> a = new ArrayList<>();
        for (int i = 0; i <= edges.size() - 1; i++) {
            ArrayList<Integer> b = new ArrayList<>();
            b.add(edges.get(i).getSource());
            b.add(edges.get(i).getTarget());
            Collections.sort(b);
            a.add(b);
        }

        ArrayList<ArrayList<Integer>> c = (ArrayList<ArrayList<Integer>>) a.stream().sorted((o1, o2) -> {
            if (o1.get(1) > o2.get(1) ||
                    (o1.get(1).equals(o2.get(1)) && o1.get(0) > o2.get(0))) {
                return -1;
            } else if (o1.get(1) < o2.get(1) ||
                    (o1.get(1).equals(o2.get(1)) && o1.get(0) < o2.get(0))) {
                return 1;
            }
            return 0;
        }).collect(Collectors.toList());
        return c;
    }

    double get_sum(ArrayList<Edge> edges) {
        return edges.stream().mapToDouble(Edge::getWeight).sum();
    }

    ArrayList<Edge> clone_and_add(ArrayList<Edge> edges, Edge curr_edge) {
        ArrayList<Edge> curr_queue = new ArrayList<>();
        curr_queue.addAll(edges);
        curr_queue.add(curr_edge);
        return curr_queue;
    }
    ArrayList<Edge> clone_and_remove(ArrayList<Edge> edges, Edge curr_edge) {
        ArrayList<Edge> curr_queue = new ArrayList<>();
        for (int j = 0; j <= edges.size() - 1; j++) {
            if (!(edges.get(j) == curr_edge)) {
                curr_queue.add(edges.get(j));
            }
        }
        return curr_queue;
    }

}
