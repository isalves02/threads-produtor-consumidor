import java.util.concurrent.Semaphore;

public class ReadWriter {
    static Semaphore mutex = new Semaphore(1); // controla o acesso a 'rc'
    static Semaphore db = new Semaphore(1);    // controla o acesso à base de dados
    static Integer rc = 0;                         // número de processos lendo ou querendo ler

    public static void main(String[] args) {
        Reader reader = new Reader(mutex, db, rc);
        Writer writer = new Writer(mutex, db, rc);

        reader.start();
        writer.start();
    }
}
