package com.example.filemanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PanelController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private TableView<FileInfo> fileTable;

    @FXML ComboBox<String> diskBox;

    @FXML TextField filePath;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

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
}
