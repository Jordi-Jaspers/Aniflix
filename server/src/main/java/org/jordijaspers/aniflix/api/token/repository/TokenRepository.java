package org.jordijaspers.aniflix.api.token.repository;

import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.token.model.Token;
import org.jordijaspers.aniflix.api.token.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The repository for the {@link Token} entity.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE Token t WHERE t.type IN (:types) AND t.user = :user")
    void invalidateTokensWithTypeForUser(@Param("types") List<TokenType> types, @Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expiresAt <= CURRENT_TIMESTAMP")
    void deleteExpiredTokens();

    @Transactional
    @Query("FROM Token t LEFT JOIN FETCH t.user u WHERE t.expiresAt > CURRENT_TIMESTAMP AND t.value = :token")
    Optional<Token> findByValue(String token);
}
