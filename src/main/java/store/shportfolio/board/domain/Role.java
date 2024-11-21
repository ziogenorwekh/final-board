package store.shportfolio.board.domain;

import lombok.Getter;
import store.shportfolio.board.jpa.RoleEntity;
import store.shportfolio.board.jpa.RoleEnum;
import store.shportfolio.board.valueobject.UserId;

@Getter
public class Role {

    private final UserId userId;
    private final RoleEnum role;

    public Role(UserId userId, RoleEntity roleEntity) {
        this.userId = userId;
        this.role = roleEntity.getRoleName();
    }

    public Role(UserId userId, RoleEnum role) {
        this.userId = userId;
        this.role = role;
    }
}
