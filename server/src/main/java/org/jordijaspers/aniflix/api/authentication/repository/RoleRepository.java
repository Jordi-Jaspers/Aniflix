package org.jordijaspers.aniflix.api.authentication.repository;

import org.jordijaspers.aniflix.api.authentication.model.Authority;
import org.jordijaspers.aniflix.api.authentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository for the roles.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByAuthority(@NonNull Authority authority);

    boolean existsByAuthority(@NonNull Authority authority);
}
