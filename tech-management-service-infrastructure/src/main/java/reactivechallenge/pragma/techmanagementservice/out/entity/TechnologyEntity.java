package reactivechallenge.pragma.techmanagementservice.out.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("technology")
public record TechnologyEntity(@Id @Column("id") Long id, @Column("name") String name,
                               @Column("description") String description) {
}
