package com.example.nodesapp;

import java.util.ArrayList;
import java.util.List;

public class NodesManager {
    private List<FunctionNode> nodes;   // list of function nodes

    public NodesManager() {
        this.nodes = new ArrayList<>();
    }

    // add a node to the function nodes
    // node: the node to add
    public void addNode(FunctionNode node) {
        this.nodes.add(node);
        Director.getInstance().getCanvas().getChildren().add(node);
    }

    public void addAllNodes(FunctionNode... nodes) {
        for(FunctionNode node : nodes) {
            this.addNode(node);
        }
    }

    // remove a node from the function nodes
    // node: the node to remove
    public void removeNode(FunctionNode node) {
        this.nodes.remove(node);
        Director.getInstance().getCanvas().getChildren().remove(node);
    }
}
