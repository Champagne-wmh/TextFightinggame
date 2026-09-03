package com.wmh.domain;

public class Character {
    public String name;
    public int HP;
    public int attack;
    public int defense;
    public int maxHP;

    public Character(String name, int HP, int attack, int defense) {
        this.name = name;
        this.HP = HP;
        this.attack = attack;
        this.defense = defense;
        this.maxHP = HP;
    }

    public Character() {
    }

    public boolean isAlive() {
        return this.HP > 0;
    }//判断角色是否存活

    public void heal(int amount) {
        this.HP += amount;
        if (this.HP > this.maxHP) {
            this.HP = this.maxHP;
        }
    }//恢复生命值

    public void takeDamage(int damage) {
        this.HP -= damage;
        if (this.HP < 0) {
            this.HP = 0;
        }
    }//受到伤害

    public String show() {
        return name + "当前生命：" + HP + "攻击" + attack + "防御" + defense;
    }


}
