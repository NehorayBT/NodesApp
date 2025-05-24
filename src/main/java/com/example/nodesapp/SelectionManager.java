package com.example.nodesapp;

import com.example.nodesapp.FN.FunctionNode;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class SelectionManager {
    private SelectionBox selectionBox;
    private List<FunctionNode> selectedNodes;
    private List<FunctionNode> previouslySelectedNodes;

    private double lastMouseX;
    private double lastMouseY;

    public SelectionManager() {
        this.selectionBox = new SelectionBox(0, 0);
        this.selectedNodes = new ArrayList<>();
        this.previouslySelectedNodes = new ArrayList<>();
    }

    public void selectNode(FunctionNode node) {
        if(!this.selectedNodes.contains(node)) {
            this.selectedNodes.add(node);
            node.showStroke();
        }
        this.selectionBox.toFront();
    }

    public void onMousePressed(MouseEvent event) {
        for(FunctionNode node : this.selectedNodes) {
            node.toFront();
        }
        lastMouseX = event.getSceneX();
        lastMouseY = event.getSceneY();
        this.selectionBox.toFront();
    }

    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getSceneX() - lastMouseX;
        double deltaY = event.getSceneY() - lastMouseY;

        for(FunctionNode node : this.selectedNodes) {
            node.setLayoutX(node.getLayoutX() + deltaX);
            node.setLayoutY(node.getLayoutY() + deltaY);
        }

        lastMouseX = event.getSceneX();
        lastMouseY = event.getSceneY();
    }

    public void deselectAll() {
        for (FunctionNode node : this.selectedNodes) {
            node.hideStroke();
        }
        this.selectedNodes.clear();
        this.previouslySelectedNodes.clear();
    }

    public void deselectNode(FunctionNode node) {
        this.selectedNodes.remove(node);
        node.hideStroke();
    }

    public void deselectNodeIfNotPreviouslySelected(FunctionNode node) {
        if(!this.previouslySelectedNodes.contains(node)) {
            this.deselectNode(node);
        } else {
            this.selectNode(node);
        }
    }

    public boolean isNodeSelected(FunctionNode node) {
        return this.selectedNodes.contains(node);
    }

    public void updatePreviouslySelectedNodes() {
        this.previouslySelectedNodes.clear();
        this.previouslySelectedNodes.addAll(this.selectedNodes);
    }

    public void selectNodeIfNotPreviouslySelected(FunctionNode node) {
        if(this.previouslySelectedNodes.contains(node)) {
            this.deselectNode(node);
        } else {
            this.selectNode(node);
        }
    }

    public void deleteSelectedNodes() {
        NodesManager nodesManager = Director.getInstance().getNodesManager();
        for (FunctionNode node : this.selectedNodes) {
            nodesManager.removeNode(node);
        }
    }

    public SelectionBox getSelectionBox() {
        return selectionBox;
    }

    public void setSelectionBox(SelectionBox selectionBox) {
        this.selectionBox = selectionBox;
    }


}
