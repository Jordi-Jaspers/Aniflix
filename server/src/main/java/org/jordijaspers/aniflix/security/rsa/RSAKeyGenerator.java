package org.jordijaspers.aniflix.security.rsa;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.common.exception.InternalServerException;
import org.jordijaspers.aniflix.security.rsa.model.RSAKey;
import org.jordijaspers.aniflix.security.rsa.repository.RSAKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
@RequiredArgsConstructor
public class RSAKeyGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAKeyGenerator.class);

    private static final String RSA = "RSA";

    private final RSAKeyRepository rsaKeyRepository;

    public KeyPair loadRsaKey() {
        LOGGER.info("Attempting to configure public and private keys for the application");
        return rsaKeyRepository.findAll()
                .stream()
                .findFirst()
                .map(this::toKeyPair)
                .orElseGet(this::generateRsaKey);
    }

    private KeyPair generateRsaKey() {
        LOGGER.debug("No RSA key found in the database, generating a new one..");
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048);
            return saveRsaKey(keyPairGenerator.generateKeyPair());
        } catch (final Exception exception) {
            LOGGER.error("Something went wrong while generating the RSA key");
            throw new InternalServerException(exception);
        }
    }

    private KeyPair toKeyPair(final RSAKey rsaKey) {
        LOGGER.debug("RSA key found in the database, loading it..");
        try {
            final PrivateKey privateKey = getPrivateKeyFromBytes(rsaKey.getPrivateKey());
            final PublicKey publicKey = getPublicKeyFromBytes(rsaKey.getPublicKey());
            return new KeyPair(publicKey, privateKey);
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException exception) {
            LOGGER.error("Something went wrong while loading the RSA key from the database", exception);
            return generateRsaKey();
        }
    }

    private KeyPair saveRsaKey(final KeyPair keyPair) {
        final RSAKey rsaKey = new RSAKey();
        rsaKey.setPrivateKey(keyPair.getPrivate().getEncoded());
        rsaKey.setPublicKey(keyPair.getPublic().getEncoded());
        rsaKeyRepository.save(rsaKey);
        return keyPair;
    }

    private PrivateKey getPrivateKeyFromBytes(final byte[] privateKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey getPublicKeyFromBytes(final byte[] publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }
}
