package com.example.nodesapp;

import com.example.nodesapp.FN.FunctionNode;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class NodesManager {
    private ObservableList<FunctionNode> nodes;   // list of function nodes

    public NodesManager() {
        this.nodes = FXCollections.observableArrayList();

        // bind nodes to the canvas children
        bindNodesToCanvasChildren();
    }

    // add a node to the function nodes
    // node: the node to add
    public void addNode(FunctionNode node) {
        this.nodes.add(node);
    }

    // a function that binds the nodes list to the canvas children list,
    // so that every node that is added here will be automatically added to the canvas children.
    private void bindNodesToCanvasChildren() {
        ObservableList<Node> canvasChildren = Director.getInstance().getCanvas().getChildren();
        // Add a listener to list1
        this.nodes.addListener((ListChangeListener<FunctionNode>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    canvasChildren.addAll(change.getAddedSubList());
                } else if (change.wasRemoved()) {
                    canvasChildren.removeAll(change.getRemoved());
                }
            }
        });
    }

    public void addAllNodes(FunctionNode... nodes) {
        for(FunctionNode node : nodes) {
            this.addNode(node);
        }
    }

    // remove a node from the function nodes
    // node: the node to remove
    public void removeNode(FunctionNode node) {
        node.hideStroke();
        node.terminateSockets();
        this.nodes.remove(node);
    }

    public ObservableList<FunctionNode> getNodes() {
        return nodes;
    }

    public void setNodes(ObservableList<FunctionNode> nodes) {
        this.nodes = nodes;
    }
}
