package it.geosolutions.unredd.stats;

import it.geosolutions.unredd.stats.impl.StatsRunner;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;
import it.geosolutions.unredd.stats.model.config.util.StatisticChecker;
import java.io.File;
import javax.xml.bind.JAXB;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 * Parses CLI params, load configuration and run a StatsRunner
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class Stats {
    private final static Logger LOGGER = Logger.getLogger(Stats.class.toString());

    private static final char OPT_CONFIGFILE = 'c';

//    private static final char CSV_SEP = ',';

    public static void main(String[] args) {

        Options options = new Options()
                .addOption(OptionBuilder
                    .withLongOpt( "help" )
                    .withDescription(  "print help" )
                    .create( '?' ))
                .addOption(OptionBuilder
                    .withLongOpt( "config" )
                    .withArgName( "configFile" )
                    .withDescription(  "XML configuration file" )
                    .hasArg()
                    .isRequired()
                    .create( OPT_CONFIGFILE ))
                ;


        try {
            CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse( options, args);

            if(cmd.hasOption('?')) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "createAreaLayer", options );
                return;
            }

            String sConfigFile = cmd.getOptionValue(OPT_CONFIGFILE);
            File configFile = new File(sConfigFile);

            StatisticConfiguration cfg = JAXB.unmarshal(configFile, StatisticConfiguration.class);
            
            if( ! StatisticChecker.check(cfg)) {
                LOGGER.error("Bad configuration");
                return;
            }

            StatsRunner sr = new StatsRunner(cfg);
            sr.run();

            return;
        } catch (ParseException ex) {
            LOGGER.error(ex.getMessage());

        } catch(Exception e) {
            LOGGER.error("Unexpected exception", e);
            throw new RuntimeException("Unexpected exception", e);
        }

        // print usage after parser error
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "createAreaLayer", options );

    }

}
