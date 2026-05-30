package dk.sdu.mmmi.cbse.common.data;

import java.io.Serializable;
import java.util.UUID;

public class Entity implements Serializable {

    // Ensures every game object has a unique ID
    private final UUID id = UUID.randomUUID();

    private double x;
    private double y;
    private double rotation;

    // Adding ability to add color to entities
    private String color = "BLACK"; // Default fallback color


    // Variable for holding the wireframe shape of objects
    private double[] polygonCoordinates;

    // Collision radius. Circle around objects for hit detection
    private float radius;

    // Variable for entity health
    private int health = 3;


    public String getID() {
        return id.toString();
    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getRotation() { return rotation; }
    public void setRotation(double rotation) { this.rotation = rotation; }

    public float getRadius() { return radius; }
    public void setRadius(float radius) { this.radius = radius; }

    public double[] getPolygonCoordinates() { return polygonCoordinates; }

    // Getter and setter for color names
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        // Accepts standard web color names ie. "RED", "DARKORANGE", "AQUA"
        this.color = color;
    }

    // "..." is a Varargs aka variable argument. Packages the arguments handed into an array
    public void setPolygonCoordinates(double... coordinates) {
        this.polygonCoordinates = coordinates;
    }

    // Getter and setter for entity health
    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        // Ensuring health doesn't drop below 0
        this.health = Math.max(0, health);
    }
}
