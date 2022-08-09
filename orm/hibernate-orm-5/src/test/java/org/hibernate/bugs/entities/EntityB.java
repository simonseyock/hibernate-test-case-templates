package org.hibernate.bugs.entities;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
public class EntityB {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(
            unique = true,
            nullable = false
    )
    private Long id;

    @ManyToMany(mappedBy = "otherBs")
    private List<EntityA> otherAs = new ArrayList<>();

    @Column
    private String someField;
}
