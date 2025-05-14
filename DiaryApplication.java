package diary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import utils.FilesystemUtilities;

public class DiaryApplication {
    public static final int maxRecords = 50;
    public static LocalDateTime[] dates = new LocalDateTime[maxRecords];
    public static String[] records = new String[maxRecords];
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void run(Scanner sc) {
        System.out.println("Вітаю! Бажаєте відкрити існуючий щоденник чи створити новий?");
        System.out.println("1. Відкрити існуючий");
        System.out.println("2. Створити новий");
        System.out.print("Ваш вибір: ");

        String filename = null;
        int startChoice = readInt(sc);

        if (startChoice == 1) {
            System.out.print("Введіть шлях до файлу: ");
            filename = sc.nextLine();
            FilesystemUtilities.loadFromFile(filename, dates, records, formatter);
        } else {
            System.out.print("Введіть ім’я нового файлу (буде створений пізніше): ");
            filename = sc.nextLine();
        }

        boolean end = false;
        while (!end) {
            try {
                System.out.println("\n=== МІЙ ЩОДЕННИК ===");
                System.out.println("1. Додати запис");
                System.out.println("2. Видалити запис за датою і часом");
                System.out.println("3. Переглянути всі записи");
                System.out.println("4. Переглянути запис за певною датою і часом");
                System.out.println("5. Вийти");
                System.out.print("Ваш вибір: ");

                int choice = readInt(sc);
                switch (choice) {
                    case 1 -> addRecord(sc);
                    case 2 -> deleteRecord(sc);
                    case 3 -> showRecords();
                    case 4 -> viewRecordByDate(sc);
                    case 5 -> {
                        end = true;
                        savePrompt(sc);
                    }
                    default -> System.out.println("Невірний вибір.");
                }
            } catch (Exception e) {
                System.out.println("Помилка введення. Спробуйте ще раз.");
                sc.nextLine();
            }
        }
    }

    public static int readInt(Scanner sc) {
        while (true) {
            try {
                int x = sc.nextInt();
                sc.nextLine();
                return x;
            } catch (Exception e) {
                System.out.print("Введіть число: ");
                sc.nextLine();
            }
        }
    }

    public static void addRecord(Scanner sc) {
        int index = findEmptyIndex();
        if (index == -1) {
            System.out.println("Щоденник заповнений.");
            return;
        }

        LocalDateTime date = inputValidDateTime(sc);
        System.out.println("Введіть текст запису (ENTER двічі для завершення):");
        String text = "";
        while (true) {
            String line = sc.nextLine();
            if (line.isEmpty()) break;
            text += line + "\n";
        }

        dates[index] = date;
        records[index] = text;
        System.out.println("Запис додано.");
    }

    public static void deleteRecord(Scanner sc) {
        LocalDateTime target = inputValidDateTime(sc);
        boolean found = false;

        for (int i = 0; i < dates.length; i++) {
            if (dates[i] != null && dates[i].equals(target)) {
                dates[i] = null;
                records[i] = null;
                found = true;
                System.out.println("Запис видалено.");
                break;
            }
        }

        if (!found) {
            System.out.println("Запис не знайдено.");
        }
    }

    public static void showRecords() {
        boolean has = false;
        for (int i = 0; i < dates.length; i++) {
            if (dates[i] != null) {
                has = true;
                System.out.println("Дата і час: " + dates[i].format(formatter));
                System.out.println("Запис:\n" + records[i]);
                System.out.println("--------------------");
            }
        }
        if (!has) {
            System.out.println("Немає записів.");
        }
    }

    public static void viewRecordByDate(Scanner sc) {
        LocalDateTime target = inputValidDateTime(sc);
        boolean found = false;

        for (int i = 0; i < dates.length; i++) {
            if (dates[i] != null && dates[i].equals(target)) {
                found = true;
                System.out.println("Дата і час: " + dates[i].format(formatter));
                System.out.println("Запис:\n" + records[i]);
                break;
            }
        }

        if (!found) {
            System.out.println("Запис не знайдено.");
        }
    }

    public static int findEmptyIndex() {
        for (int i = 0; i < maxRecords; i++) {
            if (dates[i] == null) return i;
        }
        return -1;
    }

    public static LocalDateTime inputValidDateTime(Scanner sc) {
        while (true) {
            System.out.print("Введіть дату і час у форматі yyyy-MM-dd HH:mm: ");
            String input = sc.nextLine();
            try {
                return LocalDateTime.parse(input, formatter);
            } catch (Exception e) {
                System.out.println("Невірний формат. Спробуйте ще раз.");
            }
        }
    }

    public static void savePrompt(Scanner sc) {
        System.out.print("Бажаєте зберегти щоденник? (так/ні): ");
        String answer = sc.nextLine().toLowerCase();
        if (answer.equals("так")) {
            System.out.print("Введіть шлях до файлу для збереження: ");
            String path = sc.nextLine();
            FilesystemUtilities.saveToFile(path, dates, records, formatter);
            System.out.println("Щоденник збережено.");
        } else {
            System.out.println("Зміни не збережені.");
        }
    }
}