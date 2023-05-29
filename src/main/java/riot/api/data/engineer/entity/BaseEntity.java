package riot.api.data.engineer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity {

    /*
     * 생성일
     */
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    /*
     * 갱신일
     */
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        lastUpdateDate = now;
    }

    @PreUpdate
    public void preUpdate(){
        lastUpdateDate = LocalDateTime.now();
    }

}
