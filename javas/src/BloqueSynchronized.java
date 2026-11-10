public class BloqueSynchronized {
    private static int contador = 0;
    private static final Object candado = new Object();

    public static void main(String[] args) throws InterruptedException {
        Runnable tarea = () -> {
            for (int i = 0; i < 1000; i++) {
                synchronized (candado) { contador++; }
            }
        };
        Thread a = new Thread(tarea);
        Thread b = new Thread(tarea);
        a.start(); b.start();
        a.join(); b.join();
        System.out.println("Contador final: " + contador);
    }
}