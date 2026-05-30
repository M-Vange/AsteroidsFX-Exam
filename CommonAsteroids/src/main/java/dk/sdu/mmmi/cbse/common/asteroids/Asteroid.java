package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;


public class Asteroid extends Entity {

    // Making variables to make each asteroid unique
    private final double speed;
    private final double moveAngle;
    private final double rotationSpeed;

    // Constructor to assign asteroid behavior when spawned
    public Asteroid(double speed, double moveAngle, double rotationSpeed) {
        this.speed = speed;
        this.moveAngle = moveAngle;
        this.rotationSpeed = rotationSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getMoveAngle() {
        return moveAngle;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }
}
