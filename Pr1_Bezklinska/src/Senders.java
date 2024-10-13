//Клас, що імплементує інтерфейс Runnable, щоб симулювати процес, де відправник приходить на пошту, стає в чергу, і працівник пошти обробляє його замовлення.
public class Senders implements Runnable {

    //Перевизначенний метод run()
    @Override
    public void run() {
        try {
            System.err.printf("Відправник з потоку %s прийшов і бажає відправити листа \n", Thread.currentThread().getName());
            Thread.sleep(500);

            Main.tables.acquire();
            System.out.printf("Відправник з потоку %s став в чергу \n", Thread.currentThread().getName());
            Thread.sleep(20);


            Main.postalWorker.acquire();
            System.out.printf("Працівник пошти обробляє замовлення віправника з потоку %s \n", Thread.currentThread().getName());
            Thread.sleep(2000);

            System.out.printf("Працівник пошти відправив посилку відправника з потоку %s \n", Thread.currentThread().getName());
            Thread.sleep(20);
            Main.postalWorker.release();

            System.out.printf("Отримувачу (відправника з потоку %s) прийшло сповіщення про відправлення посилки \n", Thread.currentThread().getName());
            Thread.sleep(20);


            System.out.printf("Відправник з потоку %s вийшов з відділення \n", Thread.currentThread().getName());
            Thread.sleep(20);
            Main.tables.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
