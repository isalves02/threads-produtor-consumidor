import java.util.concurrent.Semaphore;

public class ReadWriter {
    static Semaphore mutex = new Semaphore(1); // controla o acesso a 'rc'
    static Semaphore db = new Semaphore(1);    // controla o acesso à base de dados
    static int rc = 0;                         // número de processos lendo ou querendo ler

    public static void main(String[] args) {
        Thread reader = new Thread(new Reader());
        Thread reader2 = new Thread(new Reader());
        Thread writer = new Thread(new Writer());

        reader.start();
        writer.start();
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
            System.out.println("Lendo dados da base...");
        }

        private void useDataRead() {
            System.out.println("Usando dados lidos...");
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
            System.out.println("Pensando em novos dados...");
        }

        private void writeDataBase() {
            System.out.println("Escrevendo dados na base...");
        }
    }
}
