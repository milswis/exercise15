package utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FilesystemUtilities {
    public static void saveToFile(String filename, LocalDateTime[] dates, String[] records, DateTimeFormatter formatter) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(filename));
            for (int i = 0; i < dates.length; i++) {
                if (dates[i] != null) {
                    out.println(dates[i].format(formatter));
                    out.println(records[i].trim());
                    out.println();
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Помилка збереження у файл.");
        }
    }

    public static void loadFromFile(String filename, LocalDateTime[] dates, String[] records, DateTimeFormatter formatter) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line;
            int index = 0;

            while ((line = in.readLine()) != null && index < dates.length) {
                if (line.trim().isEmpty()) continue;
                LocalDateTime date = LocalDateTime.parse(line.trim(), formatter);
                String text = "";

                while ((line = in.readLine()) != null && !line.trim().isEmpty()) {
                    text += line + "\n";
                }

                dates[index] = date;
                records[index] = text;
                index++;
            }

            in.close();
            System.out.println("Щоденник завантажено.");
        } catch (IOException e) {
            System.out.println("Не вдалося завантажити файл.");
        } catch (Exception e) {
            System.out.println("Помилка формату або зчитування.");
        }
    }
}