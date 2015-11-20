package it.geosolutions.unredd.stats.model.config.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import it.geosolutions.unredd.stats.model.config.Output;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;

/**
 * This class take a CSV statistic file and convert it in the desired output format
 * @author geosolutions
 *
 */
public class CSVConverter {

    public static void convertFromCSV(StatisticConfiguration config) {

        final Output outputObj = config.getOutput();
        final String FORMAT = outputObj.getFormat();
        if (FORMAT == null || FORMAT.isEmpty() || FORMAT.equals(outputObj.FORMAT_CSV)) {
            return;
        }
        File out = new File(outputObj.getFile());
        FileReader reader = null;
        BufferedReader lineReader = null;
        try {
            reader = new FileReader(out);
            lineReader = new BufferedReader(reader);
            
            if (FORMAT.equals(outputObj.FORMAT_JSON_ARRAY)) {
                convertToJSONArray(lineReader, out);
            }
            
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read statistic output file: '" + outputObj.getFile() + "'");
        }finally{
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    reader=null;
                }
            }
            if(lineReader!=null){
                try {
                    lineReader.close();
                } catch (IOException e) {
                    lineReader=null;
                }
            }
        }
    }

    private static void convertToJSONArray(BufferedReader lineReader, File out) {
        StringBuffer jsonOutput = new StringBuffer();
        jsonOutput.append("[");
        try {
            String line = null;
            while((line = lineReader.readLine()) != null){
                StringBuffer sb = new StringBuffer(line);
                if(','==sb.charAt(sb.length()-1)){
                    sb.setLength(sb.length() -1);
                }
                sb.insert(0, "[");
                sb.append("],");
                jsonOutput.append(sb);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read line by line the stats file: '" + out.getAbsolutePath() + "'");
        }
        jsonOutput.setLength(jsonOutput.length() -1);
        jsonOutput.append("]");
        FileWriter writer = null;
        try {
            writer = new FileWriter(out,false);
            writer.write(jsonOutput.toString());
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write the stats on the file: '" + out.getAbsolutePath() + "'");
        }
        finally{
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    writer=null;
                }
            }
        }
    }

}
