import java.util.Random;
import java.util.concurrent.Semaphore;

public class Reader extends Thread {
    public Semaphore mutex; // controla o acesso a 'rc'
    public Semaphore db;    // controla o acesso à base de dados
    public Integer rc; 
    public String[] books;
    public Random random = new Random();

    public Reader(Semaphore mutex, Semaphore db, Integer rc, String[] books) {
        this.mutex = mutex;
        this.db = db;
        this.rc = rc;
        this.books = books;
    }

    /*
     * Primeiro ele abre o semaforo para que a variavel 'rc' seja manipulada, 
     * inclementa + 1 e caso o seu valor seja igual a 1 ele "pega os livros"
     * e fecha o semaforo que controla o acesso a variavel 'rc' para que outra thread
     * não consiga "pegar os livros" enquanto outra thread está pegando
     * e dê problema de inconsistência.
     * 
     * Após a leitura, ele abre novamente o semaforo para que possa manipular o 'rc',
     * decrementa - 1 e caso o seu valor seja 0 ele fecha o semaforo que controla
     * a variavel 'rc', para que outra thread possar "pegar os livros".
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
                //this.mutex.release(); // Fecha o semaforo para bloquear o acesso ao rc
                readBooks(); // Lê, lê, lê...
                //this.mutex.acquire();
                rc--;
                if (rc == 0) this.db.release(); // Fecha o semaforo para que não possa mais realizar leitura
                this.mutex.release();

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readBooks() {
        System.out.println(String.format("A %s está lendo livro(s)", this.getName()));
        int count = 0;
        int number_max_books = 1 + this.random.nextInt(9);
        int number_min_books = 1 + this.random.nextInt(number_max_books);
        for(int i = number_min_books; i <= number_max_books; i++) {
            if(this.books[i] == "livro") {
                this.books[i] = null;
                count++;
            }
        }
        System.out.println(count+ " foram lido(s)");
        bookcase();
        System.out.println("---------------------------------");
    }
    
    private void bookcase(){
    	int count = 0;
    	for(String livro : this.books) {
    		if(livro != null) {
    			count++;
    		}
        }
    	System.out.println("Estante: "+count+" Livro(s)");
    }
}
