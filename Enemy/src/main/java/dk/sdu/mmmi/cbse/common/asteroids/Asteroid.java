// For split package testing
// Same package as CommonAsteroids
package dk.sdu.mmmi.cbse.common.asteroids;

// Class exists only to demo split package issues.
// Two classes from different modules in the same package causes JPMS to throw split package error.
public class Asteroid {
    // Intentionally left empty.
}
