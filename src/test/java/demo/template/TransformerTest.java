package demo.template;


import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import demo.template.Transformer;

public class TransformerTest 
{
	
    @Test
    public void shouldTransformTemplateString()
    {
    	Map<String, String> map = new HashMap<String, String>();
    	String key = "day";
    	String value = "Friday";
    	map.put(key, value);    
    	
    	String testStr = new StringBuilder( "Today is ").append( value ).toString();
        String templateString = "Today is ${day}";
 
        Transformer tranformer = new Transformer();

        try {
        	assertEquals( 
        		tranformer.processTemplateString( templateString, map ), 
        		testStr, 
        		"Transformed string does not match." );
        }
        catch( Exception e ) {        	
        	assertNull( e );        	
        } 
    }
    
    @Test
    public void shouldEscapeAndTransformTemplateString()
    {
    	Map<String, String> map = new HashMap<String, String>();
    	String key = "${day}";
    	String value = "Monday";
    	map.put(key, value);    
    	
    	String testStr = new StringBuilder( "Today is ").append( value ).toString();
        String templateString = "Today is ${${day}";
 
        Transformer tranformer = new Transformer();

        try {
        	assertEquals( 
        		tranformer.processTemplateString( templateString, map ), 
        		testStr, 
        		"Transformed string does not match." );
        }
        catch( Exception e ) {        	
        	assertNull( e );        	
        } 
    }
    
    @Test
    public void shouldTransformTemplateStringWithNewMarker()
    {
    	Map<String, String> map = new HashMap<String, String>();
    	String key = "day";
    	String value = "${holiday}";
    	map.put(key, value);    
    	
    	String testStr = new StringBuilder( "Today is ").append( value ).toString();
        String templateString = "Today is ${day}";
 
        Transformer tranformer = new Transformer();

        try {
        	assertEquals( 
        		tranformer.processTemplateString( templateString, map ), 
        		testStr, 
        		"Transformed string does not match." );
        }
        catch( Exception e ) {        	
        	assertNull( e );        	
        } 
    }
    
    
    @Test
    public void shouldFailWhenMapDataMissing()
    {
    	Map<String, String> map = new HashMap<String, String>();    	
        String templateString = "Today is ${day}";
 
        Transformer tranformer = new Transformer();

        try {
        	tranformer.processTemplateString( templateString, map );         	
        }
        catch( Exception e ) {        	
        	assertNotNull( e );        	
        } 
        
        //now set to null
        map = null;
        try {
        	tranformer.processTemplateString( templateString, map );         	
        }
        catch( Exception e ) {        	
        	assertNotNull( e );        	
        } 
    }
    
    @Test
    public void shouldFailWhenTemplateStringMissing()
    {
    	Map<String, String> map = new HashMap<String, String>();
    	String key = "day";
    	String value = "Friday";
    	map.put(key, value); 
    	
        String templateString = "";
 
        Transformer tranformer = new Transformer();

        try {
        	tranformer.processTemplateString( templateString, map );         	
        }
        catch( Exception e ) {        	
        	assertNotNull( e );        	
        } 
        
        //now set to null
        templateString = null;
        try {
        	tranformer.processTemplateString( templateString, map );         	
        }
        catch( Exception e ) {        	
        	assertNotNull( e );        	
        } 
    }  
    
    @Test
    public void shouldFailWhenMarkerNotInTemplateString()
    {
    	Map<String, String> map = new HashMap<String, String>();
    	String key = "name";
    	String value = "Fluffy";
    	map.put(key, value); 
    	
        String templateString = "Today is ${day}";
 
        Transformer tranformer = new Transformer();

        try {
        	tranformer.processTemplateString( templateString, map );         	
        }
        catch( Exception e ) {        	
        	assertNotNull( e );        	
        }         
    
    }   

}
