package com.keita.vccs.message;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notify {
    private static Alert al;
    private static Notifications nf;

    public Notify(){}


    public static void errorRequire(String title, String message) {
        al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle(title);
        al.setHeaderText(null);
        al.setContentText(message);
        al.showAndWait();
    }

    public static void notification(String title, String message) {
        ImageView view = new ImageView(Notify.class.getResource("../images/error.png").toExternalForm());
        view.setFitHeight(80);
        view.setFitWidth(80);

        Notifications.create()
                .darkStyle()
                .title(title)
                .text(message)
                .position(Pos.CENTER)
                .graphic(view)
                .hideAfter(Duration.seconds(20))
                .show();
    }

    public static void exist(String title, String message) {
        Notifications.create()
                .darkStyle()
                .title(title)
                .text(message)
                .position(Pos.CENTER)
                .hideAfter(Duration.seconds(5))
                .show();
    }

    public static void successful(String title, String message) {
        ImageView view = new ImageView(Notify.class.getResource("../images/Ok.png").toExternalForm());
        Notifications.create()
                .darkStyle()
                .title(title)
                .text(message)
                .position(Pos.CENTER)
                .graphic(view)
                .hideAfter(Duration.seconds(5))
                .show();
    }

    public static void export() {
        Notifications.create()
                .title("Export Successfully")
                .text("Successfully exported data as an excel file")
                .position(Pos.CENTER)
                .darkStyle()
                .hideAfter(Duration.seconds(4))
                .show();
    }
}
