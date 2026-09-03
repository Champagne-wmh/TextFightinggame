package com.wmh.ui;

import com.wmh.domain.User;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Login {
    //登录注册主页面
    public void start() {
        System.out.println("   欢迎来到登录注册页面");
        System.out.println("===========================");
        System.out.println("   欢迎来到文字格斗游戏");
        System.out.println("===========================");

        ArrayList<User> list = new ArrayList<>();


        //循环初始操作
        while (true) {
            System.out.println("请选择操作1.登录2.注册3.退出");
            Scanner sc = new Scanner(System.in);
            String choose = sc.next();

            switch (choose) {
                case "1" -> login(list);
                case "2" -> register(list);
                case "3" -> {
                    System.out.println("用户选择退出游戏");
                    System.exit(0);
                }
                default -> System.out.println("输入错误");
            }
        }
    }


    public void register(ArrayList<User> list) {
        System.out.println("用户选择注册");

        User u = new User();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("输入用户名");
            String username = sc.next();
            if (!checkLen(3, 16, username)) {
                System.out.println("长度不符合要求");
                continue;
            }
            if (!checkusername(username)) {
                System.out.println("用户名格式错误");
                continue;
            }
            if (contains(list, username)) {
                System.out.println("用户名已存在");
                continue;
            }
            u.setUsername(username);
            break;
        }//用户名输入结束

        while (true) {
            System.out.println("请输入密码");
            String password1 = sc.next();

            if (!checkLen(3, 8, password1)) {
                System.out.println("密码长度不符合要求");
                continue;
            }
            if (!checkpassword(password1)) {
                System.out.println("密码只能是字母加数字的组合");
                continue;
            }
            System.out.println("请在此输入密码");
            String password2 = sc.next();
            if (!password1.equals(password2)) {
                System.out.println("两次密码输入不一致，请重新输入");
                continue;
            }
            u.setPassword(password1);
            break;
        }
        System.out.println("注册成功");
        list.add(u);

    }


    public void login(ArrayList<User> list) {
        System.out.println("用户选择登录");
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = sc.next();
        if (!contains(list, username)) {
            System.out.println("当前用户名不存在");
            return;
        }
        int index = findIndex(list, username);
        User u = list.get(index);
        if (!u.isStatus()) {
            System.out.println("当前用户状态已禁用");
            return;
        }

        String rightPassword = u.getPassword();
        for (int i = 0; i < 3; i++) {
            System.out.println("请输入密码");
            String password = sc.next();
            while (true) {
                String rightCode = getCode();
                System.out.println("验证码为：" + rightCode);
                System.out.println("请输入验证码");
                String code = sc.next();
                if (rightCode.equalsIgnoreCase(code)) {
                    System.out.println("验证码正确");
                    break;
                } else {
                    System.out.println("验证码错误");
                }
            }//生产验证码并校验
            if (rightPassword.equals(password)) {
                System.out.println("登陆成功");
                FightGame fg = new FightGame();
                fg.gameStart(u.getUsername());
                break;
            } else {
                System.out.println("密码错误");
                if (i == 2) {
                    u.setStatus(false);
                    System.out.println("当前账号已锁定");
                } else {
                    System.out.println("当前还剩下" + (2 - i) + "次机会");
                }
            }
        }


    }

    public int findIndex(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            if (u.getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    public int[] getCount(String userInfo) {
        int charCount = 0;
        int numCount = 0;
        int otherCount = 0;
        for (int i = 0; i < userInfo.length(); i++) {
            char c = userInfo.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                charCount++;
            } else if (c >= '0' && c <= '9') {
                numCount++;
            } else {
                otherCount++;
            }
        }
        return new int[]{charCount, numCount, otherCount};
    }

    public boolean contains(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            if (u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkLen(int minLen, int maxLen, String str) {
        return str.length() >= minLen && str.length() <= maxLen;
    }

    public boolean checkusername(String username) {
        int[] arr = getCount(username);
        return arr[0] > 0 && arr[2] == 0;
    }

    public boolean checkpassword(String password) {
        int[] arr = getCount(password);
        return arr[0] > 0 && arr[1] > 0 && arr[2] == 0;
    }

    public static String getCode() {
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char) ('A' + i));
            list.add((char) ('a' + i));
        }
        for (int i = 0; i < 9; i++) {
            list.add((char) ('0' + i));
        }

        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            int index = r.nextInt(list.size());
            sb.append(list.get(index));
        }
        sb.append(r.nextInt(10));
        return sb.toString();
    }

}
