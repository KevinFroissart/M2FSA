package tiw.fsa.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crypt/{keyName}")
public class EncryptionController {

    private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);

    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping(path = "/encrypt")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String encryptData(@RequestBody String data, @PathVariable(name = "keyName") String keyName) throws EncryptException {
        log.info("/crypt/{keyName}/encrypt - Encrypting data: {} with key: {}", data, keyName);
        return encryptionService.encrypt(keyName, data);
    }

    @PostMapping(path = "/decrypt")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String decryptData(@RequestBody String data, @PathVariable(name="keyName") String keyName) throws DecryptException {
        log.info("/crypt/{keyName}/decrypt - Decrypting data: {} with key: {}", data, keyName);
        return encryptionService.decrypt(keyName, data);
    }
}
