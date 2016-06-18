package net.donnelly.loyalty;

import net.donnelly.loyalty.rest.RestServer;
import org.apache.commons.cli.*;

/**
 * This is the main entry point for the application.
 * <p>
 * Created by bdonnell on 17/06/16.
 */
public class Main {

    /**
     * The server wrapper used to start and stop the server.
     */
    private static RestServer restServer;

    /**
     * This is the main entry point for java.
     *
     * @param args the args parsed in from the commandline start.
     */
    public static void main(final String[] args) {
        final CommandLine options = createOptions(args);
        if (options == null)
            return;

        final String host = checkHost(options);
        final int port = checkPort(options);

        restServer = new RestServer(host, port);

        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook") {
            @Override
            public void run() {
                if (restServer != null) {
                    restServer.stop();
                }
            }
        });

        try {
            restServer.start();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method parses the options sent on the command line into useful information.
     *
     * @param args the arguments parsed in from the command line
     * @return a {@link CommandLine} object that contains the parsed values.
     */
    private static CommandLine createOptions(final String[] args) {
        final Options options = new Options();
        options.addOption("a", "address", true, "Address for the server to liston on.");
        options.addOption("p", "port", true, "Port for the server to liston on.");
        options.addOption("h", "help", false, "Show help.");

        final DefaultParser defaultParser = new DefaultParser();
        try {
            final CommandLine parse = defaultParser.parse(options, args);
            if (parse.hasOption('h')) {
                final HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(Main.class.getName(), "", options, "\n", true);
                return null;
            }
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method will attempt to get the port from the command line, if available or default to port 8080.
     *
     * @param args the input from the commandline
     * @return the port to listen on
     */
    private static int checkPort(final CommandLine args) {
        final String port = args.getOptionValue('p');
        if (port == null)
            return 8080;
        return Integer.valueOf(port);
    }

    /**
     * This method will attempt to get the address from the command line, if available or default to
     * address 0.0.0.0, which means listen on all addresses.
     *
     * @param args the input from the commandline
     * @return the address to listen on
     */
    private static String checkHost(final CommandLine args) {
        final String address = args.getOptionValue('a');
        if (address == null)
            return "0.0.0.0";
        return address;
    }
}
