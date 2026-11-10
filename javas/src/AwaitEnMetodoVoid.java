import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class AwaitEnMetodoVoid {
    private static final CyclicBarrier barrera = new CyclicBarrier(2);

    // Este método es void; internamente invoca await(), que devuelve un int.
    static void esperarFase() throws InterruptedException, BrokenBarrierException {
        barrera.await();
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable tarea = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " listo.");
                esperarFase();
                System.out.println(Thread.currentThread().getName() + " continúa.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                System.out.println("La barrera se rompió.");
            }
        };
        Thread uno = new Thread(tarea, "Hilo-1");
        Thread dos = new Thread(tarea, "Hilo-2");
        uno.start(); dos.start();
        uno.join(); dos.join();
    }
}