import java.util.concurrent.Semaphore;

public class SemaforoBinario {
    private static final Semaphore semaforo = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        Runnable tarea = () -> {
            try {
                semaforo.acquire();
                try {
                    System.out.println(Thread.currentThread().getName()
                            + " entra en la zona crítica.");
                    Thread.sleep(300);
                    System.out.println(Thread.currentThread().getName()
                            + " sale de la zona crítica.");
                } finally { semaforo.release(); }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        Thread a = new Thread(tarea, "Hilo-A");
        Thread b = new Thread(tarea, "Hilo-B");
        Thread c = new Thread(tarea, "Hilo-C");
        a.start(); b.start(); c.start();
        a.join(); b.join(); c.join();
    }
}