import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {

    // Кількість відправників в черзі
    static final Semaphore tables = new Semaphore(3);

    // Кількість працівників пошти
    static final Semaphore postalWorker = new Semaphore(1);

    static List<Thread> list = Collections.synchronizedList(new ArrayList<Thread>());

    // Графік поштового відділення
    private static boolean isAvailableHours = true;

    // Перевірка чи відкрите ще дане відділення пошти
    public static synchronized boolean isOpen () {
        return isAvailableHours;
    }

    // Закриття пошти
    public static synchronized void closePostOffice () {
        isAvailableHours = false;
        System.err.println("================Пошту закрили===================");
    }

    public static void main(String[] args) throws InterruptedException {

        System.err.println("=============Відкриття Нової Пошти================");
        Runnable postOffice = () -> {
            int i = 0;
            while (true) {
                synchronized (Main.class) {
                    // Якщо пошта вже закрита, виходимо з циклу
                    if (!isOpen()) break;
                }

                Thread thr = new Thread(new Senders(), String.valueOf(i));
                thr.start();
                list.add(thr);
                i++;

                try {
                    Thread.sleep(1000); // Новий відправник приходить щосекунди
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.err.println("=============Працівник обслуговує решту клієнтів================");
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("=============Працівник завершив обслуговування решти клієнтів================");
        };

        // Початок роботи пошти
        Thread postOfficeThread = new Thread(postOffice, "НоваПошта");
        postOfficeThread.start();

        // Час роботи пошти (6с)
        Thread.sleep(6000);
        closePostOffice();

        // Очікування завершення роботи пошти
        postOfficeThread.join();

        // Очікування завершення всіх відправників
        for (Thread thr : list) {
            thr.join();
        }

        System.err.println("=============Працівник пішов додому================");
    }
}
