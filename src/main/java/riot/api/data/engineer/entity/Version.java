package riot.api.data.engineer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "version")
@Setter
@Getter
@NoArgsConstructor
public class Version {
    @Id
    String version;
    @Column(name = "current_version_yn")
    Boolean currentVersionYn;
    @Column(name = "update_time")
    Timestamp updateTime;

    public Version(String version,Boolean currentVersionYn){
        this.version = version;
        this.currentVersionYn = currentVersionYn;
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }
}
