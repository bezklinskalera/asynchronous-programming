//Клас, що імплементує інтерфейс Runnable, щоб симулювати процес, де відправник приходить на пошту, стає в чергу,
// і працівник пошти обробляє його замовлення.


//Алгоритм. Відправник заходить у відділення, встає в чергу (в черзі максимум 3 людини),
// працівник обробляє замовлення, надходить сповіщення отримувачу, відправник йде додому.
// Інші відправники можуть заходити до відділення, але в черзі не більше трьох.
// Вони стають у чергу і очікують, поки працівник звільниться, так як він один
public class Senders implements Runnable {

    //Перевизначенний метод run()
    @Override
    public void run() {
        try {
            //System.err -  це стандартний потік для виведення повідомлень про помилки або інші важливі повідомлення
            //Thread.currentThread().getName() — цей метод повертає ім'я поточного потоку, який виконується.
            System.err.printf("Відправник з потоку %s прийшов і бажає відправити листа \n", Thread.currentThread().getName());
            //Thread.sleep() — викликається для того, щоб призупинити (поставити на паузу) виконання поточного потоку
            Thread.sleep(500);

            //acquire() — метод класу Semaphore. Коли викликається acquire(), потік намагається зайняти одне з доступних "місць" семафора
            Main.tables.acquire();
            System.out.printf("Відправник з потоку %s став в чергу \n", Thread.currentThread().getName());
            Thread.sleep(20);

            Main.postalWorker.acquire();
            System.out.printf("Працівник пошти обробляє замовлення віправника з потоку %s \n", Thread.currentThread().getName());
            Thread.sleep(2000);

            System.out.printf("Працівник пошти відправив посилку відправника з потоку %s \n", Thread.currentThread().getName());
            Thread.sleep(20);
            //release() - протилежний до acquire()
            Main.postalWorker.release();

            System.out.printf("Отримувачу (відправника з потоку %s) прийшло сповіщення про відправлення посилки \n", Thread.currentThread().getName());
            Thread.sleep(20);

            System.out.printf("Відправник з потоку %s вийшов з відділення \n", Thread.currentThread().getName());
            Thread.sleep(20);
            Main.tables.release();

        }
        //InterruptedException — це виключення, яке виникає, коли потік переривається іншим потоком під час виконання таких операцій,
        // як очікування (наприклад, під час виклику Thread.sleep() або семафорів).
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
