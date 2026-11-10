public class MonitorEjemplo {
    private static class Monitor {
        private boolean hayDato = false;
        private String dato;

        synchronized void producir(String valor) {
            dato = valor;
            hayDato = true;
            System.out.println("Monitor: dato producido.");
            notifyAll();
        }

        synchronized String consumir() throws InterruptedException {
            while (!hayDato) {
                System.out.println("Consumidor espera dentro del monitor.");
                wait();
            }
            hayDato = false;
            return dato;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread consumidor = new Thread(() -> {
            try { System.out.println("Consumido: " + monitor.consumir()); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread productor = new Thread(() -> {
            try { Thread.sleep(300); } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); return;
            }
            monitor.producir("Actualización de inventario");
        });
        consumidor.start(); productor.start();
        consumidor.join(); productor.join();
    }
}