package com.example.toby_springframework_240526.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;
    Date lastUpgraded;



    public void upgradeLevel() {
        Level nextLevel = this.level.getNext();
        if (nextLevel == null) {
            throw new IllegalArgumentException(this.level + "은 업그레이드가 불가능합니다.");
        } else {
            this.level = nextLevel;
            this.lastUpgraded = new Date();
        }
    }
}
