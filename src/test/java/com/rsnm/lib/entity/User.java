package com.rsnm.lib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * Class entity User.
 *
 * @author pablobonansea
 *
 */
@Entity
@Table(name = "users")
@Data
public class User implements Principal {

    /**
     * field id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * field user name.
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * field password.
     */
    @JsonIgnore
    @Column(name = "user_password", length = 50, nullable = false, updatable = false)
    private String userPassword;

    /**
     * Field token.
     */
    @JsonIgnore
    @Column(name = "token", length = 100, updatable = false)
    private String token;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Account> accounts = new HashSet<Account>();

    @JsonIgnore
    @Override
    public String getName() {
        return this.userName;
    }

    /**
     * Field user first name.
     */
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    /**
     * Field user middle name.
     */
    @Column(name = "middle_names", length = 100, nullable = true)
    private String middleName;

    /**
     * Field user last_name.
     */
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    /**
     * Field user email_address.
     */
    // CHECKSTYLE IGNORE MagicNumber FOR NEXT 1 LINE
    @Column(name = "email_address", length = 100, nullable = false)
    private String email;

    /**
     * Field public key.
    */
    @Column(name = "public_key")
    private String publicKey;

    /**
     * Field admin.
    */
    @Column(name = "admin")
    private boolean admin;

}