package com.keita.vccs.associate;

import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.ScoreTable;
import com.keita.vccs.blueprint.Student;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
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

        }catch (IOException ex) {

        }
    }

    public static void exportStudentGrade(ObservableList<ScoreTable> scores) {
        try {
            chooser = new FileChooser();
            chooser.setTitle("Student's Grade");
            file = chooser.showSaveDialog(new Stage());

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

        }catch (IOException ex) {

        }
    }
}
