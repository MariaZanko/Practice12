package second;

import first.PaymentLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class main2 {
    public static void main(String[] args) {
        Path inputPath = Paths.get("payments_data.csv");
        Path reportPath = Paths.get("report.txt");

        try {

            // зчитуємо дані
            PaymentLoader.LoadResult result = PaymentLoader.loadWithStats(inputPath);

            // робим звіт
            PaymentReportWriter.writeReport(reportPath, result.payments(), result.invalidLines());

            System.out.println("---------------ЗВІТ СФОРМОВАНО---------------");
            System.out.println("Файл збережено: " + reportPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
}