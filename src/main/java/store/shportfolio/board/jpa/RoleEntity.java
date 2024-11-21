package store.shportfolio.board.jpa;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Table(name = "roles")
@Entity
@Getter
@NoArgsConstructor
public class RoleEntity {

    @Id
    @Column(name = "rold_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Builder
    public RoleEntity(RoleEnum roleName, UserEntity user) {
        this.roleName = roleName;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity role = (RoleEntity) o;
        return Objects.equals(id, role.id) && roleName == role.roleName && Objects.equals(user, role.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, user);
    }
}
