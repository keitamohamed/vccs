package com.keita.vccs.util;

import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.Record;
import com.keita.vccs.blueprint.ScoreTable;
import com.keita.vccs.blueprint.Student;
import com.keita.vccs.message.Notify;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Export {
    private static XSSFWorkbook wb;
    private static XSSFSheet sheet;
    private static XSSFRow row;
    private static XSSFFont font;
    private static XSSFCell cell;
    private static XSSFCellStyle style;
    private static FileChooser chooser;
    private static File file;
    private static FileOutputStream out;

    public static void exportListStudent(ObservableList<Class> classes) {
        try {
            chooser = new FileChooser();
            chooser.setTitle("Export Directory");
            file = chooser.showSaveDialog(new Stage());

            if (file != null) {
                wb = new XSSFWorkbook();

                sheet = wb.createSheet("List Of Student");
                font = wb.createFont();
                style = wb.createCellStyle();
                font.setFontName("Times New Roman");
                font.setFontHeight(20);
                font.setColor(XSSFFont.COLOR_RED);

                row = sheet.createRow(0);
                row.createCell(0).setCellValue("EMP (ID)");
                row.createCell(1).setCellValue("Class ID");
                row.createCell(2).setCellValue("Name");
                row.createCell(3).setCellValue("Email");
                row.createCell(4).setCellValue("Phone Num");

                for (int i = 0; i <= 5; i++) {
                    sheet.autoSizeColumn(i);
                }

                int index = 1;
                for (int i = 0; i < classes.size(); i++) {
                    for (Student stud : classes.get(i).getStudent()) {
                        row = sheet.createRow(index);
                        row.createCell(0).setCellValue(stud.getId());
                        row.createCell(1).setCellValue(classes.get(i).getClassID());
                        row.createCell(2).setCellValue(stud.getName());
                        row.createCell(3).setCellValue(stud.getEmail());
                        row.createCell(4).setCellValue(stud.getPhone());

                        index++;
                    }
                }
                out = new FileOutputStream(file + ".xlsx");
                wb.write(out);
                out.close();
                Notify.export();
            }

        }catch (NullPointerException | IOException ex) {
            System.out.println("Print this");

        }
    }

    public static void exportStudentGrade(ObservableList<ScoreTable> scores) {
        try {
            chooser = new FileChooser();
            chooser.setTitle("Student's Grade");
            file = chooser.showSaveDialog(new Stage());

            if (file != null) {

                wb = new XSSFWorkbook();
                sheet = wb.createSheet("Grade");

                row = sheet.createRow(0);
                row.createCell(0).setCellValue("EMP (ID)");
                row.createCell(1).setCellValue("Class ID");
                row.createCell(2).setCellValue("Name");
                row.createCell(3).setCellValue("Class Name");
                row.createCell(4).setCellValue("Score");

                for (int i = 0; i <= 5; i++) {
                    sheet.autoSizeColumn(i);
                }

                int index = 1;
                for (ScoreTable score : scores) {
                    row = sheet.createRow(index);
                    row.createCell(0).setCellValue(score.getId());
                    row.createCell(1).setCellValue(score.getClassID());
                    row.createCell(2).setCellValue(score.getName());
                    row.createCell(3).setCellValue(score.getScoreName());
                    row.createCell(4).setCellValue(Integer.parseInt(score.getScore()));

                    index++;
                }
                out = new FileOutputStream(file + ".xlsx");
                wb.write(out);
                out.close();
                Notify.export();
            }

        }catch (NullPointerException | IOException ex) {
            System.out.println("Print this");
        }
    }

    public static void exportCourseHistory(TableView<Record> table) {
        try {
            chooser = new FileChooser();
            chooser.setTitle("Export Directory");
            file = chooser.showSaveDialog(new Stage());

            if (file != null) {
                wb = new XSSFWorkbook();

                sheet = wb.createSheet("Course History");

                row = sheet.createRow(0);
                row.createCell(0).setCellValue("CLASS ID");
                row.createCell(1).setCellValue("Class NAME");
                row.createCell(2).setCellValue("UNITES");
                row.createCell(3).setCellValue("GRADE");
                row.createCell(4).setCellValue("TERM");
                row.createCell(5).setCellValue("YEAR");

                for (int i = 0; i <= 6; i++) {
                    sheet.autoSizeColumn(i);
                }

                int index = 1;
                for (int i = 0; i < table.getColumns().size(); i++) {
                    row = sheet.createRow(index);
                    row.createCell(0).setCellValue(table.getItems().get(i).getClassID());
                    row.createCell(1).setCellValue(table.getItems().get(i).getClassName());
                    row.createCell(2).setCellValue(table.getItems().get(i).getUnite());
                    row.createCell(3).setCellValue(table.getItems().get(i).getGrade());
                    row.createCell(4).setCellValue(table.getItems().get(i).getTerm());
                    row.createCell(5).setCellValue(table.getItems().get(i).getYear());

                    index++;
                }

                out = new FileOutputStream(file + ".xlsx");
                wb.write(out);
                out.close();
                Notify.export();
            }

        }catch (NullPointerException | IOException ex) {
        }
    }
}
