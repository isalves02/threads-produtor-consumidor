import java.util.concurrent.Semaphore;

public class ReadWriter {
    static Semaphore mutex = new Semaphore(1); // controla o acesso a 'rc'
    static Semaphore db = new Semaphore(1);    // controla o acesso à base de dados
    static Integer rc = 0;                         // número de processos lendo ou querendo ler
    static String[] books = new String[10];

    public static void main(String[] args) {
        Reader reader1 = new Reader(mutex, db, rc, books);
        Writer writer1 = new Writer(mutex, db, rc, books);
        Reader reader2 = new Reader(mutex, db, rc, books);
        Writer writer2 = new Writer(mutex, db, rc, books);

        writer1.start();
        writer2.start();
        reader2.start();
        reader1.start();
        
    }
}
