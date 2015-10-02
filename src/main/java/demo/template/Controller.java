package demo.template;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Class with Main */
public class Controller 
{
	/** Log4J logger */
	private static Logger LOGGER =
        	LoggerFactory.getLogger( Controller.class.getName() );
	
	/** 
	 * Main method: 
	 * initializes logging and properties
	 * Transforms template string and displays in Spark
	 * 
	 * template data is in demo.properties
	 * 
	 *  */
    public static void main(String[] args)  
    { 
    	String content = "";    	
    	Transformer transformer = new Transformer();
    	View view = new View( );
    	Map<String, String> map = null;
        
        try
        {  
                	
        	Controller controller = new Controller();
        	Properties props = controller.init( "log4j.xml", "mydemo.properties" );
                 
            String templateString = props.getProperty("template.string");
            String templateMapString = props.getProperty("template.map");
            
            //if properties are set, use default
            if ( StringUtils.isBlank( templateString ) ||
            		StringUtils.isBlank( templateMapString ) )
            {          
            	LOGGER.warn("Unable to find properties. Using default template values.", 
                 		templateString, templateMapString);
            	
            	templateString = "<h2>Hello ${name}, The current date/time is: ${datetime}.</h2>";
            	
            	SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm:ss a");
                Date date = new Date();
                        	
            	map = new HashMap<String, String>();
            	map.put( "name", "Rally" );
            	map.put( "datetime", sdf.format(date) );
            	
            }  else {
            	LOGGER.debug("Reading properties file: templateString= {}, templateMapValues= {}", 
                 		templateString, templateMapString);
            	
            	map = controller.getTemplateMapFromString( templateMapString ) ;
            }
     
            //transform string
            content = transformer.processTemplateString(templateString, map);                   	
        } 
        catch ( Exception ex )
        {
        	Map<String, String> errorMap = new HashMap<String, String>();
            errorMap.put("error", ex.getMessage() );
            String errorString = "<h2>Sorry, we are unable to display the content " +
            		"due to an error... </h2><p>Error Message: <i>${error}</i></p>";
            try {
            	content = transformer.processTemplateString(errorString, errorMap);}
            catch ( Exception e ){
            	content = "Unable to display content due to an error";		
            }
        }        
       
        //display transformed String
        view.show( content, "output.ftl" );       
    }
    
    
	/** 
	 * Gets key:values from String as Map
	 * 
	 * @param keyValString The key:value properties String
	 * @return Map
	 * 
	 *  */
    private Map<String, String> getTemplateMapFromString( String keyValString )
    {
    	 Map<String, String> map = new HashMap<String, String>();
    	 
        //process key/value data properties String
        String[] keyValuePairs = StringUtils.split(
        		keyValString, "|");
        for( int i=0; i < keyValuePairs.length; i++) 
        {
        	String[] keyValue = StringUtils.split(keyValuePairs[i], ":");
        	map.put(keyValue[0], keyValue[1]);
        }
        
        return map;    	
    }
    

	/** 
	 * Handles basic init: logging and properties
	 * 
	 * @param logFileName The log conig file name
	 * @param propFileName The properties file name
	 * 
	 * @return Properties
	 * 
	 *  */
    private Properties init( 
    		String logFileName, 
    		String propFileName ) throws FileNotFoundException, IOException
    {
    	FileInputStream fileIn = null;    	
    	
    	try 
    	{
	    	//configure logging 
	    	DOMConfigurator.configure( logFileName ); 
	    	
	    	LOGGER.info( "Log configuration complete" );
	        
	        //load properties
	        Properties props = new Properties();
	        fileIn = new FileInputStream( propFileName );
	        props.load( fileIn );
	        
	        LOGGER.info( "Properties configuration complete" );
	        
	        return props;
    	}
    	finally
    	{
    		if ( fileIn != null ) {
    			fileIn.close();    			
    		}    		
    	}
    }
}
