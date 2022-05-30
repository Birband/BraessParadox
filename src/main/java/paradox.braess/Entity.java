package paradox.braess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity implements Runnable{
    private double time;
    private final Node destination;
    private Node origin;

    private final List<Queue> paths;
    private final List<Node> nodes;

    public Entity(Node destination, Node origin, List<Queue> paths, List<Node> nodes) {
        this.nodes = nodes;
        this.time = 0;
        this.destination = destination;
        this.origin = origin;
        this.paths = paths;
    }

    @Override
    public void run() {
        while(origin != destination) {
            Queue path = this.searchPath(); // searches for the most optimal path to take

            // check if the path exists
            if(path == null) break;


            long traffic_start = 0;
            long traffic_end = 0;
            // wait till the car reaches the destination
            try {
                this.time += path.access();
                traffic_start = System.nanoTime();
                Thread.sleep(path.getDrive_time());
                traffic_end = System.nanoTime();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.time += (double)(traffic_end - traffic_start)/1000000;
            // leaves current path
            path.leave();
            this.origin = path.getEnd();
        }
    }

    private Queue searchPath() {
// some easy pathing for program testing
//        for (var path : this.paths) {
//             if(path.getBegin() == this.origin && !this.visited.contains(path.getEnd())) {
//                 if(best_path == null) {
//                     best_path = path;
//                     best_time = path.getCounter() >= path.getSize() ? path.getDrive_time() + path.getLast_wait() : path.getDrive_time();
//                 } else if (best_time > path.getDrive_time()) {
//                     best_path = path;
//                     best_time = path.getCounter() >= path.getSize() ? path.getDrive_time() + path.getLast_wait() : path.getDrive_time();
//                 }
//             }
//         }

        Map<Node, Double> dist = new HashMap<Node, Double>();
        Map<Node, Node> prev = new HashMap<Node, Node>();
        List<Node> Q = new ArrayList<Node>(this.nodes);

        for (var node : this.nodes) {
            if (node == this.origin) dist.put(node, 0.0);
            else dist.put(node, Double.MAX_VALUE);
            prev.put(node, null);
        }

        while (!Q.isEmpty()){
            double min = Double.MAX_VALUE;
            Node u = null;
            for (var v : Q) {
                if (dist.get(v) < min) {
                    min = dist.get(v);
                    u = v;
                }
            }
            Q.remove(u);
            if (u == this.destination) break;
            for (var path: this.paths) {
                if (path.getBegin() == u && Q.contains(path.getEnd())) {
                    double time = path.getCounter() >= path.getSize()*0.9 ? path.getDrive_time() + path.getLast_wait() : path.getDrive_time();
                    double alt = dist.get(u) +  time;
                    if (alt < dist.get(path.getEnd())) {
                        dist.put(path.getEnd(), alt);
                        prev.put(path.getEnd(), u);
                    }
                }
            }
        }

        Node track = null;
        Node target = this.destination;
        if (prev.get(target) != null || target == this.origin)
            while(prev.get(target) != null) {
                track = target;
                target = prev.get(target);
            }

        for(var p : paths) {
            if (p.getBegin() == this.origin && p.getEnd() == track) {
                return p;
            }

        }
        return null;
    }

    public double getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Node getDestination() {
        return destination;
    }

    public Node getOrigin() {
        return origin;
    }

    public void setOrigin(Node origin) {
        this.origin = origin;
    }
}
