import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ColaConCerrojo {
    private static final Queue<String> cola = new ArrayDeque<>();
    private static final int CAPACIDAD = 2;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition noVacia = lock.newCondition();
    private static final Condition noLlena = lock.newCondition();

    static void agregar(String elemento) throws InterruptedException {
        lock.lock();
        try {
            while (cola.size() == CAPACIDAD) noLlena.await();
            cola.add(elemento);
            System.out.println("En cola: " + elemento);
            noVacia.signal();
        } finally { lock.unlock(); }
    }

    static String retirar() throws InterruptedException {
        lock.lock();
        try {
            while (cola.isEmpty()) noVacia.await();
            String elemento = cola.remove();
            noLlena.signal();
            return elemento;
        } finally { lock.unlock(); }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread productor = new Thread(() -> {
            try {
                for (int i = 1; i <= 4; i++) agregar("Documento-" + i);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread consumidor = new Thread(() -> {
            try {
                for (int i = 1; i <= 4; i++)
                    System.out.println("Imprimiendo: " + retirar());
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        consumidor.start(); productor.start();
        productor.join(); consumidor.join();
    }
}