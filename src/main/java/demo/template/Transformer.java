package demo.template;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  
 * Class will transform template String to include submitted values 
 * */
public class Transformer {
	
	/** Log4J logger */
	private static Logger LOGGER =
        	LoggerFactory.getLogger( Transformer.class.getName() );
	
    /** 
     * Modifies the templateString so that 
     * each ${key} is replaced with the associated value.
     * 
     * @param templateString The templateString
     * @param templateMap The map of input key/values
     * 
     * @throws BadInputException if param values are null/empty
     *   or key is not found in String
     * 
     * */
    public String processTemplateString( 
    		String templateString, 
    		Map<String, String> templateMap ) throws BadInputException
    {
    	LOGGER.info( "Begin processTemplateString" );
    	
    	//verify we have basic requirements
    	if (  StringUtils.isBlank( templateString ) ||
    			( templateMap == null || templateMap.size() < 1 ) )
    	{
    		throw new BadInputException( 
    				"ProcessTemplateString cannot accept null/empty values.");
    	}  
    	
    	//first clean templateString
    	String modifiedString = escapeDuplicatesMarkers( templateString );    	
    	
    	//get Set of key/value entries and iterate through
    	Iterator<Map.Entry<String,String>> it = templateMap.entrySet().iterator();    	    	
    	while( it.hasNext() )
    	{
    		Map.Entry<String,String> entry = it.next();
    		
    		/* create the template marker key format for which to 
    		 search/replace call the escape method here too, 
    		 in case map key already had ${key} format */
    		String templateKey =  escapeDuplicatesMarkers(
    				new StringBuilder("${").append(entry.getKey()).append("}").toString() );
    		
    		//verify that key is in template
    		if ( modifiedString.indexOf( templateKey ) >= 0 )
    		{
    			//now search the templateString, and replace the marker
    			modifiedString = StringUtils.replace( 
        				modifiedString, 
        				templateKey, 
        				entry.getValue() );   		    			
    		}
    		else 
    		{
    			throw new BadInputException( 
    					"ProcessTemplateString unable to complete. The template does not include key:  " 
    			+ templateKey);    			
    		}    		
    	}
    	LOGGER.info( "ProcessTemplateString completed without issue: {}", modifiedString );
    	
    	return modifiedString;
    }
    
    
    /** 
     * Cleans the value of any duplicate marker characters
     * 
     * @param input The input to escape
     * 
     * @return modified String
     * 
     * */
    private String escapeDuplicatesMarkers( String input )
    {    	
    	LOGGER.debug( "Begin EscapeDuplicatesMarkers for string= {}", input );
    	
    	input  = StringUtils.replaceEach( 
    			input, 
				new String[]{"${${", "}}"}, 
				new String[]{"${", "}"}); 
		
		LOGGER.debug( "EscapeDuplicatesMarkers completing with string= {}", input ); 
		return input;    	
    }

}
