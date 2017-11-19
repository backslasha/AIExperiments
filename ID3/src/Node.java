import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代表决策树的一个结点的类
 */
public class Node implements Cloneable {
    boolean isLeaf = false;
    private Node parent = null;
    String attrName;
    List<String> values;
    Map<String, Node> sonNodesMap;

    public Node(String attrName, String value, boolean isLeaf) {
        this.isLeaf = isLeaf;
        if (isLeaf) {
            this.attrName = attrName;
            this.values = new ArrayList<>();
            values.add(value);
        } else {
            this.attrName = attrName;
            this.sonNodesMap = new HashMap<>();
        }
    }

    public Node(String attrName) {
        this.attrName = attrName;
        this.values = new ArrayList<>();
        this.sonNodesMap = new HashMap<>();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Node node = (Node) super.clone();
        node.values = new ArrayList<>();
        node.sonNodesMap = new HashMap<>();
        node.values.addAll(values);
        node.sonNodesMap.putAll(sonNodesMap);
        node.attrName = attrName;
        return node;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }
}
