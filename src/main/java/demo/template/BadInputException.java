package demo.template;

public class BadInputException extends Exception 
{
	public BadInputException( ) {};
	
	public BadInputException( String message ) {
		super( message );
	};
	
    public BadInputException(Throwable cause) {
        super(cause);      
    }

    public BadInputException(String message, Throwable cause) {
        super(message, cause);
    }

}
