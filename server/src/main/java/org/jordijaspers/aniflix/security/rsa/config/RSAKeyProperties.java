package org.jordijaspers.aniflix.security.rsa.config;

import lombok.Data;
import org.jordijaspers.aniflix.security.rsa.RSAKeyGenerator;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * A class containing the public and private key for RSA encryption.
 */
@Data
@Component
public class RSAKeyProperties {

    private final RSAPrivateKey privateKey;

    private final RSAPublicKey publicKey;

    /**
     * Sets the private and public key with RSA encryption.
     */
    public RSAKeyProperties(final RSAKeyGenerator keyGenerator) {
        final KeyPair keyPair = keyGenerator.loadRsaKey();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }
}
