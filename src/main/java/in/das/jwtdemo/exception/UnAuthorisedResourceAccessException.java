package in.das.jwtdemo.exception;

public class UnAuthorisedResourceAccessException extends RuntimeException{
    public UnAuthorisedResourceAccessException(String message){
        super(message);
    }
}
