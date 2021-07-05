package pe.gob.reniec.tsa.wstsa.dto;

public class ResponseError {

    private String message;

    public ResponseError() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "message='" + message + '\'' +
                '}';
    }
}
