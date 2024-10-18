package main;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Writer extends Thread {
    public Semaphore mutex; 
    public Semaphore bookShelf_acess;    
    public Integer rc;  
    public String[] books;
    public Random random = new Random();

    public Writer(Semaphore bookShelf_acess, String[] books) {
        this.bookShelf_acess = bookShelf_acess;
        this.books = books;
    }

    @Override
    public void run() {
        while (true) {
            try {

                this.bookShelf_acess.acquire(); // Fecha o semaforo de acesso a estante de livros

                writeBooks(); // Escreve, escreve, escreve...
                
                this.bookShelf_acess.release(); // Abre o semaforo de acesso a estante de livros

                Thread.sleep(1000);
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

        System.out.println(count + " foram escrito(s)");
        this.bookCase();
        this.showBookShelf();

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

    private void bookCase(){
    	int count = 0;
    	for(String livro : this.books) {
    		if(livro != null) {
    			count++;
    		}
        }
    	System.out.println("Estante: " + count + " livro(s)");
    }
}
