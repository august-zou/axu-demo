public class NettyServerApplication {
    public static void main(String[] args) {
        HttpServer server=  new HttpServer(8801,false);
        try{
            server.run();
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
