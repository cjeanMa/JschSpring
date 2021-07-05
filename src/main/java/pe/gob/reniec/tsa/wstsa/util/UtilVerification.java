package pe.gob.reniec.tsa.wstsa.util;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UtilVerification {

    public boolean validateGroupIp(String[] listIp){
        String regExp = "([1-9][0-9]*|1\\d{2}|2[0-4][0-9]|25[0-5])(\\.)" +
                "(\\d{1,2}|1\\d{2}|2[0-4][0-9]|25[0-5])(\\.)" +
                "(\\d{1,2}|1\\d{2}|2[0-4][0-9]|25[0-5])(\\.)" +
                "([1-9][0-9]*|1\\d{2}|2[0-4][0-9]|25[0-5])";
        for (String ip : listIp){
            if (!Pattern.matches(regExp, ip)) return false;
        }

        return true;
    }
}
