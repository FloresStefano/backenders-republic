package it.addvalue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@Component
@SuppressWarnings("all")
public class BatchCommandLine
{

    public static final String LOG4J_PARAM_NAME     = "--log4j";

    public static final String CONFIG_PARAM_NAME    = "--config";

    public static final String BATCHFILE_PARAM_NAME = "--batch_file";

    public static final String RESOURCE_PATH_PREFIX = "file:///";

    public static final String XML_BATCH_CONFIG     = "config/batch-config.xml";

    public static final String PATH_CONFIG_CUSTOM   = "config";

    private static Log4JLogger log;

    public static void main(String[] args) throws Exception
    {
        // Parsing parametri e costruzione parametri per il CommandLineJobRunner
        if ( args == null || args.length < 7 )
            throw new IllegalArgumentException("Parametri mancanti per l'esecuzione del processo");

        ArrayList<String> newargs = new ArrayList<String>();
        newargs.add(XML_BATCH_CONFIG);

        String pathLog4j = "";
        String pathConfig = "";
        String batchFile = "";

        Iterator parameters = Arrays.asList(args).iterator();
        while (parameters.hasNext())
        {
            String currentParam = (String) parameters.next();
            if ( currentParam.equals(LOG4J_PARAM_NAME) )
            {
                pathLog4j = (String) parameters.next();
            }
            else if ( currentParam.equals(CONFIG_PARAM_NAME) )
            {
                pathConfig = (String) parameters.next();
            }
            else if ( currentParam.equals(BATCHFILE_PARAM_NAME) )
            {
                batchFile = (String) parameters.next();
            }
            else
            {
                newargs.add(currentParam);
            }
        }

        // Verifica esistenza file log4j
        File log4jFile = new File(pathLog4j);
        if ( !log4jFile.exists() )
            throw new FileNotFoundException("File di configurazione log4j non trovato: " + pathLog4j);

        // Verifica esistenza file properties
        File configFile = new File(pathConfig);
        if ( !configFile.exists() )
            throw new FileNotFoundException("File di configurazione config non trovato: " + pathConfig);

        // Configuro il logger log4j
        DOMConfigurator.configure(new URL(RESOURCE_PATH_PREFIX + pathLog4j));
        log = new Log4JLogger(ConfigLoader.class.getName());

        log.info("\n--ESECUZIONE JOB TRAMITE BatchCommandLine");
        log.info(logArgs(pathLog4j, pathConfig, batchFile, newargs));

        // Definisco il path base per il configLoader
        String configLoaderPath = "";
        if ( pathConfig.substring(0, pathLog4j.indexOf(PATH_CONFIG_CUSTOM)).equals(pathConfig.substring(0, pathConfig.indexOf(PATH_CONFIG_CUSTOM))) )
            configLoaderPath = pathConfig.substring(0, pathLog4j.indexOf(PATH_CONFIG_CUSTOM));

        if ( configLoaderPath == null || configLoaderPath.length() == 0 )
        {
            log.info("\n--Impossibile determinare il path base per il configLoader");
            throw new Exception("Impossibile determinare il path base per il configLoader");
        }
        else
        {
            log.info("\n--Definito path base per il configLoader: " + configLoaderPath);
        }

        // Controllo la validità dell'xml batchFile
        // InputStream is = ConfigLoader.class.getClassLoader()
        // .getResourceAsStream(batchFile);
        // boolean batchFileValid = isValidXMLFile(is);
        // is.close();
        // if (!batchFileValid) {
        // log.info("\n--Il file batchFile non è valido: " + batchFile);
        // throw new IOException("Il file batchFile non è valido: "
        // + batchFile);
        // }

        // Setto le variabili d'ambiente
        pathConfig = RESOURCE_PATH_PREFIX + pathConfig;
        configLoaderPath = RESOURCE_PATH_PREFIX + configLoaderPath;

        System.setProperty("batch_file", batchFile);
        System.setProperty("properties_file", pathConfig);
        System.setProperty("configloader.base.path", configLoaderPath);

        log.info(logEnv(batchFile, pathConfig, configLoaderPath));

        // Eseguo il job
        log.info("\n--Lanco il CommandLineJobRunner per esecuzione del job specificato");
        CommandLineJobRunner.main(convertArrayListToStringArray(newargs));

    }

    private static String logArgs(String pathLog4j,
                                  String pathConfig,
                                  String batchFile,
                                  ArrayList<String> args)
    {
        String elencoParametri = "\n--ELENCO PARAMETRI RICEVUTI IN INGRESSO: \n" + "------------------------------------------------------" + "\n" + "|Parametro  | Valore" + "\n" + "------------------------------------------------------" + "\n" + formatParamRow("log4j", pathLog4j, 10) + "\n" + formatParamRow("config", pathConfig, 10) + "\n" + formatParamRow("batch_file", batchFile, 10);
        for ( int i = 1; i < args.size(); i++ )
        {
            String argName = i == 1 ? "jobId" : "args_param";
            elencoParametri += "\n" + formatParamRow(argName, args.get(i), 10);
        }
        elencoParametri += "\n" + "------------------------------------------------------";

        return elencoParametri;
    }

    private static String logEnv(String batchFile,
                                 String pathConfig,
                                 String configLoaderPath)
    {
        String elencoEnviroment = "\n--ELENCO VARIABILI DI AMBIENTE SETTATE: \n" + "------------------------------------------------------" + "\n" + "|Variabile         | Valore" + "\n" + "------------------------------------------------------" + "\n" + formatParamRow("batchFile", batchFile, 17) + "\n" + formatParamRow("pathConfig", pathConfig, 17) + "\n" + formatParamRow("configLoaderPath", configLoaderPath, 17) + "\n" + "------------------------------------------------------";

        return elencoEnviroment;
    }

    private static String formatParamRow(String paramName,
                                         String paramValue,
                                         int rightPad)
    {
        return "|" + StringUtils.rightPad(paramName, rightPad, '0') + "| " + paramValue;
    }

    private static String[] convertArrayListToStringArray(ArrayList<String> arrayList)
    {
        String[] array = new String[arrayList.size()];
        array = arrayList.toArray(array);
        return array;
    }

    private static boolean isValidXMLFile(InputStream is)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
        }
        catch ( SAXParseException spe )
        {
            return false;

        }
        catch ( SAXException sxe )
        {
            return false;

        }
        catch ( ParserConfigurationException pce )
        {
            return false;

        }
        catch ( IOException ioe )
        {
            return false;
        }

        return true;
    }

}
