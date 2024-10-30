package problema;

import java.util.concurrent.Semaphore;

public class ReadWriter {
    static Semaphore bookShelf_acess = new Semaphore(1);    // controla o acesso à estante de livro
    static String[] books = new String[10];

    public static void main(String[] args) {
    	
    	Reader[] readers = new Reader[3];
    	Writer[] writers = new Writer[2];
    	
    	for(int i = 0; i < readers.length; i++) {
    		readers[i] = new Reader(bookShelf_acess, books);
    		readers[i].start();
    	}
    	
    	for(int i = 0; i < writers.length; i++) {
    		writers[i] = new Writer(bookShelf_acess, books);
    		writers[i].start();
    	}
    }
}
