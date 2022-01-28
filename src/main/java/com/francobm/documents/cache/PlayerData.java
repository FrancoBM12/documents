package com.francobm.documents.cache;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private int id;
    private final Player player;
    private String name;
    private String lastName;
    private String dateBirth;
    private String nationality;
    private String gender;
    private String job;
    private List<String> history;
    private boolean complete;
    private String chat;

    public PlayerData(Player player, int id, String name, String lastName, String dateBirth, String nationality, String gender, String job, List<String> history) {
        this.player = player;
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dateBirth = dateBirth;
        this.nationality = nationality;
        this.gender = gender;
        this.job = job;
        this.history = history;
        complete = true;
        chat = "";
    }

    public PlayerData(Player player){
        this.player = player;
        complete = false;
        this.name = "";
        this.lastName = "";
        this.dateBirth = "";
        this.nationality = "";
        this.gender = "";
        this.job = "";
        this.history = new ArrayList<>();
        chat = "";
    }

    public Player getPlayer() {
        return player;
    }

    public String getFullName(){
        return name + " " + lastName;
    }
    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getGender() {
        return gender;
    }

    public String getJob() {
        return job;
    }

    public List<String> getHistory() {
        return history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean getComplete(){
        return !name.isEmpty() && !lastName.isEmpty() && !dateBirth.isEmpty() && !nationality.isEmpty() && !gender.isEmpty() && !job.isEmpty() && !history.isEmpty();
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
