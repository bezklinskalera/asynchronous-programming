package task2;

/*2. Користувач хоче забронювати квиток на літак, перевірити
наявність місць, ціни, а потім здійснити оплату. Потрібно перевірити
наявність місць, знайти найкращу ціну і завершити бронювання.*/

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.text.DecimalFormat;

public class task2 {

    //статичне приватне поле formatter, яке форматує числа з фіксованими двома десятковими знаками
    private static final DecimalFormat formatter = new DecimalFormat("0.00");

    public static void main(String[] args) {
        // Перевірка наявності місць
        CompletableFuture<Boolean> seatAvailability = checkSeatAvailability();

        // Отримання найкращої ціни з масиву цін, якщо місця доступні
        //thenCompose бере функцію як аргумент, яка виконується після завершення seatAvailability
        CompletableFuture<Double> bestPriceFuture = seatAvailability.thenCompose(isAvailable -> {
            if (isAvailable) {
                return fetchBestPrice();
            } else {
                throw new IllegalStateException("Місць немає!");
            }
        }).exceptionally(ex -> {
            System.err.println("Помилка під час отримання ціни: " + ex.getMessage());
            return null;
        });

        // Підтвердження бронювання
        CompletableFuture<String> bookingResult = bestPriceFuture.thenCompose(price -> {
            if (price != null) {
                return confirmBooking(price);
            } else {
                return CompletableFuture.completedFuture("Бронювання не вдалося через відсутність місць або проблему з ціною.");
            }
        });

        // Комбінація задач для отримання результату
        CompletableFuture<Void> bookingProcess = CompletableFuture.allOf(seatAvailability, bookingResult);

        // Виконання всіх задач та обробка результату
        bookingProcess.thenRun(() -> {
            try {
                if (seatAvailability.get()) {
                    System.out.println("Бронювання підтверджено: " + bookingResult.get());
                }
            } catch (Exception e) {
                System.err.println("Помилка: " + e.getMessage());
            }
        }).join();
    }

    // метод використовує CompletableFuture для асинхронного перевірки наявності місць
    public static CompletableFuture<Boolean> checkSeatAvailability() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay();
            boolean isAvailable = ThreadLocalRandom.current().nextBoolean();
            System.out.println("Наявність місць: " + isAvailable);
            return isAvailable;
        });
    }

    // Симуляція пошуку найкращої ціни з масиву цін
    public static CompletableFuture<Double> fetchBestPrice() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay();
            List<Double> priceList = List.of(
                    100 + ThreadLocalRandom.current().nextDouble(50),
                    100 + ThreadLocalRandom.current().nextDouble(50),
                    100 + ThreadLocalRandom.current().nextDouble(50),
                    100 + ThreadLocalRandom.current().nextDouble(50)
            );
            System.out.println("Усі ціни: ");
            priceList.forEach(price -> System.out.println("$" + formatter.format(price)));
            //знаходить найменшу ціну з колекції priceList або генерує виняток, якщо колекція порожня
            double lowestPrice = priceList.stream().min(Double::compareTo).orElseThrow();
            System.out.println("Найкраща ціна: $" + formatter.format(lowestPrice));
            return lowestPrice;
        });
    }

    // Симуляція бронювання квитка
    public static CompletableFuture<String> confirmBooking(double price) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay();
            String confirmationId = "ID бронювання: " + ThreadLocalRandom.current().nextInt(1000, 9999);
            System.out.println("Квиток заброньовано за ціною: $" + formatter.format(price));
            return confirmationId;
        });
    }

    // Допоміжний метод для симуляції затримки
    private static void simulateDelay() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(1, 3));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
