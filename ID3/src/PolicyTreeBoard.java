import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * 此类构造时接收一个决策树的根结点，
 * 用于将决策树完整地绘制在一个 JPanel 中
 */
public class PolicyTreeBoard extends JPanel {
    private Node root;
    private int[] layerNodesCounter;
    private static final int NODE_PADDING = 20;
    private static final int NODE_HEIGHT = 20;
    private static final int NODE_WIGHT = 40;
    private static final int NODE_LINE_HEIGHT = 60;

    PolicyTreeBoard(Node root) {
        this.root = root;
        this.layerNodesCounter = layerNodesCounter(root);
        int maxNodeCountsInLayer = -1;
        int depth = depth(root);

        for (int nodeCountInLayer : layerNodesCounter) {
            maxNodeCountsInLayer = Math.max(nodeCountInLayer, maxNodeCountsInLayer);
        }

        setPreferredSize(new Dimension(
                maxNodeCountsInLayer * (NODE_WIGHT + NODE_PADDING + NODE_PADDING),
                depth * NODE_HEIGHT + (depth - 1) * NODE_LINE_HEIGHT
        ));
    }

    /**
     * 计算每一层的结点数
     */
    private int[] layerNodesCounter(Node root) {

        if (root == null) {
            return null;
        }

        int[] layerRecord = new int[depth(root)];
        layerRecord[0] = 1;
        int i = 1;

        LinkedList<Node> queue = new LinkedList<>();
        queue.addLast(root);

        int layerTag = 0;
        int layerNodeCount = 0;
        while (!queue.isEmpty()) {
            Node node = queue.removeFirst();
            layerTag = Math.max(--layerTag, 0);
            int sizeBefore = queue.size();
            Map<String, Node> sonNodesMap = node.sonNodesMap;
            if (sonNodesMap != null) {
                sonNodesMap.forEach((s, son) -> queue.addLast(son));
                layerNodeCount += queue.size() - sizeBefore;
                if (layerTag == 0) {
                    layerTag = layerNodeCount;
                    layerRecord[i++] = layerNodeCount;
                    layerNodeCount = 0;
                }
            } else {
                layerNodeCount += 0;
                if (!queue.isEmpty() && layerTag == 0) {
                    layerTag = layerNodeCount;
                    layerRecord[i++] = layerNodeCount;
                    layerNodeCount = 0;
                }

            }
        }

        return layerRecord;
    }

    /**
     * 求决策树深度
     */
    private int depth(Node root) {
        if (root == null) {
            return 0;
        }

        Map<String, Node> sonNodesMap = root.sonNodesMap;

        if (sonNodesMap == null) {
            return 1;
        }

        int maxDepth = 0;
        Iterator<Map.Entry<String, Node>> iterator = sonNodesMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Node> son = iterator.next();
            int depth = depth(son.getValue()) + 1;
            maxDepth = Math.max(depth, maxDepth);
        }
        return maxDepth;
    }

    @Override
    public void paint(Graphics g) {
        paintNodes(root, g);
    }

    /**
     * 迭代画每一个结点
     */
    private void paintNodes(Node root, Graphics g) {

        LinkedList<Node> queue = new LinkedList<>();
        queue.addLast(root);

        int index = 0;//记录画结点的行数
        int[] counter = layerNodesCounter.clone();// 记录 index 行还没画的结点个数
        Map<Node, Point> nodePointMap = new HashMap<>();
        while (!queue.isEmpty()) {
            Node node = queue.removeFirst();

            // 一行的所有字符内容的长度
            int rowContentWidth = layerNodesCounter[index]
                    * (NODE_WIGHT + NODE_PADDING + NODE_PADDING);

            // 当前这一行还没画的结点个数减 1
            counter[index]--;

            // 此结点要画的 text
            String text;
            if (node.isLeaf) {
                text = node.values.get(0);
            } else {
                text = node.attrName;
            }

            int stringWidth = g.getFontMetrics().stringWidth(text);// text 的长度
            int stringHeight = g.getFontMetrics().getHeight();// text 的高度

            // text 的起始 X 坐标
            int startX = getWidth() / 2 - rowContentWidth / 2;
            startX += (layerNodesCounter[index] - counter[index] - 1) * (NODE_WIGHT + NODE_PADDING + NODE_PADDING);
            startX += +NODE_PADDING;

            // text 的起始 Y 坐标
            int stringBaseLineY = g.getFontMetrics().getHeight() - g.getFontMetrics().getDescent();

            // 画 text
            g.drawString(text, startX, NODE_HEIGHT + NODE_LINE_HEIGHT * index + stringBaseLineY);

            // 画椭圆
            g.drawOval(startX - 10, NODE_HEIGHT + NODE_LINE_HEIGHT * index, stringWidth + 20, stringHeight);

            // 记录此结点的位置，以让子结点可以连线过来
            nodePointMap.put(node, new Point(
                    startX + (stringWidth / 2),
                    NODE_HEIGHT + NODE_LINE_HEIGHT * index + stringBaseLineY
            ));

            // 画线，连线至父节点
            if (node.getParent() != null) {
                Point p = nodePointMap.get(node.getParent());
                Point me = new Point(startX + (stringWidth / 2), NODE_HEIGHT + NODE_LINE_HEIGHT * index + stringBaseLineY);
                g.drawLine(p.x, p.y, me.x, me.y);
                node.getParent().sonNodesMap.forEach((value, son) -> {
                    if (son == node) {
                        g.drawString(value, (p.x + me.x) / 2, (p.y + me.y) / 2);
                    }
                });
            }

            // 将所有子结点加入队列
            Map<String, Node> sonNodesMap = node.sonNodesMap;

            if (sonNodesMap != null) {
                sonNodesMap.forEach((s, son) -> queue.addLast(son));
            }

            // 当前这一行还没画的结点个数为 0 时，index++ 表示换行
            if (counter[index] == 0) {
                index++;
            }
        }


    }
}
