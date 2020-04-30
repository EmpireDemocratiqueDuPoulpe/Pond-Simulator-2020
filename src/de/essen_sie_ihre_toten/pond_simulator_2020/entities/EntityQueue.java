package de.essen_sie_ihre_toten.pond_simulator_2020.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityQueue {
    // Attributes
    private Entity leader;
    private int sizeLimit;
    private List<float[]> lastLeaderPos;

    private List<Entity> members;

    // Constructors
    public EntityQueue() {
        this.leader = null;
        this.sizeLimit = 10;
        this.lastLeaderPos = new ArrayList<>();

        this.members = new ArrayList<>();
    }

    public EntityQueue(Entity leader) {
        this.leader = leader;
        this.sizeLimit = 10;
        this.lastLeaderPos = new ArrayList<>();

        this.members = new ArrayList<>();
    }

    public EntityQueue(Entity leader, int sizeLimit) {
        this.leader = leader;
        this.sizeLimit = sizeLimit;
        this.lastLeaderPos = new ArrayList<>();

        this.members = new ArrayList<>();
    }

    // Getters
    public Entity getLeader()               { return this.leader; }
    public int getSizeLimit()               { return this.sizeLimit; }
    public List<float[]> getLastLeaderPos() { return this.lastLeaderPos; }
    public List<Entity> getMembers()        { return this.members; }

    // Setters
    public void setLeader(Entity leader)                        { this.leader = leader; }
    public void setSizeLimit(int limit)                         { this.sizeLimit = limit; this.lastLeaderPos = this.lastLeaderPos.stream().limit(limit).collect(Collectors.toList()); }
    public void setLastLeaderPos(List<float[]> lastLeaderPos)   { this.lastLeaderPos = lastLeaderPos; }
    public void setMembers(List<Entity> members)                { this.members = members; }

    // Methods
    // Pos
    public void addLastLeaderPos(float... pos) {
        if (!isInPos(pos)) {
            this.lastLeaderPos.add(pos);
            checkLeaderPosList();
        }
    }

    // Queue
    public void addMember(Entity member) {
        if (!isInQueue(member) && !member.isInQueue()) {
            this.members.add(member);
            member.setInQueue(true);
        }
    }

    public void follow() {
        if (!(this.leader instanceof EntityMoveable)) return;

        // Get offset between points
        EntityMoveable lead = ((EntityMoveable) this.leader);
        float spacing = 25;
        float offsetX = 0;
        float offsetY = 0;

        if (this.leader.getX() > lead.getTargetX()) { offsetX = +((this.leader.getWidth() / 2) + spacing); }
        else if (this.leader.getX() < lead.getTargetX()) { offsetX = -((this.leader.getWidth() / 2) - spacing); }

        if (this.leader.getY() > lead.getTargetY()) { offsetY = +(spacing); }
        else if (this.leader.getY() < lead.getTargetY()) { offsetY = -(this.leader.getHeight() - spacing); }

        for (int i = 0; i < this.members.size(); i++) {
            Entity member = this.members.get(i);

            // Prevent from trying to move an entity that cannot move
            if (!(member instanceof EntityMoveable)) continue;

            // Set the new target
            float[] pos = this.lastLeaderPos.get(i);
            ((EntityMoveable) member).setTarget(pos[0] + (offsetX * (i + 1)), pos[1] + (offsetY * (i + 1)));
        }
    }

    private boolean isInQueue(Entity entity) {
        return this.members.contains(entity);
    }

    public boolean isQueueEmpty() {
        return this.members.size() == 0;
    }

    public void emptyQueue() {
        this.members = new ArrayList<>();
    }

    public void freeAll() {
        for (Entity member : this.members) {
            member.setInQueue(false);
        }

        emptyQueue();
    }

    // Lists
    private void checkLeaderPosList() {
        while (this.lastLeaderPos.size() > this.sizeLimit) {
            this.lastLeaderPos.remove(0);
        }
    }

    private boolean isInPos(float[] pos) {
        return this.lastLeaderPos.contains(pos);
    }
}
