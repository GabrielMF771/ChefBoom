package br.com.gabriel.chefboom.world;

public class ClienteAtendidoThread {

    public static void notificarClienteAtendido() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000); // espera 1 segundo

                // Mensagem após atendimento
                System.out.println("Cliente foi atendido!");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start(); // Inicia a thread
    }
}
