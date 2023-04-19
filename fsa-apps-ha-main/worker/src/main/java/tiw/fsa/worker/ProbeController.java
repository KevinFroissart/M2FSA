package tiw.fsa.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ProbeController {

    private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);

    @GetMapping("/liveness")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean isAlive() {
        log.info("/liveness - liveness probe");
        return true;
    }
}
