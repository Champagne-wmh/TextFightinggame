package com.wmh.ui;

import com.wmh.domain.EnemyCharater;
import com.wmh.domain.HeroCharater;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FightGame {
    public void gameStart(String username) {
        System.out.println("===========================");
        System.out.println("   " + username + "欢迎来到文字格斗游戏");
        System.out.println("===========================");

        HeroCharater player = creatPlayerCharater(username);
        System.out.println("角色创建成功");
        System.out.print("初始属性为");
        player.show();
        System.out.println("拥有的技能" + player.showSkill());

        ArrayList<EnemyCharater> enemyList = new ArrayList<EnemyCharater>();
        enemyList.add(new EnemyCharater("初级战士", 80, 15, 10, "猛击"));
        enemyList.add(new EnemyCharater("敏捷刺客", 60, 20, 5, "快速攻击"));
        enemyList.add(new EnemyCharater("重装坦克", 120, 10, 20, "防御姿态"));
        enemyList.add(new EnemyCharater("神秘法师", 70, 25, 8, "火球术(180%伤害)"));


        int count = 1;
        int wins = 0;
        while (player.isAlive()) {
            if (wins != 0) {
                for (int i = 0; i < enemyList.size(); i++) {
                    EnemyCharater c = enemyList.get(i);
                    c.maxHP += 10;
                    c.HP = c.maxHP;
                    c.attack += 3;
                    c.defense += 2;
                    c.defending = false;
                }
            }
            Random r = new Random();
            int index = r.nextInt(enemyList.size());
            EnemyCharater enemy = enemyList.get(index);

            System.out.println("---------------------");
            System.out.println("第" + count + "关" + enemy.name + "出现");
            System.out.println(enemy.show());
            int round = 1;
            while (player.isAlive()) {
                System.out.println("---------------------");
                System.out.println("第" + count + "关");
                System.out.println(getHealthBar(player.name, player.HP, player.maxHP));
                System.out.println(getHealthBar(enemy.name, enemy.HP, enemy.maxHP));


                playerTurn(player, enemy);
                if (!enemy.isAlive()) {
                    System.out.println("击败" + enemy.name);
                    wins++;
                    break;
                }

                enemyTurn(enemy, player);
                if (!player.isAlive()) {
                    System.out.println("被" + enemy.name + "击败");
                    break;
                }
                System.out.println("第" + round + "轮结束");
                round++;
            }
//            count++;
            if (player.isAlive()) {
                Random r1 = new Random();
                int healHP = r1.nextInt(21) + 20;
                player.heal(healHP);
                System.out.println("恢复" + healHP + "HP");
                System.out.println("当前胜场" + wins);
            }
            if (player.isAlive() && wins > 0 && wins % 3 == 0) {
                System.out.println("恭喜，属性获得提升");
                player.maxHP += 30;
                player.attack += 5;
                player.defense += 3;
                System.out.println("最大生命值+30，攻击力+5，防御力+3");
                player.show();
            }
            if (player.isAlive()) {
                System.out.println("是否继续游戏？");
                Scanner sc = new Scanner(System.in);
                String choose = sc.next();
                if ("y".equalsIgnoreCase(choose)) {
                    count++;
                    continue;
                } else if ("n".equalsIgnoreCase(choose)) {
                    System.out.println("游戏结束");
                    break;
                }
            }


        }
        System.out.println("=============================");
        System.out.println("游戏结束");
        System.out.println("总胜场" + wins);
        System.out.println("感谢游玩");
        System.exit(0);


    }

    public String getHealthBar(String name, int HP, int maxHP) {
        int barLength = 20;
        double ratio = (double) HP / maxHP;
        int filledLength = (int) (ratio * barLength);
        String healthBar = "[";
        for (int i = 0; i < barLength; i++) {
            if (i < filledLength) {
                healthBar += "#";
            } else {
                healthBar += "-";
            }
        }
        healthBar += "]";
        return name + ":" + healthBar + " " + HP + "/" + maxHP + "HP";
    }

    public HeroCharater creatPlayerCharater(String username) {
        System.out.println("创建您的角色");
        System.out.println("您的角色名为：" + username);

        int points = 20;
        System.out.println("请分配属性点(20点)");
        System.out.println("1.生命值（每点加10HP）");
        System.out.println("2.攻击力（每点加2攻击）");
        System.out.println("3.防御力（每点加1防御）");

        Scanner sc = new Scanner(System.in);
        String[] attributes = {"生命值", "攻击力", "防御力"};
        int[] values = {0, 0, 0};

        for (int i = 0; i < 3; i++) {
            System.out.println("分配点数到" + attributes[i]);
            int input = sc.nextInt();
            if (input < 0) {
                System.out.println("属性点不能为负数");
                input = 0;
            }
            if (input > points) {
                System.out.println("属性点不能超过20");
                input = points;
            }
            points -= input;
            values[i] = input;
        }//分配属性点

        HeroCharater player = new HeroCharater(username, 100 + values[0] * 10, 10 + values[1] * 2, 0 + values[2]);

        player.skillList.add("普通攻击");
        player.skillList.add("强力一击");
        player.skillList.add("生命汲取");

        return player;

    }

    public void playerTurn(HeroCharater player, EnemyCharater enemy) {
        System.out.println("请选择行动");
        System.out.println("1.普通攻击");
        System.out.println("2.强力一击");
        System.out.println("3.生命汲取");
        Scanner sc = new Scanner(System.in);
        int choose = sc.nextInt();
        switch (choose) {
            default:
                System.out.println("自动选择普通攻击");
            case 1:
                int damage1 = calculateDamage(player.attack, enemy.defense);
                System.out.println("攻击造成" + damage1 + "伤害");
                enemy.takeDamage(damage1);
                break;
            case 2:
                if (player.HP > 10) {
                    player.takeDamage(10);
                    int damage2 = calculateDamage((int) (player.attack * 1.8), enemy.defense);
                    System.out.println("攻击造成" + damage2 + "伤害");
                    enemy.takeDamage(damage2);
                    System.out.println("消耗10HP，攻击造成" + damage2 + "伤害");
                } else {
                    System.out.println("体力不足，攻击失败");
                }
                break;
            case 3:
                if (player.HP > 10) {
                    player.takeDamage(10);
                    Random r = new Random();
                    int healHP = r.nextInt(21);
                    player.heal(healHP);
                    System.out.println("消耗10HP恢复" + healHP + "HP");
                } else {
                    System.out.println("体力不足，恢复生命失败");
                }
                break;
        }


    }

    public void enemyTurn(EnemyCharater enemy, HeroCharater player) {
        System.out.println(enemy.name + "的回合");
        String action = "普通攻击";
        Random r = new Random();
        int num = r.nextInt(10);
        if (num >= 5) {
            action = enemy.skill;
        }


        switch (action) {
            case "普通攻击":
                System.out.println(enemy.name + "使用普通攻击");
                int damage1 = calculateDamage(enemy.attack, player.defense);
                System.out.println("攻击造成" + damage1 + "伤害");
                player.takeDamage(damage1);
                break;
            case "猛击":
                System.out.println(enemy.name + "使用猛击");
                int damage2 = calculateDamage((int) (enemy.attack * 1.5), player.defense);
                System.out.println("攻击造成" + damage2 + "伤害");
                player.takeDamage(damage2);
                break;
            case "快速攻击":
                System.out.println(enemy.name + "使用快速攻击");
                int damage3 = 0;
                for (int i = 0; i < 2; i++) {
                    int temp = calculateDamage((int) (enemy.attack / 2), player.defense);
                    damage3 += temp;
                }
                System.out.println("攻击造成" + damage3 + "伤害");
                player.takeDamage(damage3);
                break;
            case "防御姿态":
                System.out.println(enemy.name + "使用防御姿态");
                enemy.defending = true;
                break;
            case "火球术":
                System.out.println(enemy.name + "使用火球术");
                int damage4 = calculateDamage((int) (enemy.attack * 1.8), player.defense);
                System.out.println("攻击造成" + damage4 + "伤害");
                player.takeDamage(damage4);
                break;
        }


    }

    public int calculateDamage(int attack, int defense) {
        int damage = attack - defense;
        if (damage < 1) {
            damage = 1;
        }
        return damage;
    }
}
