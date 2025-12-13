package me.elb.squidutils.client.data;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomData {
    private int currentPlayers;
    private int maxPlayers;
    private List<String> playersWaiting;
    private GameStage stage;

    public WaitingRoomData() {
        this.currentPlayers = 0;
        this. maxPlayers = 10;
        this.playersWaiting = new ArrayList<>();
        this.stage = GameStage.LOBBY;
    }

    // Getters y Setters
    public int getCurrentPlayers() { return currentPlayers; }
    public void setCurrentPlayers(int currentPlayers) { this.currentPlayers = currentPlayers; }
    
    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    
    public List<String> getPlayersWaiting() { return playersWaiting; }
    public void setPlayersWaiting(List<String> playersWaiting) { this.playersWaiting = playersWaiting; }
    
    public GameStage getStage() { return stage; }
    public void setStage(GameStage stage) { this.stage = stage; }
}