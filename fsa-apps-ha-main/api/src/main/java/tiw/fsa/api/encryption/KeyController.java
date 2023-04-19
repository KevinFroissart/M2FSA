package tiw.fsa.api.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiw.fsa.api.security.ForbiddenAccessException;
import tiw.fsa.api.security.SecurityUtils;
import tiw.fsa.api.user.UtilisateurNotFoundException;

/**
 * Point d'accès pour la gestion des clés
 */
@RestController
@RequestMapping("/key")
public class KeyController {

    private static final Logger log = LoggerFactory.getLogger(EncryptService.class);

    private final KeyService keyService;

    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }

    @PostMapping(path = "/{login}/{keyname}")
    @ResponseStatus(HttpStatus.OK)
    public void createKey(
            @PathVariable("login") String login,
            @PathVariable("keyname") String keyname)
            throws UtilisateurNotFoundException, ForbiddenAccessException {
        log.info("/key/{login}/{keyname} - Creating key {} for user {}", keyname, login);
        SecurityUtils.checkCurrentUserOrAdmin(login);
        keyService.createKey(login, keyname);
    }

    @GetMapping(path = "/{login}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String[] getKeys(@PathVariable("login") String login) throws ForbiddenAccessException, UtilisateurNotFoundException {
        log.info("/key/{login} - Getting keys for user {}", login);
        SecurityUtils.checkCurrentUserOrAdmin(login);
        return keyService.getKeys(login);
    }

}
