package com.example.filemanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PanelController implements Initializable {

    @FXML
    private TableView<FileInfo> fileTable;

    @FXML ComboBox<String> diskBox;

    @FXML TextField filePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>("type");
        fileTypeColumn.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getType()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("name");
        fileNameColumn.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getName()));
        fileNameColumn.setPrefWidth(180);

        TableColumn<FileInfo, String> fileSizeColumn = new TableColumn<>("size");
        fileSizeColumn.setCellValueFactory(param->{
            if (String.valueOf(param.getValue().getSize()).equals("0")){
                return new SimpleStringProperty("");
            }
            else return new SimpleStringProperty(String.format("%s bytes", String.valueOf(param.getValue().getSize())));
        });
        fileSizeColumn.setPrefWidth(180);

        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("last update");
        fileDateColumn.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getLastUpdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        fileDateColumn.setPrefWidth(180);

        fileTable.getColumns().addAll(fileTypeColumn,fileNameColumn, fileSizeColumn, fileDateColumn);
        fileTable.getSortOrder().add(fileTypeColumn);

        diskBox.getItems().clear();
        for (var e: FileSystems.getDefault().getRootDirectories()) {
            diskBox.getItems().add(e.toString());
        }
        diskBox.getSelectionModel().select(0);

        filePath.clear();

        updateList(Paths.get("."));

        fileTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount()==2){
                    Path path = Paths.get(filePath.getText()).resolve(fileTable.getSelectionModel().getSelectedItem().getName());
                    if (Files.isDirectory(path)){
                        updateList(path);
                    }
//                    else {
//                        if (path.toFile().canExecute()){
//                            try {
//                                Process p = Runtime.getRuntime().exec(String.valueOf(path));
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
                }
            }
        });
    }
    public void updateList(Path path){
        try {
            filePath.setText(path.normalize().toAbsolutePath().toString());

            fileTable.getItems().clear();
            fileTable.getItems().addAll(Files.list(path).map(FileInfo::new).toList());
            fileTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "update list error", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void btnUpAction(ActionEvent actionEvent) {
        Path upPaths = Paths.get(filePath.getText()).getParent();
        if (upPaths!=null) {
            updateList(upPaths);
        }
    }

    public void selectDisc(ActionEvent actionEvent) {
        updateList(Paths.get(diskBox.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFileName(){
        if (!fileTable.isFocused()) return null;
        else return fileTable.getSelectionModel().getSelectedItem().getName();
    }

    public String getCurrentPath(){
        return filePath.getText();
    }
}
