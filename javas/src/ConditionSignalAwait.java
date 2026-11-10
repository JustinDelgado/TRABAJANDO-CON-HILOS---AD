import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionSignalAwait {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition datoDisponible = lock.newCondition();
    private static boolean listo = false;
    private static String mensaje;

    public static void main(String[] args) throws InterruptedException {
        Thread consumidor = new Thread(() -> {
            lock.lock();
            try {
                while (!listo) {
                    System.out.println("Consumidor: espera el mensaje.");
                    datoDisponible.await();
                }
                System.out.println("Consumidor recibe: " + mensaje);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally { lock.unlock(); }
        });

        Thread productor = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); return;
            }
            lock.lock();
            try {
                mensaje = "Pedido confirmado";
                listo = true;
                System.out.println("Productor: envía señal.");
                datoDisponible.signal();
            } finally { lock.unlock(); }
        });

        consumidor.start(); productor.start();
        consumidor.join(); productor.join();
    }
}