import java.util.concurrent.Semaphore;

public class Writer extends Thread {
    public Semaphore mutex; // controla o acesso a 'rc'
    public Semaphore db;    // controla o acesso à base de dados
    public Integer rc;  

    public Writer(Semaphore mutex, Semaphore db, Integer rc) {
        this.mutex = mutex;
        this.db = db;
        this.rc = rc;
    }

    /*
     * Primeiro ele abre o semaforo para poder escrever ("abre o livro"), escreve
     * e depois fecha p semaforo ("fecha o livro") para que o  leitor possa ler
    */
    @Override
    public void run() {
        while (true) {
            try {

                this.db.acquire(); //Abre o semaforo para poder escrever
                System.out.println("\nLivro aberto - " + this.getName());

                writeDataBase(); // Escreve, escreve, escreve...

                this.db.release(); // Fecha o semaforo para que não possa mais escrever
                System.out.println("Livro fechado - " + this.getName() + "\n");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeDataBase() {
        System.out.println(String.format("A %s está escrevendo no livro", this.getName()));
    }
}
