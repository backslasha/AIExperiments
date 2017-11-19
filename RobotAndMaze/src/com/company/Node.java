package com.company;

public class Node {
    private int x, y;
    private Node parent;
    private boolean isObstacle;

    public Node(int x, int y, boolean isObstacle) {
        this.x = x;
        this.y = y;
        this.isObstacle = isObstacle;
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public boolean isObstacle() {
        return isObstacle;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (x != node.x) return false;
        return y == node.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
