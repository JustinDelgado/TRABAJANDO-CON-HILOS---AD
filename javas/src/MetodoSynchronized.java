public class MetodoSynchronized {
    private static int visitas = 0;

    private static synchronized void registrarVisita() {
        visitas++;
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable tarea = () -> {
            for (int i = 0; i < 1000; i++) registrarVisita();
        };
        Thread a = new Thread(tarea);
        Thread b = new Thread(tarea);
        a.start(); b.start();
        a.join(); b.join();
        System.out.println("Visitas registradas: " + visitas);
    }
}