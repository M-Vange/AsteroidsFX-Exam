package dk.sdu.mmmi.cbse.score;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This is the entry point for the ScoreService.
// @SpringBootApplication starts an embedded web server on port 8080.
// The game will talk to this server over HTTP and get updates for the score.

@SpringBootApplication
public class ScoreApplication {


    // Press green run button and leave running to use microservice.
    public static void main(String[] args) {
        SpringApplication.run(ScoreApplication.class, args);
    }
}