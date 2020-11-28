package api;

import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph _g;

    @Override
    public void init(directed_weighted_graph g) {
        _g = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return _g;
    }

    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(_g);
    }

    @Override
    public boolean isConnected() {


        if (_g.nodeSize() == 0 || _g.nodeSize() == 1) {
            return true;
        }
        if (this._g.nodeSize() > this._g.edgeSize() + 1) { //the minimum number of edges for a connected graph, shallow chek.
            return false;
        }
        reset_and_lonely_check();
//       if (reset_and_lonely_check()){//reset all the Tags in the graph to 0.
//           return false;
//       }


        node_data startPoint = (_g.getV().iterator().next()); // get a random Node in the graph.
        Queue<node_data> q = new LinkedList<>(); // Queue to store all the checked Nodes.
        q.add(startPoint);

        while (!q.isEmpty()) {
            node_data cur = q.poll();
            for (edge_data i : _g.getE(cur.getKey())) { //check if V neighbor has been checked.
                if (_g.getNode(i.getDest()).getTag() == 0) {
                    q.add(_g.getNode(i.getDest())); // if not, add him to the Queue.
                    _g.getNode(i.getDest()).setTag(1);    //mark 1 - In the queue but not checked yet.
                }
            }
            cur.setTag(2);  // mark 2- done to check this node.
        }

        LinkedList<node_data> checklist = new LinkedList<>(_g.getV()); //List of all the graph nodes.
        for (node_data i : checklist) { // check if there is a node who doesn't marked with 2.
            if (i.getTag() != 2) return false;
        }
        return true;

    }

    /**
     * 1. Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.
     * 2. Assign to every node a tentative distance value: set it to zero for our initial node and to infinity for all other nodes. Set the initial node as current.
     * 3. For the current node, consider all of its unvisited neighbours and calculate their tentative distances through the current node. Compare the newly calculated tentative distance to the current assigned value and assign the smaller one. For example, if the current node A is marked with a distance of 6, and the edge connecting it with a neighbour B has length 2, then the distance to B through A will be 6 + 2 = 8. If B was previously marked with a distance greater than 8 then change it to 8. Otherwise, the current value will be kept.
     * 4. When we are done considering all of the unvisited neighbours of the current node, mark the current node as visited and remove it from the unvisited set. A visited node will never be checked again.
     * 5. If the destination node has been marked visited (when planning a route between two specific nodes) or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning a complete traversal; occurs when there is no connection between the initial node and remaining unvisited nodes), then stop. The algorithm has finished.
     * 6. Otherwise, select the unvisited node that is marked with the smallest tentative distance, set it as the new "current node", and go back to step 3.
     */

    @Override
    public double shortestPathDist(int src, int dest) {
        this.reset();
        node_data destination = _g.getNode(dest);
        _g.getNode(src).setWeight(0);
        PriorityQueue<NodeData> q = new PriorityQueue<>();
        q.add((NodeData) _g.getNode(src));
        while (!q.isEmpty()) {
            node_data cur = q.poll();
            int curkey = cur.getKey();
            for (edge_data d : _g.getE(curkey)) {
                NodeData ni = (NodeData) _g.getNode(d.getDest());
                double distance = d.getWeight() + cur.getWeight();
                if (ni.getWeight() > distance) {
                    ni.setWeight(distance);
                    ni.setInfo("" + curkey);
                    if (!q.contains(ni)) {
                        q.add(ni);
                    }
                }

            }
        }
        if (destination.getWeight() < Double.MAX_VALUE) {
            return destination.getWeight();
        }
        return -1;
    }

    private void reset() {
        for (node_data n : _g.getV()) {
            n.setWeight(Double.MAX_VALUE);
            n.setInfo("");
        }
    }
    private void reset_and_lonely_check() {
        for (node_data n : _g.getV()) {
            n.setTag(0);
//            if (_g.getE(n.getKey()).size() == 0) { //check if there is a lonely node --> false
//                return true;
//            }
        }
//        return false;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (this.shortestPathDist(src, dest) == -1) {
            return null;
        }
        LinkedList<node_data> res = new LinkedList<>();
        node_data cur = _g.getNode(dest);
        while (cur.getKey() != src) {
            res.addFirst(cur);
            cur = _g.getNode(Integer.parseInt(cur.getInfo()));
        }
        res.addFirst(cur);
        return res;
    }

    @Override
    public boolean save(String file) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean load(String file) {
        // TODO Auto-generated method stub
        return false;
    }

}
