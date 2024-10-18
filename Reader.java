package main;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Reader extends Thread {
    public Semaphore mutex;
    public Semaphore bookShelf_acess;
    public Integer rc; 
    public String[] books;
    public Random random = new Random();

    public Reader(Semaphore mutex, Semaphore bookShelf_acess, Integer rc, String[] books) {
        this.mutex = mutex;
        this.bookShelf_acess = bookShelf_acess;
        this.rc = rc;
        this.books = books;
    }

    @Override
    public void run() {
        while (true) {
            try {
            	// this.mutex.acquire(); // Fecha o semaforo de acesso para que outra thread não acesse essa area do codigo
                this.bookShelf_acess.acquire(); // Fecha o semaforo de acesso a estante de livros
                
                readBooks(); // Lê, lê, lê...
                
                this.bookShelf_acess.release(); // Abre o semaforo para que outra thread possa acessar a estante 
             //  this.mutex.release(); // Abre o semaforo para que outra thread possa acessar essa area do codigo

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readBooks() {
        System.out.println(String.format("A %s está lendo livro(s)", this.getName()));

        int count = 0;
        int start_interval = 1 + this.random.nextInt(9);
        int end_interval = 0 + this.random.nextInt(start_interval);
        for(int i = end_interval; i <= start_interval; i++) {
            if(this.books[i] == "livro") {
                this.books[i] = null;
                count++;
            }
        } 
        
        System.out.println(count + " foram lido(s)");
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
    	System.out.println("Estante: " + count + " livro(s)");
    }
}
