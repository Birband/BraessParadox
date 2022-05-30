package paradox.braess;

public class Node {
    private String name;
    private Node node;
    private int cost;

    public Node(){};

    public Node(String name, Node node, int cost){
        this.name = name;
        this.node = node;
        this.cost = cost;
    };

    public Node(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
