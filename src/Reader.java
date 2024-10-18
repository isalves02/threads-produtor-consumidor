import java.util.concurrent.Semaphore;

public class Reader extends Thread {
    public Semaphore mutex; // controla o acesso a 'rc'
    public Semaphore db;    // controla o acesso à base de dados
    public Integer rc; 

    public Reader(Semaphore mutex, Semaphore db, Integer rc) {
        this.mutex = mutex;
        this.db = db;
        this.rc = rc;
    }

    /*
     * Primeiro ele abre o semaforo para que a variavel 'rc' seja manipulada, 
     * inclementa + 1 e caso o seu valor seja igual a 1 ele "abre o livro"
     * e fecha o semaforo que controla o acesso a variavel 'rc' para que outra thread
     * não consiga "abrir o libro" e dê problema de inconsistência.
     * 
     * Após a leitura, ele abre novamente o semaforo para que possa manipular o 'rc',
     * decrementa - 1 e caso o seu valor seja 0 ele "fecha o livro" e fecha o semaforo que controla
     * a variavel 'rc', para que outra thread possar "ler o livro".
     * 
     * A explicação não está das melhores, mas foi como eu consegui interpretar o código 😁 👍
    */
    @Override
    public void run() {
        while (true) {
            try {

                this.mutex.acquire(); //abre o semaforo para poder manipular o rc
                this.rc++;
                if (this.rc == 1) this.db.acquire(); // Abre o "livro" para poder ler
                System.out.println("\nLivro aberto - " + this.getName());

                this.mutex.release(); // Fecha o semaforo para bloquear o acesso ao rc

                readDataBase(); // Lê, lê, lê...

                this.mutex.acquire();
                rc--;
                if (rc == 0) this.db.release(); // Fecha o semaforo para que não possa mais realizar leitura
                System.out.println("Livro fechado - " + this.getName() + "\n");
                this.mutex.release();

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readDataBase() {
        System.out.println(String.format("A %s está lendo o livro", this.getName()));
    }
}
