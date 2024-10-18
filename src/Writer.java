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
        System.out.println(String.format("A %s está escrevendo livro(s)", this.getName()));

        int count = 0;
        int start_interval = 1 + this.random.nextInt(9);
        int end_interval = 0 + this.random.nextInt(start_interval);
        for(int i = end_interval; i <= start_interval; i++) {
            if(this.books[i] == null) {
                this.books[i] = "livro";
                count++;
            }
        }

        System.out.println(count+ " foram escrito(s)");
        bookcase();
        showBookShelf();

    }

    private void showBookShelf() {
        for(String livro : this.books) {
            System.out.print(livro == "livro" ? 
                            Colors.GREEN_BOLD + livro + Colors.RESET  + " " : 
                            Colors.RED_BOLD + livro + Colors.RESET  + " "
            );
        }
        System.out.println("\n---------------------------------\n");
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
