//使用线程池
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer03 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8801);
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        while (true){
            try{
                Socket socket = serverSocket.accept();
                executorService.execute(()->service(socket));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket){
        try {
            Thread.sleep(20);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println();
            printWriter.write("hello,nio");
            printWriter.close();
            socket.close();

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
