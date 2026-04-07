package first;

import java.io.BufferedReader;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;


public class PaymentLoader {

    // Результат, який повертає і список, і лічильник помилок
    public record LoadResult(List<Payment> payments, int invalidLines) {

    }

    public static LoadResult loadWithStats(Path csvPath) {

        List<Payment> payments = new ArrayList<>();
        int invalidLines = 0;


        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {

            String line;

            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {

                // щоб він не рахував порожні рядки та пропуски а тільки вміст
                // Пропускаємо перший рядок

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // ігноруємо порожні рядки
                if (line.trim().isEmpty()) {
                    invalidLines++;
                    continue;
                }

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 4) throw new Exception("Мало даних");

                    String id = parts[0].trim();
                    String email = parts[1].trim();

                    PaymentStatus status = PaymentStatus.valueOf(parts[2].trim().toUpperCase());

                    long amount = Long.parseLong(parts[3].trim());

                    payments.add(new Payment(id, email, status, amount));
                } catch (Exception e) {

                    invalidLines++;
                }
            }
        } catch (IOException e) {

            System.err.println("Помилка доступу до файлу: " + e.getMessage());
        }

        return new LoadResult(payments, invalidLines);
    }
}