package problema;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Reader extends Thread {
    public Semaphore bookShelf_acess;
    public String[] books;
    public Random random = new Random();

    public Reader(Semaphore bookShelf_acess, String[] books) {
        this.bookShelf_acess = bookShelf_acess;
        this.books = books;
    }

    @Override
    public void run() {
        while (true) {
            try {
            	this.bookShelf_acess.acquire(); // Fecha o semaforo de acesso a estante de livros
                
                readBooks(); // Lê, lê, lê...
                
                this.bookShelf_acess.release(); // Abre o semaforo para que outra thread possa acessar a estante 
                
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readBooks() {
        int count = 0;
        int end_interval = 1 + this.random.nextInt(this.books.length - 1);
        int start_interval = 0 + this.random.nextInt(end_interval);
        
        for(int i = start_interval; i <= end_interval; i++) {
            if(this.books[i] == "livro") {
                this.books[i] = null;
                count++;
            }
        } 
        
        if(count == 0) return;
        
        System.out.println(String.format("A %s está leu livro(s)", this.getName()));
        System.out.println(count + " foram lido(s)");
        bookcase();
        showBookShelf();

    }

    public boolean checkIfHaveBooksInInterval(int start, int end) {
        for(int i = start; i <= end; i++) {
            if(this.books[i] == "livro") {
                return true;
            }
        }
        return false;
    }

    private void showBookShelf() {
        for(String livro : this.books) {
            System.out.print(livro + " ");
        	/* System.out.print(livro == "livro" ? 
                     Colors.GREEN_BOLD + livro + Colors.RESET  + " " : 
                     Colors.RED_BOLD + livro + Colors.RESET  + " "
        			 );*/
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
    	System.out.println("Estante: " + count + " livro(s)");
    }
}
