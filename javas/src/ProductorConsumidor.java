import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProductorConsumidor {
    private static final int CAPACIDAD = 3;
    private static final Queue<Integer> buffer = new ArrayDeque<>();
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition noVacio = lock.newCondition();
    private static final Condition noLleno = lock.newCondition();

    static void producir(int valor) throws InterruptedException {
        lock.lock();
        try {
            while (buffer.size() == CAPACIDAD) {
                System.out.println("Buffer lleno: productor espera.");
                noLleno.await();
            }
            buffer.add(valor);
            System.out.println("Productor genera: " + valor + " | buffer=" + buffer);
            noVacio.signal();
        } finally { lock.unlock(); }
    }

    static int consumir() throws InterruptedException {
        lock.lock();
        try {
            while (buffer.isEmpty()) {
                System.out.println("Buffer vacío: consumidor espera.");
                noVacio.await();
            }
            int valor = buffer.remove();
            System.out.println("Consumidor procesa: " + valor + " | buffer=" + buffer);
            noLleno.signal();
            return valor;
        } finally { lock.unlock(); }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread productor = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) {
                    producir(i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Productor");

        Thread consumidor = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) {
                    consumir();
                    Thread.sleep(250);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Consumidor");

        productor.start();
        consumidor.start();
        productor.join();
        consumidor.join();
        System.out.println("Proceso terminado. Elementos pendientes: " + buffer.size());
    }
}