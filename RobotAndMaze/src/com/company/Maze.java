package com.company;

import java.util.LinkedList;

public class Maze {
    // 表示地形的一个字符串，空心方块表示空位，黑心方块表示障碍物
    public static String terrain;

    // 行、列
    private int cols, rows;

    // 起点和终点
    private Node start, end;

    // 地图矩阵
    private Node[][] nodes;

    public Node[][] getNodes() {
        return nodes;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public Maze(int rows,int cols) {
        this.cols = cols;
        this.rows = rows;
        nodes = new Node[rows][cols];
        initGraph(nodes, getTerrain());
    }

    /**
     * 规则打印出地图
     */
    public void printGraph() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (nodes[i][j].equals(end)) {
                    System.out.printf("❤ ");
                } else if (nodes[i][j].equals(start)) {
                    System.out.printf("✫ ");
                } else if (nodes[i][j].isObstacle()) {
                    System.out.printf("■ ");
                } else {
                    System.out.printf("□ ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 规则打印出显示了解决方案的地图
     * @param solution 解决方案，就是一个路径点的集合，从起点到终点的最佳路径
     */
    public void printSolution(LinkedList<Node> solution) {
        if (solution == null) {
            System.out.println("no solution!");
            return;
        }
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j].equals(start)) {
                    System.out.printf("✫ ");
                } else if (nodes[i][j].equals(end)) {
                    System.out.printf("❤ ");
                } else if (solution.contains(nodes[i][j])) {
                    System.out.printf("✫ ");
                } else if (nodes[i][j].isObstacle()) {
                    System.out.printf("■ ");
                } else {
                    System.out.printf("□ ");
                }
            }
            System.out.println();
        }
    }

    /**
     * 初始化地图
     * @param nodes 地图点集合
     * @param obstacleRef 指示障碍物的位置的数组
     */
    private void initGraph(Node[][] nodes, char[][] obstacleRef) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodes[i][j] = new Node(
                        i, j,
                        obstacleRef[i][j] == '■'
                );
                if (obstacleRef[i][j] == '✫') {
                    start = nodes[i][j];
                }
                if (obstacleRef[i][j] == '❤') {
                    end = nodes[i][j];
                }
            }
        }
    }

    /**
     * 获取地图情况
     * @return 代表地图情况的二维字符数组，空心方块表示空位，黑心方块表示障碍物
     */
    private char[][] getTerrain() {
        final char[][] terrainChars = new char[rows][cols];
        terrain = terrain.replaceAll("\\s", "");
        int k = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                terrainChars[i][j] = terrain.charAt(k++);
            }
        }
        return terrainChars;
    }

}
