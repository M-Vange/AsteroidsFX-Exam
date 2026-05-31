package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Enemy extends Entity {
    private int decisionTimer = 0;
    // Movement state is the variable changing randomly to decide movement for enemies
    // 0=idle, 1=forward, 2=left and 3=right
    private int movementState = 0;

    public int getDecisionTimer() { return decisionTimer; }
    public void setDecisionTimer(int decisionTimer) { this.decisionTimer = decisionTimer; }

    public int getMovementState() { return movementState; }
    public void setMovementState(int movementState) { this.movementState = movementState; }
}