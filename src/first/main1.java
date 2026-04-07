package first;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class main1 {
    public static void main(String[] args) {

        Path path = Paths.get("payments_data.csv");


        String csvContent = """
                id,email,status,amountCents
                pay-101,petya@ukr.net,PAID,15000
                pay-102,masha@gmail.com,NEW,2000
                ERROR_LINE,no_email,WRONG_STATUS,abc
                pay-103,ivan@ukma.edu,FAILED,0
                """;

        try {
            // Записуємо дані у файл
            Files.writeString(path, csvContent);


            PaymentLoader.LoadResult result = PaymentLoader.loadWithStats(path);

            // результат, просто текст

            System.out.println("Валідних платежів: " + result.payments().size());
            System.out.println("Невалідних рядків (ігноровано): " + result.invalidLines());

            System.out.println("Деталі успішних записів:");
            result.payments().forEach(p ->
                    System.out.println("ID: " + p.id() + " | Email: " + p.email() + " | Status: " + p.status()));

        } catch (IOException e) {
            System.out.println("Помилка при роботі з файлом: " + e.getMessage());
        }
    }
}