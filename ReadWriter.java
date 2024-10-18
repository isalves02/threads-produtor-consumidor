package main;

import java.util.concurrent.Semaphore;

public class ReadWriter {
    static Semaphore bookShelf_acess = new Semaphore(1);    // controla o acesso à estante de livro
    static String[] books = new String[10];

    public static void main(String[] args) {
        Reader reader1 = new Reader(bookShelf_acess, books);
        Writer writer1 = new Writer(bookShelf_acess, books);
        Reader reader2 = new Reader(bookShelf_acess, books);
        Writer writer2 = new Writer(bookShelf_acess, books);

        reader2.start();
        reader1.start();
        writer1.start();
        writer2.start();
        
        
    }
}
