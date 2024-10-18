import java.util.concurrent.Semaphore;

public class ReadWriter {
    static Semaphore mutex = new Semaphore(1); // controla o acesso a 'rc'
    static Semaphore db = new Semaphore(1);    // controla o acesso à base de dados
    static int rc = 0;                         // número de processos lendo ou querendo ler

    public static void main(String[] args) {
        // Criação das threads para leitor e escritor
        Thread reader1 = new Thread(new Reader(), "Reader 1");
        Thread reader2 = new Thread(new Reader(), "Reader 2");
        Thread reader3 = new Thread(new Reader(), "Reader 3");
        Thread writer1 = new Thread(new Writer(), "Writer 1");
        Thread writer2 = new Thread(new Writer(), "Writer 2");

        // Inicia as threads de leitor e escritor
        reader1.start();
        reader2.start();
        reader3.start();
        writer1.start();
        writer2.start();
    }

    static class Reader implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    mutex.acquire();
                    rc++;
                    if (rc == 1) db.acquire(); // primeiro leitor
                    mutex.release();

                    readDataBase(); // acessa os dados

                    mutex.acquire();
                    rc--;
                    if (rc == 0) db.release(); // último leitor
                    mutex.release();

                    useDataRead(); // região não crítica
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void readDataBase() {
            System.out.println(Thread.currentThread().getName() + " está lendo dados.");
        }

        private void useDataRead() {
            System.out.println(Thread.currentThread().getName() + " está consumindo dados.");
        }
    }

    static class Writer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    thinkUpData(); // região não crítica

                    db.acquire(); // acesso exclusivo
                    writeDataBase(); // atualiza os dados
                    db.release(); // libera o acesso exclusivo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void thinkUpData() {
            System.out.println(Thread.currentThread().getName() + " está aguardando.");
        }

        private void writeDataBase() {
            System.out.println(Thread.currentThread().getName() + " está produzindo.");
        }
    }
}