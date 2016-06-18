package net.donnelly.loyalty.rest;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * This is a wrapper to the Grizzly server that serves the pages for this app.
 * <p>
 * Created by bdonnell on 17/06/16.
 */
public class RestServer {
    /**
     * The Address to listen on
     */
    private final String listen;
    /**
     * The Port ot listen on
     */
    private final int port;
    /**
     * The Actual Grizzly server implementation
     */
    private HttpServer httpServer;

    /**
     * Create a new wrapper for the grizzly server
     *
     * @param listen the address to listen on
     * @param port   the port to listen on
     */
    public RestServer(final String listen, final int port) {
        this.listen = listen;
        this.port = port;
    }

    /**
     * Stop the server if it is active
     */
    public void stop() {
        if (httpServer != null) {
            httpServer.shutdown();
        }
    }

    /**
     * Start the server.
     * <p>
     * <b>Warning</b>
     * This method blocks and will not return until the server has been stopped.
     *
     * @throws IOException if the server cannot be started
     */
    public void start() throws IOException {
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages("net.donnelly.loyalty.rest.api");

        resourceConfig.register(JacksonFeature.class);

        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://" + listen + ":" + port + "/rest"), resourceConfig);
        httpServer.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(RestServer.class.getClassLoader(), "/"));
        httpServer.start();
    }
}
