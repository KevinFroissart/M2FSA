package tiw.fsa.worker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class EncryptionService {
    private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);

    @Autowired
    private MeterRegistry meterRegistry;
    private Counter encryptStartedCounter;
    private Counter encryptFinishedCounter;

    @Value("${tiw.fsa.worker.latence}")
    private long latence;

    @PostConstruct
    public void init() {
        encryptStartedCounter = Counter.builder("encrypt.started")
                .description("Number of encrypt started")
                .register(meterRegistry);
        encryptFinishedCounter = Counter.builder("encrypt.finished")
                .description("Number of encrypt finished")
                .register(meterRegistry);
    }

    public String encrypt(String keyName, String data) throws EncryptException {
        encryptStartedCounter.increment();
        log.trace("Encryption started with key: {} and data: {}", keyName, data);
        String key = keyName;
        byte[] dataB = Base64.getDecoder().decode(data);
        byte[] keyB = key.getBytes();
        byte[] dataC = new byte[dataB.length];
        for (int i = 0; i < dataB.length; i++) {
            dataC[i] = (byte) (dataB[i] + keyB[i % keyB.length]);
        }
        try {
            lock.sleep(latence); // Simulation de temps de calcul
        } catch (InterruptedException e) {
            throw new EncryptException(e);
        } finally {
            log.trace("Encryption finished with key: {} and data: {}", keyName, data);
            encryptFinishedCounter.increment();
        }
        return new String(Base64.getEncoder().encode(dataC), StandardCharsets.ISO_8859_1);
    }

    public String decrypt(String keyName, String data) throws DecryptException {
        encryptStartedCounter.increment();
        log.trace("Decryption started with key: {} and data: {}", keyName, data);
        String key = keyName;
        byte[] dataB = Base64.getDecoder().decode(data);
        byte[] keyB = key.getBytes();
        byte[] dataC = new byte[dataB.length];
        for (int i = 0; i < dataB.length; i++) {
            dataC[i] = (byte) (dataB[i] - keyB[i % keyB.length]);
        }
        try {
            lock.sleep(latence); // Simulation de temps de calcul
        } catch (InterruptedException e) {
            throw new DecryptException(e);
        } finally {
            log.trace("Decryption finished with key: {} and data: {}", keyName, data);
            encryptFinishedCounter.increment();
        }
        return new String(Base64.getEncoder().encode(dataC), StandardCharsets.ISO_8859_1);
    }

    private static class Sleeper {
        /**
         * Méthode qui simule le calcul. Afin de simuler une charge processeur en multithreading
         * dans un cadre monocœur, on rend la méthode synchronized, ce qui va poser un verrou pour
         * empêcher deux threads de l'utiliser simultanément.
         *
         * @param time nombre de millisecondes à attendre
         * @throws InterruptedException si un problème survient pendant le thread.sleep
         */
        public synchronized void sleep(long time) throws InterruptedException {
            log.debug("Compute simulation in {}", this);
            Thread.sleep(time); // Simulation de temps de calcul
        }
    }

    private final static Sleeper lock = new Sleeper();

}
