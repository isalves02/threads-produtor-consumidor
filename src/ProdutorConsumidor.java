import java.util.concurrent.Semaphore;

public class ProdutorConsumidor {
    static Semaphore mutex = new Semaphore(1); // controla o acesso a 'rc'
    static Semaphore db = new Semaphore(1);    // controla o acesso à base de dados
    static Integer rc = 0;                     // número de processos lendo ou querendo ler
    static String[] books = new String[10];

    public static void main(String[] args) {
    	
    	Writer escritor;
    	Reader leitor;
    	
    	for(int i = 0; i<10; i++) {
    		escritor = new Writer(mutex, db, rc, books);
    		leitor =  new Reader(mutex, db, rc, books);
    		escritor.start();
    		leitor.start();
    	}
        
    }
}
