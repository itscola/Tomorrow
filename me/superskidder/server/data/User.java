package me.superskidder.server.data;

public class User {
    public String name, password, hwid, rank, gameID;

    public User(String name, String password, String hwid) {
        this.name = name;
        this.password = password;
        this.hwid = hwid;
    }
}
