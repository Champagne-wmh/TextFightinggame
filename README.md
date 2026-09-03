# TextFightinggame — Java 文字格斗游戏

一个纯控制台的 Java 文字冒险 / 回合制格斗小游戏。包含**账号注册登录**、**角色属性点分配**、**回合制战斗**、**关卡推进与属性成长**等完整流程，不依赖任何第三方库，用 JDK 直接编译即可运行。

适合作为 Java 面向对象基础（封装、继承、多态、集合、异常流程控制）的练手项目。

---

## ✨ 功能特性

| 模块 | 说明 |
| --- | --- |
| 注册 | 用户名 3–16 位（字母+数字，必须含字母）；密码 3–8 位（必须同时含字母和数字）；两次密码校验；用户名重复检测 |
| 登录 | 用户名存在性校验 + 账号状态校验 + 4 位字母数字 + 1 位数字的随机验证码；密码最多 3 次机会，失败锁定账号 |
| 角色创建 | 20 点自由属性点分配到生命 / 攻击 / 防御 |
| 战斗 | 回合制，玩家 3 选 1 行动，敌人 50% 概率释放专属技能；带 ASCII 血条 |
| 关卡 | 随机敌人、无限关卡、每胜一场全体敌人增强 |
| 成长 | 每胜一场恢复 20–40 HP；每 3 胜永久提升属性 |

---

## 🛠 环境要求

- **JDK 21+**（推荐 JDK 25）

> ⚠️ 注意：入口类 `App` 的 `main` 方法签名是 `static void main()`（无 `public`、无 `String[] args`）。
> 这种简化写法依赖 Java 21 起引入、**Java 25 正式转正**的启动协议放宽（JEP 512）。
> 已在 `java 25.0.3` 上实测可正常启动；若在 **Java 21 及以下**运行会报 `Main method not found`，需要改回：
> ```java
> public static void main(String[] args) { ... }
> ```

无需 Maven / Gradle，无外部依赖。

---

## 📁 目录结构

```
TextFightinggame-main/
└── TextFightinggame-main/
    ├── README.md
    └── src/                          # IDEA 中标记为 Sources Root
        ├── App.java                  # 程序入口
        └── com/wmh/
            ├── domain/               # 实体层
            │   ├── Character.java        # 角色基类（HP/攻击/防御/存活/受伤/治疗）
            │   ├── HeroCharater.java     # 玩家角色（技能列表）
            │   ├── EnemyCharater.java    # 敌人（技能 / 防御姿态减伤）
            │   └── User.java             # 账号实体（id/用户名/密码/状态）
            └── ui/                   # 界面与流程层
                ├── Login.java           # 登录注册主页面、验证码、校验规则
                └── FightGame.java       # 战斗主循环、伤害计算、血条
```

包名 `wmh`、类名 `Charater` / `EnemyCharater`（`Character` 少了 `r`）均为原作者拼写，为保持与 `java.lang.Character` 区分而保留，**未做重命名**。

---

## 🚀 快速开始

### 方式一：命令行

在**项目最外层目录**（含 `TextFightinggame-main.iml` 的那层）执行：

```bash
# 编译
javac -encoding UTF-8 -d out $(find TextFightinggame-main/src -name "*.java")

# 运行
java -cp out App
```

Windows PowerShell 版本：

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java TextFightinggame-main\src).FullName
java -cp out App
```

### 方式二：IntelliJ IDEA

1. `File → Open`，选择**外层**目录 `TextFightinggame-main`
2. 确认 `TextFightinggame-main/src` 被标记为 Sources Root（`.iml` 中已配置）
3. Project SDK 选 21 及以上
4. 运行 `App.java`

---

## 🎮 游戏流程

```
启动 App
   ↓
登录注册页面 ──1──→ 登录（验证码 + 密码）──成功──→ 进入战斗
   │                                              ↓
   ├──────────2──→ 注册（用户名/密码校验）        创建角色（分配 20 点属性）
   │                                              ↓
   └──────────3──→ 退出                        回合制战斗循环
                                                  ↓
                                        胜负 → 恢复 HP → 成长 → 是否继续
