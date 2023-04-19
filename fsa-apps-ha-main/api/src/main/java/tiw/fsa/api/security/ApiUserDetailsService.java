package tiw.fsa.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tiw.fsa.api.encryption.EncryptService;
import tiw.fsa.api.user.Utilisateur;
import tiw.fsa.api.user.UtilisateurRepository;

import java.util.Optional;

@Service
public class ApiUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(EncryptService.class);

    private final UtilisateurRepository utilisateurRepository;

    public ApiUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public ApiUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.trace("Loading user: {}", username);
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(username);
        if (utilisateurOpt.isPresent()) {
            log.trace("User {} found", username);
            return new ApiUserDetails(utilisateurOpt.get());
        } else {
            throw new UsernameNotFoundException("Utilisateur inconnu");
        }
    }
}
