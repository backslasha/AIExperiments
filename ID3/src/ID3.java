import java.util.*;

import static java.lang.Math.log;

/**
 * 此类提供构造决策树的方法
 */
public class ID3 {
    private static Node root = null;
    private static List<Node> attrNodes = new ArrayList<>();
    private static String resultName;
    private static String[][] table;

    /**
     * 构造决策树
     * @param table 提供数据的表格
     * @return 决策树的根结点
     */
    public static Node buildPolicyTree(String[][] table) {
        ID3.root = null;
        ID3.table = table;

        // 求的所有属性，放在 List 中
        for (int i = 0; i <= table[0].length - 2; i++) {
            attrNodes.add(new Node(table[0][i]));
        }

        // 取的结果的含义
        resultName = table[0][table[0].length - 1];

        buildPolicyTree(table, attrNodes, null);

        return root;
    }

    /**
     * 构造决策树
     *
     * @param table
     * @param attrNames
     * @param p
     */
    private static void buildPolicyTree(String[][] table, List<Node> attrNames, Node p) {

        // 从属性值 List 中取出信息熵最小的属性
        Node minNode = getMinEntropyNode(attrNames, table);

        if (minNode == null) {
            return;
        }

        // 如果该属性值不是决策树的根结点，则把该属性值连接到它的父节点
        if (p != null) {
            Iterator<Map.Entry<String, Node>> iterator = p.sonNodesMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Node> next = iterator.next();
                if (next.getValue() == null) {
                    minNode.setParent(p);
                    p.sonNodesMap.put(next.getKey(), minNode);
                    table = DataHandler.leftOnlyRows(
                            table,
                            p.attrName,
                            next.getKey()
                    );
                    break;
                }
            }

        } else {
            root = minNode;
        }

        // 保存该属性的所有可能的取值
        minNode.values.addAll(
                DataHandler.queryDistinct(table, minNode.attrName)
        );


        // 对于该属性的每个可能的取值来说，记录下对应的结点（可能是最终决策，也可能是另一个属性）
        for (String value : minNode.values) {
            // 求出该属性对应的决策结果集
            List<String> policys = DataHandler.queryDistinct(
                    table,
                    resultName,
                    minNode.attrName + "=?",
                    new String[]{value}
            );
            // 若决策结果只有 1 种，该属性在该取值下，结果已经确定，因此为其增加一个叶子结点
            // 并且从样本集中删除该属性在该值下的所有行
            if (1 == policys.size()) {
                Node leaf = new Node(resultName, policys.get(0), true);
                leaf.setParent(minNode);
                minNode.sonNodesMap.put(value, leaf);
                table = DataHandler.withoutRows(table, minNode.attrName, value);
            }// 若决策结果有 2 种，
            else {
                minNode.sonNodesMap.put(value, null);
                buildPolicyTree(copy(table), copy(attrNames), minNode);
            }

        }

    }

    /**
     * 从属性值 List 中取出信息熵最小的属性
     *
     * @param attrs 所有属性值
     * @return 信息熵最小的属性
     */
    private static Node getMinEntropyNode(List<Node> attrs, String[][] table) {
        if (attrs.size() == 0) {
            return null;
        }
        Node node = attrs.get(0);
        for (Node attr : attrs) {
            if (getEntropy(attr, table) < getEntropy(node, table)) {
                node = attr;
            }
        }
        attrs.remove(node);
        return node;
    }

    /**
     * 计算信息熵
     *
     * @param attr
     * @param table
     * @return
     */
    private static double getEntropy(Node attr, String[][] table) {
        double entropy = 0;
        String resultName = table[0][table[0].length - 1];
        List<String> attrDistinctValues = DataHandler.queryDistinct(table, attr.attrName);
        int length = attrDistinctValues.size();
        double param = table.length - 1;
        for (int i = 0; i < length; i++) {

            String value = attrDistinctValues.get(i);
            List<String> resultValues = DataHandler.query(
                    table,
                    resultName,
                    attr.attrName + "=?",
                    new String[]{value}
            );
            double param0 = resultValues.size();
            double param1 = count(resultValues);
            double param2 = param0 - param1;

            entropy += param1 * log2(param1 / param0) + param2 * log2(param2 / param0);

        }
        return -entropy / param;
    }

    /**
     * 计算结果集的值的种类数
     * @param resultValues 结果集
     * @return 结果集的值的种类数
     */
    private static double count(List<String> resultValues) {
        int count = 0;
        String s = resultValues.get(0);
        for (String resultValue : resultValues) {
            if (s.equals(resultValue)) {
                count++;
            }
        }
        return count;
    }

    private static double log2(double num) {
        if (num == 0) {
            return 0;
        }
        return log(num) / log(2);
    }

    /**
     * 深度复制二维数组
     */
    private static String[][] copy(String[][] table) {
        String[][] copy = new String[table.length][table.length];
        for (int k = 0; k < copy.length; k++) {
            copy[k] = table[k].clone();
        }

        return copy;
    }

    /**
     * 深度复制 List<Node> 
     */
    private static List<Node> copy(List<Node> attrNames) {
        List<Node> copy = new ArrayList<>();
        for (Node attrName : attrNames) {

            try {
                copy.add((Node) attrName.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return copy;
    }

}

