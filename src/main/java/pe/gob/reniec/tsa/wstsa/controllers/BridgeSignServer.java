package pe.gob.reniec.tsa.wstsa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.reniec.tsa.wstsa.commons.AnalyticsLogger;
import pe.gob.reniec.tsa.wstsa.constants.ResponseGeneral;
import pe.gob.reniec.tsa.wstsa.dto.RequestIpUpdate;
import pe.gob.reniec.tsa.wstsa.dto.ResponseError;
import pe.gob.reniec.tsa.wstsa.dto.ResponseIpUpdate;
import pe.gob.reniec.tsa.wstsa.util.SSHConnection;
import pe.gob.reniec.tsa.wstsa.util.UtilVerification;

@RestController
@RequestMapping("/address")
public class BridgeSignServer {

    @Autowired
    SSHConnection sshConnection;

    @Autowired
    AnalyticsLogger logger;

    @Value("${authorization.code}")
    private String authorizationCode;

    ResponseIpUpdate responseIpUpdate = new ResponseIpUpdate();

    @Autowired
    public UtilVerification utilVerification;

    @PostMapping("/update")
    public ResponseEntity putIpLocation(@RequestBody RequestIpUpdate requestIp, @RequestHeader("Authorization") String code) {

        if (code.equals(authorizationCode)) {
            responseIpUpdate.setListIp(requestIp.getIp());
            String[] listIp = requestIp.getIp().split("\\s");
            if (utilVerification.validateGroupIp(listIp)){
                try {
                    if (sshConnection.getConnection()) {
                        if(sshConnection.updateIps(requestIp.getIp())){
                            responseIpUpdate.setMessage(ResponseGeneral.SUCCESS_UPDATE_IP);

                            sshConnection.closeConnection();

                            logger.info(requestIp.getIp(), ResponseGeneral.SUCCESS_UPDATE_IP);
                            return new ResponseEntity(responseIpUpdate, HttpStatus.OK);
                        }
                        else{
                            logger.error(requestIp.getIp(), ResponseGeneral.ERROR_COMMAND);

                            sshConnection.closeConnection();

                            responseIpUpdate.setMessage(ResponseGeneral.ERROR_COMMAND);
                            return new ResponseEntity(responseIpUpdate, HttpStatus.BAD_REQUEST);
                        }
                    }
                    else{
                        logger.error(requestIp.getIp(), ResponseGeneral.ERROR_SERVER_CONNECTION);

                        responseIpUpdate.setMessage(ResponseGeneral.ERROR_SERVER_CONNECTION);
                        return new ResponseEntity(responseIpUpdate, HttpStatus.BAD_GATEWAY);
                    }

                }
                catch (InterruptedException e) {
                    responseIpUpdate.setMessage(ResponseGeneral.ERROR_COMMAND);
                    return new ResponseEntity(responseIpUpdate, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                catch (Exception e) {
                    responseIpUpdate.setMessage(ResponseGeneral.ERROR_SERVER_CONNECTION);
                    return new ResponseEntity(responseIpUpdate, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else {
                responseIpUpdate.setMessage(ResponseGeneral.ERROR_VALIDATION_IP);
                logger.error(requestIp.getIp(), ResponseGeneral.ERROR_VALIDATION_IP);
                return new ResponseEntity(responseIpUpdate, HttpStatus.BAD_REQUEST);
            }

        }
        else{
            ResponseError error = new ResponseError();
            error.setMessage(ResponseGeneral.ERROR_CODE_AUTORIZATION);

            logger.error(code, ResponseGeneral.ERROR_CODE_AUTORIZATION);

            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }

    }

}
