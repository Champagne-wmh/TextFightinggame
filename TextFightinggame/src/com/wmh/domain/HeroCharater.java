package com.wmh.domain;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HeroCharater extends Character{
        public ArrayList<String> skillList;

        public HeroCharater(String name, int HP, int attack, int defense) {
            super(name, HP, attack, defense);
            skillList = new ArrayList<String>();
        }

        public void addSkill(String skill) {
            skillList = new ArrayList<String>();
        }

        public String showSkill() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < skillList.size(); i++) {
                sb.append(skillList.get(i)).append(" ");
            }
            return sb.toString();
        }

}