```

### 1. 创建角色

基础属性：**100 HP / 10 攻击 / 0 防御**，另有 **20 点属性点**自由分配：

| 属性 | 每点收益 |
| --- | --- |
| 生命值 | +10 HP |
| 攻击力 | +2 攻击 |
| 防御力 | +1 防御 |

分配按顺序进行（生命 → 攻击 → 防御），单次输入超过剩余点数会被截断，**未用完的点数不会保留**。

### 2. 玩家技能

| 选项 | 技能 | 效果 | 消耗 |
| --- | --- | --- | --- |
| 1 | 普通攻击 | 伤害 = 攻击 − 敌防御 | 无 |
| 2 | 强力一击 | 伤害 = 攻击 × 1.8 − 敌防御 | 10 HP（HP > 10 才可用） |
| 3 | 生命汲取 | 随机恢复 0–20 HP | 10 HP（HP > 10 才可用） |

> 所有伤害最低为 **1**（不会出现 0 或负数伤害）。

### 3. 敌人一览

| 敌人 | HP | 攻击 | 防御 | 专属技能 | 技能效果 |
| --- | --- | --- | --- | --- | --- |
| 初级战士 | 80 | 15 | 10 | 猛击 | 伤害 = 攻击 × 1.5 |
| 敏捷刺客 | 60 | 20 | 5 | 快速攻击 | 两段攻击，各为「攻击 ÷ 2 − 防御」 |
| 重装坦克 | 120 | 10 | 20 | 防御姿态 | 本回合起受到伤害减半（最低 1） |
| 神秘法师 | 70 | 25 | 8 | 火球术（180% 伤害） | 伤害 = 攻击 × 1.8 |

敌人每回合有 **50%** 概率使用专属技能，否则使用普通攻击。

### 4. 关卡与成长

- 每关从敌人列表中**随机**抽取一名对手
- **每胜一场**：全部敌人 `最大HP +10`、`攻击 +3`、`防御 +2`，并回满血
- **每胜一场**：玩家恢复 `20 ~ 40` HP
- **每 3 胜**：玩家 `最大HP +30`、`攻击 +5`、`防御 +3`
- 每关结束后输入 `y` 继续 / `n` 结束（不区分大小写）

---

## 🧩 核心类说明

| 类 | 职责 |
| --- | --- |
| `Character` | 角色基类，持有 `name / HP / maxHP / attack / defense`，提供 `isAlive()`、`heal()`、`takeDamage()`、`show()` |
| `HeroCharater` | 继承 `Character`，额外持有 `skillList` 技能列表 |
| `EnemyCharater` | 继承 `Character`，持有 `skill` 与 `defending` 标志，并**重写 `takeDamage()` 实现防御减伤**（多态的典型用法） |
| `User` | 账号实体，`creatID()` 生成 `heima` 前缀 + 5 位数字的 ID，`status` 表示账号是否被锁定 |
| `Login` | 登录注册主循环、验证码生成、各类格式校验工具方法 |
| `FightGame` | 游戏主循环、角色创建、玩家/敌人回合、伤害公式、ASCII 血条绘制 |

---

## ⚠️ 已知问题与改进建议

以下问题已在源码中确认存在，按影响程度排列，可作为后续练手的改造清单：

| # | 问题 | 位置 |
| --- | --- | --- |
| 1 | **神秘法师的「火球术」永远不会触发**。敌人 `skill` 存的是 `"火球术(180%伤害)"`，但 `enemyTurn` 的 `switch` 匹配的是 `"火球术"`，字符串不匹配导致**该回合敌人不造成任何伤害**。建议把 `skill` 改为纯技能名，倍率另设字段 | `FightGame.java` |
| 2 | **`addSkill()` 实际是清空技能列表**。方法体内写的是 `skillList = new ArrayList<>()` 而非 `add()`，调用后技能全部丢失 | `HeroCharater.java` |
| 3 | **多处 `new Scanner(System.in)`**。程序中反复创建 Scanner，每个都会预读缓冲，导致一次性粘贴多行输入时数据被吞掉。建议全局共用一个 Scanner 并复用 | `Login.java` / `FightGame.java` |
| 4 | **「是否继续游戏」输入非法时会直接开打**。既不是 `y` 也不是 `n` 时不 `break`，循环直接重跑并重抽敌人、再次强化敌人 | `FightGame.java` |
| 5 | 关卡数 `count++` 被注释掉，只在选择 `y` 时自增，关号显示与逻辑不一致 | `FightGame.java` |
| 6 | `switch` 中 `default` 写在 `case 1` 之前，靠贯穿（fall-through）实现「非法输入默认普通攻击」，可读性差且易误改 | `FightGame.java` |
| 7 | `player.show()` 有返回值但调用处未 `println`，初始属性实际不会打印出来 | `FightGame.java` |
| 8 | **用户数据仅存于内存** `ArrayList`，程序退出即丢失。可改造为文件 / 数据库持久化 | `Login.java` |
| 9 | 密码明文存储，可引入哈希（如 BCrypt / 加盐摘要） | `User.java` |
| 10 | `creatID()` 只随机一次数字后重复拼接 5 次，生成的是 5 个**相同**数字（如 `heima77777`），随机性不足 | `User.java` |
| 11 | 游戏结束直接 `System.exit(0)`，无法返回登录页重开 | `FightGame.java` |

---

## 🔧 可扩展方向

- 数据持久化：账号、最高胜场、排行榜写入文件或 SQLite
- 技能系统重构：用枚举 / 策略模式替代字符串 `switch`，支持配置化技能表
- 战斗表现：加入暴击、闪避、状态异常（中毒 / 眩晕）、技能冷却
- 输入层封装：统一的 `InputUtil`，避免 Scanner 复用问题，并做健壮的非法输入处理
- 单元测试：为 `calculateDamage()`、`takeDamage()` 减伤逻辑补充 JUnit 测试
- 图形化：迁移到 JavaFX 或 Web 前端，保留现有 domain 层

---

## 📄 说明

项目源自 Java 基础阶段练习，代码保留原始结构与命名风格（含 `Charater` 拼写），README 中的「已知问题」部分已实测验证，供后续迭代参考。
