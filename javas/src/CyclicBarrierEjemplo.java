import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class CyclicBarrierEjemplo {
    public static void main(String[] args) throws InterruptedException {
        int participantes = 3;
        CyclicBarrier barrera = new CyclicBarrier(participantes,
                () -> System.out.println("Todos llegaron: comienza la siguiente fase."));

        Runnable tarea = () -> {
            String nombre = Thread.currentThread().getName();
            try {
                System.out.println(nombre + " realiza su preparación.");
                Thread.sleep(200);
                int indice = barrera.await();
                System.out.println(nombre + " continúa. Índice de llegada: " + indice);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                System.out.println(nombre + ": barrera interrumpida.");
            }
        };

        Thread a = new Thread(tarea, "Equipo-A");
        Thread b = new Thread(tarea, "Equipo-B");
        Thread c = new Thread(tarea, "Equipo-C");
        a.start(); b.start(); c.start();
        a.join(); b.join(); c.join();
    }
}