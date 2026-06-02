package dk.sdu.mmmi.cbse.main;

import org.springframework.web.client.RestTemplate;

// This class is how the game talks with the microservice ScoreService.
// Uses Spring's RestTemplate to make HTTP calls
public class ScoreClient {

    private final RestTemplate restTemplate;

    // The address of the ScoreService. It runs locally on port 8080.
    private static final String SCORE_URL = "http://localhost:8080/score";

    public ScoreClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Calls GET http://localhost:8080/score and returns the current score as an integer.
    // If the ScoreService isn't running, returns 0 and silently continues.
    public int getScore() {
        try {
            String response = restTemplate.getForObject(SCORE_URL, String.class);
            return Integer.parseInt(response.trim());
        } catch (Exception e) {
            // Score service not available — game still runs normally
            return 0;
        }
    }
}