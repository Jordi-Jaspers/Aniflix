package org.jordijaspers.aniflix.api.token.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jordijaspers.aniflix.api.user.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The token model which represents a token in the database.
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@Table(name = "token")
public class Token implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value", unique = true)
    private String value;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TokenType type;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Token(final String value, final LocalDateTime expiresAt, final TokenType type, final User user) {
        this.value = value;
        this.type = type;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
