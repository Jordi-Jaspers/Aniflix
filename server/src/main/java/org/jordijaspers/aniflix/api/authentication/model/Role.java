package org.jordijaspers.aniflix.api.authentication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The role entity.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", unique = true)
    private Authority authority;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    /**
     * Creates a non-persisted role with the given authority and description.
     */
    public Role(final Authority authority) {
        this.authority = authority;
        this.description = authority.getDescription();
    }

    @Override
    public String getAuthority() {
        return authority.name();
    }

    public void setAuthority(final String authority) {
        this.authority = Authority.valueOf(authority);
    }
}
