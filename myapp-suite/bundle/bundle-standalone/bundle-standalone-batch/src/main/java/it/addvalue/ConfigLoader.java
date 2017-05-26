package it.addvalue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.xml.DOMConfigurator;

public class ConfigLoader
{

    private static final Log4JLogger log        = new Log4JLogger(ConfigLoader.class.getName());

    private URL                      configUrlPath;

    private String                   configString;

    private long                     lastUpdate = 0;

    private long                     deadline   = 300000;                                       // 5 minuti

    private Map<String, Properties>  configCache;

    public ConfigLoader()
    {
        log.debug("Created ConfigLoader " + this.hashCode());
        configCache = new HashMap<String, Properties>();
    }

    public static ConfigLoader buildWithConfigBasePath(String url)
    {
        try
        {
            ConfigLoader configLoader = new ConfigLoader();
            configLoader.setUrl(url);
            log.info("loading ..." + configLoader.configUrlPath.toString());
            return configLoader;
        }
        catch ( MalformedURLException e )
        {
            throw new RuntimeException(e);
        }
    }

    public ConfigLoader(String jndi) throws Exception
    {
        this(jndi, false);
    }

    public ConfigLoader(String jndi,
                        boolean loadLog4j) throws Exception
    {
        this();
        InitialContext context;
        try
        {
            context = new InitialContext();
            Context envCtx = (Context) context.lookup("java:comp/env");

            log.warn("Recupero configurazione via JNDI: " + jndi);
            Object obj = envCtx.lookup(jndi);
            if ( obj instanceof URL )
            {
                configUrlPath = (URL) obj;
                log.warn("ottenuto1: " + configUrlPath.toString());
            }
            else if ( obj instanceof String )
            {
                configString = (String) obj;
                log.warn("ottenuto2: " + configString.toString());
            }
            else if ( obj instanceof Config )
            {
                Config configurazione = (Config) obj;
                log.debug("Ottenuta configurazione: " + configurazione + " - " + configurazione.size());
                final String configUrl = configurazione.getAttribute("url");
                log.warn("Letta url path di configurazione: " + configUrl);
                setUrl(configUrl);
            }
            else
            {
                throw new Exception("Attenzione: configurazione non ottenuta " + obj.getClass());
            }

        }
        catch ( Exception e )
        {
            // Scrivo su System.out perche' il log potrebbe essere non
            // configurato
            if ( "url/PATH_UPLOAD_RENDICONTAZIONI,url/PATH_SAVE_FLUSSI,url/CONFIGURATION".contains(jndi) )
                System.out.println("\nERRORE!\nLA RISORSA [" + jndi + "] DEVE ESSERE CONGIGURATA \n" + "Verifica /polipo-config/src/main/resources/environment/tomcat-context/context-xxx.xml\n");

            log.error("Errore nel caricamento del path di configurazione \n  - risorsa [ " + jndi + "]", e);
            boolean lanciaEccezione = false;
            if ( lanciaEccezione )
            {
                throw e;
            }
        }

        if ( loadLog4j )
        {
            try
            {
                DOMConfigurator.configure(new URL(getConf("config/log4j.xml")));
            }
            catch ( Exception e )
            {
                log.error("Errore nella configurazione di log4j", e);
                throw e;
            }
        }
    }

    public String getConf(String filename)
    {
        final String s = configUrlPath.toString() + filename;
        log.trace("Risorsa richiesta " + s);
        return s;
    }

    public void setUrl(String configUrl) throws MalformedURLException
    {
        configUrlPath = new URL(configUrl);
    }

    public URL getConfigUrlPath()
    {
        return configUrlPath;
    }

    public String getConfigUrlPathString()
    {
        return configUrlPath != null ? configUrlPath.getFile() : null;
    }

    /**
     * Il metodo ritorna il valore di una property contenuta nel file di config identificato dal nome in input
     *
     * @param fileName
     * @param propName
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String getProperty(String fileName,
                              String propName) throws IOException, URISyntaxException
    {
        Properties prop = configCache.get(fileName);
        if ( prop == null || lastUpdate + deadline < System.currentTimeMillis() )
        {
            log.debug("fileName: [" + fileName + "]" + " propName: [" + propName + "]");
            File fileProp = new File(new URI(this.getConf(fileName)));
            InputStream is = new FileInputStream(fileProp);

            prop = new Properties();
            prop.load(is);
            configCache.put(fileName, prop);

            IOUtils.closeQuietly(is);
            lastUpdate = System.currentTimeMillis();
        }
        log.trace("Config richiesta " + propName);

        if ( !prop.isEmpty() )
            return prop.getProperty(propName);
        return null;
    }

    public void setProperty(String fileName,
                            String propName,
                            String propVal) throws IOException, URISyntaxException
    {
        File fileProp = new File(new URI(this.getConf(fileName)));

        InputStream is = new FileInputStream(fileProp);

        Properties prop = new Properties();
        prop.load(is);
        prop.setProperty(propName, propVal);
        prop.store(new FileOutputStream(fileName), null);
    }

    public String getConfigString()
    {
        return configString;
    }

    /**
     * Ritorna tutte le proprietà contenute dentro al path del file .properties in input
     *
     * @param fileName
     *            - path del file props
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public Properties getProperties(String fileName) throws IOException, URISyntaxException
    {
        Properties prop = configCache.get(fileName);
        if ( prop == null || lastUpdate + deadline < System.currentTimeMillis() )
        {
            log.debug("Caricamento di.. " + fileName);
            File fileProp = new File(new URI(this.getConf(fileName)));
            InputStream is = new FileInputStream(fileProp);

            prop = new Properties();
            prop.load(is);
            configCache.put(fileName, prop);

            IOUtils.closeQuietly(is);
            lastUpdate = System.currentTimeMillis();
        }

        return prop;
    }
}
