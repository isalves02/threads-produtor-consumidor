import java.util.Random;
import java.util.concurrent.Semaphore;

public class Writer extends Thread {
    public Semaphore mutex; // controla o acesso a 'rc'
    public Semaphore db;    // controla o acesso à base de dados
    public Integer rc;  
    public String[] books;
    public Random random = new Random();

    public Writer(Semaphore mutex, Semaphore db, Integer rc, String[] books) {
        this.mutex = mutex;
        this.db = db;
        this.rc = rc;
        this.books = books;
    }

    /*
     * Primeiro ele abre o semaforo para poder escrever ("abre o livro"), escreve
     * e depois fecha p semaforo ("fecha o livro") para que o  leitor possa ler
    */
    @Override
    public void run() {
        while (true) {
            try {

                this.db.acquire();

                writeBooks(); // Escreve, escreve, escreve...
                
                this.db.release(); 

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeBooks() {
        System.out.println(String.format("A %s está escrevendo livros", this.getName()));

        int number_max_books = 1 + this.random.nextInt(9);
        int number_min_books = 1 + this.random.nextInt(number_max_books);
        System.out.println("min: " + number_min_books + " | max: " + number_max_books);
        for(int i = number_min_books; i <= number_max_books; i++) {
            if(this.books[i] == null) {
                this.books[i] = "livro";
            }
        }

        for(String livro : this.books) {
            System.out.print(livro + " ");
        }
        System.out.println("\n");

    }
}
