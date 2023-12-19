import java.io.BufferedReader;

class ClientReaderThread implements Runnable {
    private BufferedReader reader;

    public ClientReaderThread (BufferedReader reader) {
        this.reader = reader;
    }
    public void run() {

        try {
            String msg;
            while((msg = reader.readLine())!=null) {
                System.out.println(msg);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }
}
