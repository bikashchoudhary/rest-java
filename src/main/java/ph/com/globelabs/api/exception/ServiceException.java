package ph.com.globelabs.api.exception;

public class ServiceException extends Exception {

    private static final long serialVersionUID = -3230489572388414359L;

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }

}
