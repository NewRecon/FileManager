package com.example.filemanager;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HelloController {
    @FXML
    VBox leftPanel;

    @FXML
    VBox rightPanel;

    public void btnCopyAction(ActionEvent actionEvent) {
        PanelController leftController = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightController = (PanelController) rightPanel.getProperties().get("ctrl");

        if (leftController.getSelectedFileName() == null && rightController.getSelectedFileName() == null){
            new Alert(Alert.AlertType.WARNING,"File not selected", ButtonType.OK).showAndWait();
            return;
        }
        if (leftController.getSelectedFileName().equals(rightController.getSelectedFileName())){
            return;
        }

        PanelController srcPC, dstPC;
        if (leftController.getSelectedFileName()!=null){
            srcPC=leftController;
            dstPC=rightController;
        }
        else {
            srcPC=rightController;
            dstPC=leftController;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try{
            Files.copy(srcPath,dstPath);
            dstPC.updateList(Path.of(dstPC.getCurrentPath()));
        } catch (Exception ex){
            new Alert(Alert.AlertType.WARNING, "can't copy",ButtonType.OK).showAndWait();
        }
    }

    public void btnMoveAction(ActionEvent actionEvent) {
        PanelController leftController = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightController = (PanelController) rightPanel.getProperties().get("ctrl");

        if (leftController.getSelectedFileName() == null && rightController.getSelectedFileName() == null){
            new Alert(Alert.AlertType.WARNING,"File not selected", ButtonType.OK).showAndWait();
            return;
        }

        PanelController srcPC, dstPC;
        if (leftController.getSelectedFileName()!=null){
            srcPC=leftController;
            dstPC=rightController;
        }
        else {
            srcPC=rightController;
            dstPC=leftController;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try{
            Files.move(srcPath,dstPath);
            dstPC.updateList(Path.of(dstPC.getCurrentPath()));
            dstPC.updateList(Path.of(srcPC.getCurrentPath()));
        } catch (Exception ex){
            new Alert(Alert.AlertType.WARNING, "can't move",ButtonType.OK).showAndWait();
        }
    }

    public void btnDeleteAction(ActionEvent actionEvent) {
        PanelController leftController = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightController = (PanelController) rightPanel.getProperties().get("ctrl");

        if (leftController.getSelectedFileName() == null && rightController.getSelectedFileName() == null){
            new Alert(Alert.AlertType.WARNING,"File not selected", ButtonType.OK).showAndWait();
            return;
        }

        PanelController srcPC, dstPC;
        if (leftController.getSelectedFileName()!=null){
            srcPC=leftController;
            dstPC=rightController;
        }
        else {
            srcPC=rightController;
            dstPC=leftController;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());

        try{
            Files.delete(srcPath);
            dstPC.updateList(Path.of(dstPC.getCurrentPath()));
            dstPC.updateList(Path.of(srcPC.getCurrentPath()));
        } catch (Exception ex){
            new Alert(Alert.AlertType.WARNING, "can't del",ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void closeWindowAction(){
        Platform.exit();
    }
}