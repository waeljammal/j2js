package com.rsnm.lib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An Account object.
 *
 * @author Endre Czirbesz <endre.czirbesz@askblackswan.com>
 */
@Entity
@Table(name = "accounts")
@JsonInclude(Include.NON_DEFAULT)
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "airline_id")
    private String airlineId;

    @Column(name = "logo")
    private String logo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @JsonIgnore
    private List<Portal> portals;

    @JsonIgnore
    private Set<User> users = new HashSet<User>();
}
