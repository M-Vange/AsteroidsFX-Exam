import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module Collision {
    requires Common;
    requires CommonAsteroids;

    // java.net.http is Java's built-in HTTP client, used to POST to the
    // ScoreService when an asteroid is destroyed. No extra dependencies needed.
    requires java.net.http;

    uses dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;

    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
}