package tiw.fsa.api.encryption;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tiw.fsa.api.user.UtilisateurNotFoundException;
import tiw.fsa.api.user.UtilisateurRepository;

/**
 * Service gérant les clés de chiffrement.
 */
@Service
public class KeyService {

    private static final Logger log = LoggerFactory.getLogger(EncryptService.class);

    @Autowired private MeterRegistry meterRegistry;
    private Gauge keyCountGauge;

    private final KeyRepository keyRepository;
    private final UtilisateurRepository utilisateurRepository;

    public KeyService(KeyRepository keyRepository, UtilisateurRepository utilisateurRepository) {
        this.keyRepository = keyRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @PostConstruct
    public void init() {
        keyCountGauge = Gauge.builder("key.count", keyRepository, KeyRepository::count)
                .description("Number of keys")
                .register(meterRegistry);
    }

    /**
     * Créée une clé pour un utilisateur
     *
     * @param login   le nom de l'utilisateur
     * @param keyname le nom de la clé
     * @throws UtilisateurNotFoundException si l'utilisateur n'existe pas
     */
    public void createKey(String login, String keyname) throws UtilisateurNotFoundException {
        log.trace("Creating key {} for user {}", keyname, login);
        var uOpt = utilisateurRepository.findById(login);
        if (uOpt.isPresent()) {
            var id = new Key.IdC(uOpt.get(), keyname);
            if (!keyRepository.existsById(id)) {
                // If key does not exist
                Key key = new Key();
                key.setId(keyname);
                key.setUtilisateur(uOpt.get());
                keyRepository.save(key);
                keyCountGauge.measure();
                log.trace("Key {} created for user {}", keyname, login);
            }
        } else { // no such user
            throw new UtilisateurNotFoundException();
        }
    }

    /**
     * Récupère les clés d'un utilisateur
     *
     * @param login le nom de l'utilisateur
     * @return un tableau des noms de clés de cet utilisateur
     * @throws UtilisateurNotFoundException si l'utilisateur n'existe pas
     */
    public String[] getKeys(String login) throws UtilisateurNotFoundException {
        log.trace("Getting keys for user {}", login);
        if (!utilisateurRepository.existsById(login)) {
            throw new UtilisateurNotFoundException();
        }
        var keys = keyRepository.getKeyIdsByLogin(login);
        log.trace("Found {} keys for user {}", keys.size(), login);
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * Checks if a key exists
     *
     * @param login   the username for this key
     * @param keyname the name of the key
     * @return true if the key exists for this user
     */
    public boolean keyExists(String login, String keyname) {
        log.trace("Checking if key {} exists for user {}", keyname, login);
        return keyRepository.existsByIdAndUtilisateur_Login(keyname, login);
    }

    /**
     * Delete all keys from some user
     * @param login the username of the user
     */
    @Transactional
    public void deleteUserKeys(String login) {
        keyCountGauge.measure();
        log.trace("Deleting all keys for user {}", login);
        keyRepository.deleteAllByUtilisateur_Login(login);
    }
}
