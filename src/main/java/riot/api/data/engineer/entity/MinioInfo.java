package riot.api.data.engineer.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
public class MinioInfo extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "filename")
    private String filename;

    @Column(name = "size")
    private long size;

    @Column(name = "path")
    private String path;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "riot_version")
    private String riotVersion;

}
