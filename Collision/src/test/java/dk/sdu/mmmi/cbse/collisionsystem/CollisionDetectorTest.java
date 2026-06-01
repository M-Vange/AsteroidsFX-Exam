package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Unit test for CollisionDetector.
// Testing collides() method, which just involves math.
// collides() has no external dependencies, so no mocking needed.

class CollisionDetectorTest {

    // The thing being tested
    private CollisionDetector collisionDetector;

    // @BeforeEach runs once before each following test method.
    // Gives each test a new CollisionDetector, so tests shouldn't affect each other.
    @BeforeEach
    void setUp() {
        collisionDetector = new CollisionDetector();
    }

    // Test 1. Two entities with circles overlapping should register a collision.
    // The distance between them is 5. Sum of radii = 20. 5 < 20, so they should collide.
    @Test
    void overlappingEntities_shouldCollide() {
        Entity entity1 = new Entity() {};
        entity1.setX(0);
        entity1.setY(0);
        entity1.setRadius(10);

        Entity entity2 = new Entity() {};
        entity2.setX(5);
        entity2.setY(0);
        entity2.setRadius(10);

        assertTrue(collisionDetector.collides(entity1, entity2),
                "Entities with overlapping radii should be detected as colliding");
    }

    // Test 2. Two entities far enough apart without overlapping radii should not collide.
    // The distance between them is 100. Sum of radii = 10. 100 > 10, so they shouldn't collide.
    @Test
    void distantEntities_shouldNotCollide() {
        Entity entity1 = new Entity() {};
        entity1.setX(0);
        entity1.setY(0);
        entity1.setRadius(5);

        Entity entity2 = new Entity() {};
        entity2.setX(100);
        entity2.setY(0);
        entity2.setRadius(5);

        assertFalse(collisionDetector.collides(entity1, entity2),
                "Entities far apart should not be detected as colliding");
    }

    // Test 3. Edge case test. Two entities whose radii barely touch at the edge.
    // They should not collide, since the condition is '<' and not "<="
    @Test
    void exactBoundaryEntities_shouldNotCollide() {
        Entity entity1 = new Entity() {};
        entity1.setX(0);
        entity1.setY(0);
        entity1.setRadius(5);

        // distance = exactly 10, sum of radii = exactly 10
        Entity entity2 = new Entity() {};
        entity2.setX(10);
        entity2.setY(0);
        entity2.setRadius(5);

        assertFalse(collisionDetector.collides(entity1, entity2),
                "Entities touching at exactly the boundary should not collide");
    }
}