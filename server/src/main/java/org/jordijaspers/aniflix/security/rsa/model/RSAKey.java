package org.jordijaspers.aniflix.security.rsa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * A class containing the public and private key for RSA encryption.
 */
@Data
@Entity
@Table(name = "rsa_key")
public class RSAKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "public_key")
    private byte[] publicKey;

    @Column(name = "private_key")
    private byte[] privateKey;
}
