import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockEjemplo {
    private static final ReentrantLock lock = new ReentrantLock();
    private static int saldo = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable tarea = () -> {
            for (int i = 0; i < 1000; i++) {
                lock.lock();
                try { saldo++; }
                finally { lock.unlock(); }
            }
        };
        Thread t1 = new Thread(tarea, "Hilo-1");
        Thread t2 = new Thread(tarea, "Hilo-2");
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Saldo final esperado: 2000");
        System.out.println("Saldo final obtenido: " + saldo);
    }
}