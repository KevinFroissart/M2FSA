package tiw.fsa.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tiw.fsa.api.encryption.EncryptService;

@RestController()
public class ProbeController {

    private static final Logger log = LoggerFactory.getLogger(EncryptService.class);

    @Value("${tiw.fsa.api.worker.url}")
    private String workerUrl;

    @GetMapping("/liveness")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean isAlive() {
        log.info("/liveness - liveness probe");
        return true;
    }

    @GetMapping("/readiness")
    @ResponseBody
    public boolean isReady() {
        log.info("/readiness - readiness probe");
        RestTemplate restTemplate = new RestTemplate();
        log.trace("Calling worker liveness probe at {}", workerUrl.concat("/liveness"));
        return restTemplate.getForEntity(workerUrl.concat("/liveness"), String.class).getStatusCode() == HttpStatus.OK;
    }
}