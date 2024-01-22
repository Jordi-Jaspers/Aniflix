package org.jordijaspers.aniflix.security.rsa.repository;

import org.jordijaspers.aniflix.security.rsa.model.RSAKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository connection to the RSAKey table.
 */
@Repository
public interface RSAKeyRepository extends JpaRepository<RSAKey, Integer> {

}
