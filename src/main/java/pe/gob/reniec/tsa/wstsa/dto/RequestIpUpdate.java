package pe.gob.reniec.tsa.wstsa.dto;

public class RequestIpUpdate {

    private String ip;

    public RequestIpUpdate() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "RequestIpUpdate{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
