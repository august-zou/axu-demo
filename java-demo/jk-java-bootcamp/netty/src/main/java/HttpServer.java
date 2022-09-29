import javax.net.ssl.SSLContext;

public class HttpServer {
    private String port;
    private Boolean ssl;

    HttpServer(String port, Boolean ssl){
        this.port = port;
        this.ssl = ssl;
    }

    public void run() throws Exception {
        final SSLContext sslCtx;

    }
}
