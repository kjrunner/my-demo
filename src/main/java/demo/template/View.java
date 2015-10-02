package demo.template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import freemarker.template.Configuration;
import freemarker.template.Template;


/** 
 * Displays template via Spark server: localhost:4567
 *   
 *   */
public class View 
{
	/** Log4J logger */
	private static Logger LOGGER =
        	LoggerFactory.getLogger( View.class.getName() );
	
	/** Freemarker template configuration object */
	private Configuration configuration = null;
	
	
	/** Default constructor 
	 * */
	public View() 
	{
		this("/freemarker");
	}
	
	/** 
	 * Overloaded constructor
	 * 
	 * @param templateDir The template directory location	 
	 */
	public View( String templateDir ) 
	{   
		configuration = new Configuration();
        configuration.setClassForTemplateLoading( View.class, templateDir  ) ;
	};
	
	/** 
	 * Launches Spark server and displays template page.
	 * 
	 * Note this simple implementation assumes the template file
	 * will include the marker ${body}.
	 * 
	 * @param body The template body content
	 * @param templateFileName The template file	 
	 */
	public void show( final String body, final String templateFileName ) 
    {
    	LOGGER.info( "Begin show for templateFile= {}", templateFileName );
    	
        Spark.get(
        		//new Route to get, process and display template 
        		new Route("/") 
        		{
		            @Override
		            public Object handle(final Request request,
		                                 final Response response) 
		            {
		                StringWriter writer = new StringWriter();
		                try
		                {
		                    Template template = configuration.getTemplate( templateFileName );
		             
		                    Map<String, String> inputMap = new HashMap<String, String>();
		                   
		                    inputMap.put( "body", body );
		
		                    template.process( inputMap, writer );
		                } 
		                catch (Exception e) 
		                {
		                    e.printStackTrace();
		                    halt(500);
		                }
		                return writer;
		            }
		        } 
          ); //end Spark.get()
    
     }

}
