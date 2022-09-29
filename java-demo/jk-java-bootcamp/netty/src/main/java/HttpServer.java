import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.ssl.SslContext;



public class HttpServer {
    private Integer port;
    private Boolean ssl;

    HttpServer(Integer port, Boolean ssl){
        this.port = port;
        this.ssl = ssl;
    }

    public void run() throws Exception {
        final SslContext sslCtx;
        if(ssl){
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(),ssc.privateKey());
        }else {
            sslCtx=null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(3);
        EventLoopGroup workerGroup = new NioEventLoopGroup(1000);
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.SO_REUSEADDR,true)
                    .option(ChannelOption.SO_RCVBUF,32*1024)
                    .option(ChannelOption.SO_SNDBUF,3*1024)
                    .option(EpollChannelOption.SO_REUSEPORT,true)
                    .option(EpollChannelOption.SO_KEEPALIVE,true);
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpInitializer(sslCtx));

            Channel ch = b.bind(port).sync().channel();
            System.out.printf("start a http server at %d",port);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
