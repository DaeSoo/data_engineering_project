package riot.api.data.engineer.util;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class RiotApiManager {
    private static final int RATE_LIMIT_PER_MINUTE = 100; // 분당 요청 제한 수
    private static final int RATE_LIMIT_BUFFER = 10; // 버퍼 제한 수
    private static int remainingRequests = RATE_LIMIT_PER_MINUTE; // 남은 요청 수
    private static Date resetTime = new Date(); // 리셋 시간

    // Riot API 요청을 보내는 메소드
    public static String sendRequest(String urlString) throws IOException, InterruptedException {
        // rate limit 확인
        checkRateLimit();

        // API 요청
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Riot-Token", "YOUR_API_KEY");

        // API 응답 처리
        int responseCode = con.getResponseCode();
        if (responseCode == 429) { // rate limit 초과 에러
            long retryAfter = Long.parseLong(con.getHeaderField("Retry-After"));
            System.out.println("Rate limit exceeded. Waiting " + retryAfter + " seconds.");
            Thread.sleep(retryAfter * 1000); // retryAfter초 만큼 대기
            return sendRequest(urlString); // 재귀호출로 다시 시도
        } else {
            remainingRequests = Integer.parseInt(con.getHeaderField("X-App-Rate-Limit-Count"));
            resetTime = new Date(Long.parseLong(con.getHeaderField("X-App-Rate-Limit-Reset")) * 1000);
            return con.getInputStream().toString();
        }
    }

    // rate limit 확인 메소드
    private static void checkRateLimit() throws InterruptedException {
        long waitTime = 0;
        while (remainingRequests <= RATE_LIMIT_BUFFER && new Date().before(resetTime)) {
            waitTime = resetTime.getTime() - new Date().getTime() + 1000; // 1초 대기
            System.out.println("Rate limit reached. Waiting " + waitTime + " milliseconds.");
            Thread.sleep(waitTime);
            waitTime = 0;
        }
        double secondsPerRequest = 60.0 / RATE_LIMIT_PER_MINUTE; // 초당 요청 제한 수
        long waitTimeInMs = (long) (secondsPerRequest * 1000); // 대기 시간 계산
        Thread.sleep(waitTimeInMs);
        remainingRequests--;
    }
}