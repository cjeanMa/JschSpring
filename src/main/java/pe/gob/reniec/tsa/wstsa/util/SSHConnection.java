package pe.gob.reniec.tsa.wstsa.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class SSHConnection {

    @Value("${connection.host}")
    private String host;
    @Value("${connection.user}")
    private String username;
    @Value("${connection.password}")
    private String password;
    @Value("${connection.port}")
    private int port;

    private Session session = null;
    private ChannelExec channel = null;

    public boolean getConnection() throws Exception {
        try {
            session = new JSch().getSession(this.username, this.host, this.port);
            session.setPassword(this.password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return true;
        } catch (JSchException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public boolean updateIps(String ip) throws InterruptedException {
        String command = "ssh cos-signserver \"export JAVA_HOME=/opt/jdk && export APPSRV_HOME=/opt/jboss && /opt/signserver/bin/signserver setproperty 5 WHITELISTED_FORWARDED_ADDRESSES '" + ip + "' && /opt/signserver/bin/signserver reload 5\"";

        try {
            channel = (ChannelExec) this.session.openChannel("exec");
            channel.setCommand(command);
            //To handle successfully commands
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            //To handle errors in commands
            ByteArrayOutputStream responseStreamError = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.setErrStream(responseStreamError);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(200);
            }

            String responseString = new String(responseStream.toByteArray());
            String responseStringError = new String(responseStreamError.toByteArray());

            System.out.println("Satisfactorio: " + responseString);
            System.out.println("Error: " + responseStringError);

            if(responseString.isEmpty()){
                return false;
            }
            else
                return true;

        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println("Exception catched Error al enviar comando");
            return false;
        }
    }

    public void closeConnection() {
        if (session != null) {
            session.disconnect();
        }
        if (channel != null) {
            channel.disconnect();
        }
    }

}
