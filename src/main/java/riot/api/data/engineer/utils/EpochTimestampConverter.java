package riot.api.data.engineer.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
public class EpochTimestampConverter {
    private final Long epochTimestamp;
    private final String targetTimeZone;

    public EpochTimestampConverter(Long epochTimestamp) {
        this.epochTimestamp = epochTimestamp;
        this.targetTimeZone = "Asia/Seoul";
    }

    public String convertToDateString() {
        Instant instant = Instant.ofEpochMilli(epochTimestamp);
        LocalDateTime localDateTime = instant.atZone(ZoneId.of(targetTimeZone)).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }
}
