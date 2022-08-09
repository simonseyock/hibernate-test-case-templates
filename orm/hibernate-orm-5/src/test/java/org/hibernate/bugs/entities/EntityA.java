package org.hibernate.bugs.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityA {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(
            unique = true,
            nullable = false
    )
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "EntityA_EntityB",
            joinColumns = @JoinColumn(name = "entity_a_id"),
            inverseJoinColumns = @JoinColumn(name = "entity_b_id")
    )
    @AuditJoinTable
    private List<EntityB> otherBs = new ArrayList<>();
}