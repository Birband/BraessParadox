package paradox.braess;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
            List<Node> nodes = new ArrayList<Node>();
            List<Queue> paths = new ArrayList<Queue>();

            // creating required nodes / names don't matter
            nodes.add(new Node("A"));
            nodes.add(new Node("Y"));
            nodes.add(new Node("X"));
            nodes.add(new Node("B"));

            // creating connections
            //   ___Y___
            //  /   |   \
            // A    |    B
            //  \___X___/       :)
            paths.add(new Queue(20, nodes.get(0), nodes.get(1), 20));
            paths.add(new Queue(20, nodes.get(1), nodes.get(0), 20));
            paths.add(new Queue(50, nodes.get(1), nodes.get(3), 50));
            paths.add(new Queue(50, nodes.get(3), nodes.get(1), 50));
            paths.add(new Queue(50, nodes.get(0), nodes.get(2), 50));
            paths.add(new Queue(50, nodes.get(2), nodes.get(0), 50));
            paths.add(new Queue(20, nodes.get(2), nodes.get(3), 20));
            paths.add(new Queue(20, nodes.get(3), nodes.get(2), 20));

            // uncomment line above for fast addition of another track or create it
            //paths.add(new Queue(20, nodes.get(1), nodes.get(2), 20));

            // starting cars to ride from A to B
            int cars = 10000;
            List<Thread> threads = new ArrayList<Thread>(cars);
            List<Entity> traffic = new ArrayList<Entity>(cars);

            for (int i = 0; i < cars; i++) {
                traffic.add(new Entity(nodes.get(3), nodes.get(0), paths, nodes));
                threads.add(new Thread(traffic.get(i)));
            }

            for (int i = 0; i < cars; i++) {
                threads.get(i).start();
                Thread.sleep(0, 200000);
            }

            for (int i = 0; i < cars; i++) threads.get(i).join();
            // returning the average value of  time needed to reach destination

            double average = 0;

            for (int i = 0; i < cars; i++) {
                average += traffic.get(i).getTime();
            }

            average /= cars;

        System.out.println(average + "ms");
    }
}
