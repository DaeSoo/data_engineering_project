package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import riot.api.data.engineer.config.MinioAdapter;
import riot.api.data.engineer.dto.UploadResponse;

@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {
    private final MinioAdapter minioAdapter;

    @PostMapping(value = "/upload", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<UploadResponse> upload(
            @RequestPart(value = "files", required = true) Mono<FilePart> files) {
        return minioAdapter.uploadFile(files);
    }

    @PostMapping(path = "/stream", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<UploadResponse> uploadStream(
            @RequestPart(value = "files", required = true) FilePart files, @RequestParam(value = "ttl", required = false) Integer ttl) {
        return minioAdapter.putObject(files);

    }

    @GetMapping(path = "/download")
    public ResponseEntity<Mono<InputStreamResource>> download(
            @RequestParam(value = "filename") String fileName) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE).body(minioAdapter.download(fileName));

    }
}
