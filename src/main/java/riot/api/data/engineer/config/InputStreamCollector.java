package riot.api.data.engineer.config;

import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class InputStreamCollector {
    ByteArrayOutputStream targetStream = new ByteArrayOutputStream();


    @SneakyThrows
    public InputStreamCollector collectInputStream(InputStream input) {
        IOUtils.copy(input, targetStream);
        return this;
    }

    public InputStream getStream() {
        return new ByteArrayInputStream(targetStream.toByteArray());
    }

}
