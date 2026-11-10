import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityInversion {
    private static final ReentrantLock recurso = new ReentrantLock();
    private static final CountDownLatch bajoTieneLock = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        Thread bajo = new Thread(() -> {
            recurso.lock();
            try {
                System.out.println("Baja prioridad: obtiene el recurso.");
                bajoTieneLock.countDown();
                try { Thread.sleep(700); } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Baja prioridad: libera el recurso.");
            } finally { recurso.unlock(); }
        }, "Bajo");

        Thread alto = new Thread(() -> {
            try { bajoTieneLock.await(); } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); return;
            }
            System.out.println("Alta prioridad: solicita el recurso y queda esperando.");
            recurso.lock();
            try { System.out.println("Alta prioridad: entra y utiliza el recurso."); }
            finally { recurso.unlock(); }
        }, "Alto");

        Thread medio = new Thread(() -> {
            try { bajoTieneLock.await(); Thread.sleep(100); } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); return;
            }
            System.out.println("Prioridad media: ejecuta trabajo independiente.");
            long fin = System.nanoTime() + 150_000_000L;
            while (System.nanoTime() < fin) { /* trabajo de CPU simulado */ }
        }, "Medio");

        bajo.setPriority(Thread.MIN_PRIORITY);
        medio.setPriority(Thread.NORM_PRIORITY);
        alto.setPriority(Thread.MAX_PRIORITY);
        bajo.start();
        alto.start();
        medio.start();
        bajo.join(); medio.join(); alto.join();
        System.out.println("Fin de la demostración.");
    }
}