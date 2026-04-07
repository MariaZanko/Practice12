package second;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentReportWriter {

    public static void writeReport(Path out, List<Payment> payments, int invalidLines) throws IOException {
        // Створюємо тимчасовий файл для атомарності
        Path tempFile = out.resolveSibling(out.getFileName() + ".tmp");

        // Рахуємо суму тільки для PAID
        long paidTotal = payments.stream()
                .filter(p -> p.status() == PaymentStatus.PAID)
                .mapToLong(Payment::amountCents)
                .sum();

        // Групуємо за статусами для статистики
        Map<PaymentStatus, Long> counts = payments.stream()
                .collect(Collectors.groupingBy(Payment::status, Collectors.counting()));

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write("invalidLines=" + invalidLines);
            writer.newLine();
            writer.write("paidTotalCents=" + paidTotal);
            writer.newLine();
            writer.write(String.format("NEW=%d, PAID=%d, FAILED=%d",
                    counts.getOrDefault(PaymentStatus.NEW, 0L),
                    counts.getOrDefault(PaymentStatus.PAID, 0L),
                    counts.getOrDefault(PaymentStatus.FAILED, 0L)));
            writer.newLine();
        }

        // Замінюємо старий файл новим (атомарно)
        Files.move(tempFile, out, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
}