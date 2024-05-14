package org.jenko.gui.mylocale;

import java.io.*;

/**
 * Класс для записи и чтения сохранённой локали
 */
class LocaleSaver {

    private static String filename = System.getProperty("user.home") + "\\MyRobotsConf" + "\\locale.config";


    /**
     * Записать локаль в файл
     */
    public static void writeLocale(int number) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(number);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при сохранении локали");
        }
    }

    /**
     * Прочитать локаль из файла
     */
    public static int readLocale() {
        int number = -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line != null) {
                number = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при чтении сохранённой локали");
        }
        return number;
    }

    private LocaleSaver() {

    }
}
