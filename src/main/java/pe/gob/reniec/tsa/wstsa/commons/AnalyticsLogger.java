package pe.gob.reniec.tsa.wstsa.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsLogger {

    private static final Logger logger = LoggerFactory.getLogger(pe.gob.reniec.tsa.wstsa.commons.AnalyticsLogger.class);

    private static final String LOG_TYPE_INFO = "INFO";
    private static final String LOG_TYPE_WARN = "WARN";
    private static final String LOG_TYPE_ERROR = "ERROR";

    @Value("${spring.application.name}")
    private String appName;

    public void error(String data, String message) {
        writeLog(LOG_TYPE_ERROR, data, message);
    }

    public void info(String data, String message) {
        writeLog(LOG_TYPE_INFO, data, message);
    }

    public void warn(String data, String message) {
        writeLog(LOG_TYPE_WARN, data, message);
    }

    private void writeLog(String type, String data, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = new HashMap<>();

            map.put("type", type);
            map.put("data", data);
            map.put("message", message);

            switch (type) {
                case LOG_TYPE_ERROR:
                    logger.error("ANALYTICS_LOG: ".concat(mapper.writeValueAsString(map)));
                    break;

                case LOG_TYPE_WARN:
                    logger.warn("ANALYTICS_LOG: ".concat(mapper.writeValueAsString(map)));
                    break;

                default:
                    logger.info("ANALYTICS_LOG: ".concat(mapper.writeValueAsString(map)));
                    break;
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));

            logger.error("ANALYTICS_LOG: ERROR PARSING LOG\n"
                    .concat(sw.toString())
                    .concat("\nLog parsed:\n")
                    .concat(data));
        }
    }

}