package com.company;

import java.util.Comparator;
import java.util.LinkedList;

class AStar {

    static LinkedList<Node> searchSolution(Maze maze) {
        // TODO: 17-10-21
        // 建立只含初始节点 S0 的搜索图 Graph，计算 f(S0);

        // 将 S0 放入 OPEN 表；
        LinkedList<Node> openList = new LinkedList<>();
        openList.add(maze.getStart());
        // 将 CLOSED 表初始化为空；
        LinkedList<Node> closedLinkedList = new LinkedList<>();

        Node n = null;
        // while OPEN 表不为空 DO
        while (!openList.isEmpty()) {
            // 从 OPEN 表中取出 f（n）值最小的节点 n；
            // 将 n 点从 OPEN 表中剔除；
            n = openList.remove(0);

            // 将 n 点加入 CLOSED 表；
            closedLinkedList.add(n);

            // If n 是目标节点
            if (n.equals(maze.getEnd())) {
                // Then 根据 n 的父指针指出从 S0 到 n 的路径，算法结束；
                break;
            }
            // Else
            else {
                // 生成 n 的子节点集合 {m0，m1，..}；
                // For 对于每一个该集合的节点 mij：
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {

                        int neighborI = n.getX() + i;
                        int neighborJ = n.getY() + j;
                        if (neighborI < 0 || neighborI > maze.getNodes().length - 1) {
                            continue;
                        }
                        if (neighborJ < 0 || neighborJ > maze.getNodes()[n.getX()].length - 1) {
                            continue;
                        }
                        if (neighborJ == n.getY() && neighborI == n.getX()) {
                            continue;
                        }

                        Node candidate = maze.getNodes()[neighborI][neighborJ];

                        if (candidate.isObstacle()) {
                            continue;
                        }
                        // 如果将要扩展的子节点为 n 的父亲节点，则放弃
                        if (isAncestor(n, candidate)) {
                            continue;
                        }

                        // TODO: 17-10-21
                        // 把 mi 作为 n 的子节点加入到 Graph 中，并计算 f（mi）；


                        // If mi 未曾在 OPEN 表和 CLOSED 表中出现过
                        if (!openList.contains(candidate) && !closedLinkedList.contains(candidate)) {
                            // 将 mi 配上刚刚计算过的 f（mi）值；

                            // 将 mi 的父亲节点指针指向 n；
                            candidate.setParent(n);
                            // 将 mi 加入 OPEN 表；
                            openList.add(candidate);
                        }
                        //     Else if mi 已经在 OPEN 表中
                        else if (openList.contains(candidate)) {

                            // 该节点的父亲指针已经有指向的父亲节点；
                            // If 『mi 相对于 n 的 f（mi）值』< 『mi 相对于原来的父亲节点的 f（mi）值』
                            Node temp = new Node(candidate.getX(), candidate.getY());
                            temp.setParent(n);
                            if (figureF(temp, maze) < figureF(candidate, maze)) {
                                // 修改 mi 的 f（mi）值为刚刚计算过的 f（mi）值；
                                // 修改 mi 的父亲节点指针指向 n；
                                candidate.setParent(n);
                            }
                        }
                        // Else if mi 已经在 CLOSED 表中
                        else if (closedLinkedList.contains(candidate)) {
                            // 该节点的父亲指针同样也已经有指向的父亲节点；
                            // If 『mi 相对于 n 的 f（mi）值』< 『mi 相对于原来的父亲节点的 f（mi）值』
                            Node temp = new Node(candidate.getX(), candidate.getY());
                            temp.setParent(n);
                            if (figureF(temp, maze) < figureF(candidate, maze)) {
                                // 修改 mi 的 f（mi）值为刚刚计算过的 f（mi）值；
                                // 修改 mi 的父亲节点指针指向 n；
                                candidate.setParent(n);
                                // 将 mi 节点移动至 OPEN 表；
                                openList.add(candidate);
                                closedLinkedList.remove(candidate);
                            }
                        }
                    }
                    // 按 f（mi）值从小到大的次序，对 OPEN 表进行重新排序；
                    openList.sort(Comparator.comparingInt(value -> figureF(value, maze)));
                }
            }

        }

        if (!maze.getEnd().equals(n))
            return null;
        else {
            LinkedList<Node> solution = new LinkedList<>();
            collectionSolution(n, solution);
            return solution;
        }
    }

    /**
     * @return return whether the candidate node is one of the n' mazeObstacleString ancestor
     */
    private static boolean isAncestor(Node n, Node candidate) {
        while (n != null) {
            if (candidate.equals(n.getParent()))
                return true;
            n = n.getParent();
        }
        return false;
    }

    private static void collectionSolution(Node n, LinkedList<Node> solution) {
        if (n.getParent() != null) {
            collectionSolution(n.getParent(), solution);
        }
        solution.addLast(n);
    }

    private static int figureG(Node p, Maze maze) {
        if (p.equals(maze.getStart())) {
            return 0;
        }
        return figureG(p.getParent(), maze);
    }

    private static int figureH(Node p, Maze maze) {
        if (p.isObstacle()) return Integer.MAX_VALUE;
        return Math.abs(p.getX() - maze.getEnd().getX()) + Math.abs(p.getY() - maze.getEnd().getY());
    }

    private static int figureF(Node p, Maze maze) {
        return figureG(p, maze) + figureH(p, maze);
    }
}
