/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opticalreader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.util.ArrayList;
import java.text.DecimalFormat;


/**
 *
 * @author Laptop
 */
public class OpticalReader {




    public static void main(String[] args) {
        DecimalFormat DecimalFormat = new DecimalFormat(".#####");
        ArrayList<ArrayList<Integer[]>> courseGrades = new ArrayList<>();
        ArrayList<String> courseNames = new ArrayList<>();
        File[] ListFiles;
        ListFiles = new File("C:\\Users\\Laptop\\Documents\\Courses").listFiles();
        if (ListFiles == null) {
            System.out.println("The file is not found");
            return;
        }
        new File("./survey_calc").mkdir();
        for (File file : ListFiles) {
            if (!file.getName().contains(".txt"))
                continue;
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                String filename = file.getName().substring(0, file.getName().length() - 4);
                courseNames.add(filename);
                ArrayList<Integer[]> grades = parseLines(lines);
                courseGrades.add(grades);
                File outFile = new File("./survey_calc/" + filename + ".csv");
                outFile.delete();
                outFile.createNewFile();
                try (FileWriter fileWriter = new FileWriter(outFile)) {
                    fileWriter.write("Student");
                    for (int i = 0; i < 34; i++) {
                        fileWriter.write(",Q " + (i + 1));
                    }
                    fileWriter.write('\n');
                    int i = 1;
                    for (Integer[] grade : grades) {
                        fileWriter.write("Student" + (i++));
                        for (Integer integer : grade) {
                            if (integer == -1)
                                fileWriter.write(", ");
                            else
                                fileWriter.write(", " + integer);
                        }
                        fileWriter.write('\n');
                    }
                }
            } catch (IOException e) {
                System.out.println("Reading file failed" + e.getMessage());
            }
        }
        try {
            File outFile = new File("./survey_calc/Survey_Average_Calc.csv");
            outFile.delete();
            outFile.createNewFile();
            try (FileWriter fileWriter = new FileWriter(outFile)) {
                fileWriter.write("CourseName, StudentsNo ");
                for (int i = 0; i < 34; i++) {
                    fileWriter.write(",Q " + (i + 1));
                }
                fileWriter.write('\n');
                for (int i = 0; i < courseNames.size(); i++) {
                    fileWriter.write(courseNames.get(i));
                    fileWriter.write(',');
                    fileWriter.write(String.valueOf(courseGrades.get(i).size()));
                    double[] grades = new double[34];
                    for (int j = 0; j < 34; j++) {
                        int count = 0;
                        for (Integer[] integers : courseGrades.get(i)) {
                            if (integers[j] == -1)
                                continue;
                            grades[j] += integers[j];
                            count++;
                        }
                        if (count != 0)
                            grades[j] /= count;
                    }
                    for (int j = 0; j < 34; j++) {
                        fileWriter.write(',');
                        if (grades[j] == 0)
                            fileWriter.write(' ');
                        else {
                            fileWriter.write(String.valueOf(DecimalFormat.format(grades[j])));
                        }
                    }
                    fileWriter.write('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Integer[]> parseLines(List<String> lines) {
        ArrayList<Integer[]> studentGrades = new ArrayList<>();
        for (String line : lines) {
            line = line.substring(10);
            Integer[] grades = new Integer[line.length()];
            for (int i = 0; i < line.length(); i++) {
                int grade;
                switch (line.charAt(i)) {
                    case 'A':
                        grade = 4;
                        break;
                    case 'B':
                        grade = 3;
                        break;
                    case 'C':
                        grade = 2;
                        break;
                    case 'D':
                        grade = 1;
                        break;
                    case 'E':
                        grade = 0;
                        break;
                    default:
                        grade = -1;
                }
                grades[i] = grade;
            }
            studentGrades.add(grades);
        }
        return studentGrades;
    }
}