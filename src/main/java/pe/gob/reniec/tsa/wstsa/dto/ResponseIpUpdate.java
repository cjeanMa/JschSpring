package pe.gob.reniec.tsa.wstsa.dto;

public class ResponseIpUpdate {

    public String listIp;
    public String message;

    public ResponseIpUpdate() {
    }

    public String getListIp() {
        return listIp;
    }

    public void setListIp(String listIp) {
        this.listIp = listIp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseIpUpdate{" +
                "listIp='" + listIp + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
