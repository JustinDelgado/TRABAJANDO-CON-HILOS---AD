import java.util.concurrent.Semaphore;

public class SemaforoMultiplesPermisos {
    private static final Semaphore accesos = new Semaphore(2);

    public static void main(String[] args) throws InterruptedException {
        Runnable consulta = () -> {
            try {
                accesos.acquire();
                try {
                    System.out.println(Thread.currentThread().getName()
                            + " ocupa un cupo. Disponibles: " + accesos.availablePermits());
                    Thread.sleep(400);
                } finally {
                    accesos.release();
                    System.out.println(Thread.currentThread().getName() + " libera un cupo.");
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };
        Thread[] usuarios = new Thread[5];
        for (int i = 0; i < usuarios.length; i++) {
            usuarios[i] = new Thread(consulta, "Usuario-" + (i + 1));
            usuarios[i].start();
        }
        for (Thread t : usuarios) t.join();
    }
}