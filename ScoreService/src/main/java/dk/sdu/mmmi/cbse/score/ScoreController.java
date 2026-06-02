package dk.sdu.mmmi.cbse.score;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// The @RestController annotation means this class handles HTTP request and responses.
// @RequestMapping("/score") means all endpoints start with /score.
// GET = http://localhost:8080/score
// POST = http://localhost:8080/score/increment

@RestController
@RequestMapping("/score")
public class ScoreController {

    private int score = 0;

    // GET /score = the game calls this to display the current score.
    // Returns plain text to avoid needing JSON parsing in the game.
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String getScore() {
        return String.valueOf(score);
    }

    // POST /score/increment = the game calls this when an asteroid is destroyed.
    // Adds 1 to the score and returns the new total.
    @PostMapping(value = "/increment", produces = MediaType.TEXT_PLAIN_VALUE)
    public String increment() {
        score++;
        return String.valueOf(score);
    }
}