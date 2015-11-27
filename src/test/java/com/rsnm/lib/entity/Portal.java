package com.rsnm.lib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Endre Czirbesz <endre.czirbesz@askblackswan.com>
 */
@Entity
@Table(name = "portals")
@JsonInclude(Include.NON_EMPTY)
@Data
public class Portal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_microsite", nullable = false)
    private boolean isMicrosite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account owner;

    /**
     * Microsites that can be linked to from the portal - as setup by admin.
     * <p>This is not the link itself - the link is only a shortcode in a portal page.</p>
     */
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Portal> micrositesAllowedForLink = new HashSet<Portal>();

    @Column(name = "template_url", nullable = true)
    private String url;

    @Column(name = "git_validation", nullable = false)
    private boolean gitValidation;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PortalStatus status = PortalStatus.active;
}
