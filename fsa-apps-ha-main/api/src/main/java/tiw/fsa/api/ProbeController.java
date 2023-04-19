package tiw.fsa.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController()
public class ProbeController {

    @Value("${tiw.fsa.api.worker.url}")
    private String workerUrl;
    @GetMapping("/liveness")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean isAlive() {
        return true;
    }

    @GetMapping("/readiness")
    @ResponseBody
    public boolean isReady() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(workerUrl.concat("/liveness"), String.class).getStatusCode() == HttpStatus.OK;
    }
}