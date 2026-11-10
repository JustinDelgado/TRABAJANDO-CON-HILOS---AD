import java.util.concurrent.atomic.AtomicInteger;

public class AtomicAcquireRelease {
    private static final AtomicInteger estado = new AtomicInteger(0);
    private static int datoCompartido = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread escritor = new Thread(() -> {
            datoCompartido = 2026;
            estado.setRelease(1); // Publica el dato antes de cambiar el estado.
            System.out.println("Escritor: dato publicado.");
        });

        Thread lector = new Thread(() -> {
            while (estado.getAcquire() == 0) {
                Thread.onSpinWait();
            }
            System.out.println("Lector: dato recibido = " + datoCompartido);
        });

        lector.start();
        escritor.start();
        escritor.join();
        lector.join();
    }
}