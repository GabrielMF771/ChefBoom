package br.com.gabriel.chefboom.console;

public interface Command {
    /**
     * Executa o comando.
     * @param args Argumentos passados para o comando.
     * @return Uma mensagem de resultado para exibir no console.
     */
    String execute(String[] args);
}
