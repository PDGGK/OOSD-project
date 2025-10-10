# Shadow Donkey Kong

> Full-mark SWEN20003 semester project that rebuilds the classic Donkey Kong arcade experience in Java and extends it with a second, combat-focused level.

## Overview

This repository contains both halves of the assessment:

- **Project 1** lays the foundation – player movement, ladders, barrels, hammer power-up, scoring and a polished game loop.
- **Project 2** evolves the engine into a multi-level experience with intelligent enemies, projectiles, blasters, blazingly fast state transitions and a richer UI.  

All gameplay is built on the Bagel 2D engine, organised with clean object-oriented design, and configured through external properties to keep the codebase adaptable.

## Feature Highlights

- 🎮 Two fully playable levels with unique win conditions (hammer showdown in Level 1, health-based boss fight in Level 2).
- 🧠 AI-driven monkeys (patrolling + banana-throwing variants) and a projectile system for both player and enemies.
- 🧰 Robust architecture: entity hierarchy, behavioural interfaces, reusable gameplay framework and configuration-driven level data.
- 📊 Comprehensive scoring: jump/destroy rewards, time bonus, health HUD and per-level score carry-over.
- 🌐 Portfolio-ready development journal (`project-journal-website/`) with bilingual copy, theme toggle and animated storytelling.

## Controls

- `← / →` — Run left / right (air control supported).
- `↑ / ↓` — Climb ladders.
- `SPACE` — Jump.
- `H` — Automatically wield the hammer when collected.
- `F` — Fire blaster bullets (Level 2, while you have ammo).
- `ESC` — Quit to desktop.
- Title screen shortcuts: `ENTER` → Level 1, `2` → Jump straight to Level 2.

## Getting Started

```bash
# Requirements: Java 17+, Maven 3.x

# Run either project (from its directory)
./run.sh

# Or do it manually
mvn clean compile dependency:copy-dependencies -DoutputDirectory=target/dependency
java -Djava.library.path=target/dependency -cp target/classes:target/dependency/* ShadowDonkeyKong
```

Bagel’s native libraries are copied into `target/dependency/` on build; the bundled `run.sh` script sets up the classpath automatically.

## Repository Layout

| Path | Description |
| --- | --- |
| `dazd1-project-1/DonkeyKong_Skeleton/` | Project 1 submission – foundational gameplay loop. |
| `dazd1-project-1/DonkeyKongsolution/` | Earlier solution snapshot kept for reference. |
| `dazd1-project-2/DonkeyKong_Skeleton/` | Project 2 submission – advanced multi-level implementation. |
| `dazd1-project-2/project-journal-website/` | Interactive development journal (open `index.html` locally). |
| `project_report.md` | 600+ line deep dive into design decisions, physics, AI and testing. |
| `1488802.txt` | Sample Git commit log generated for the submission rubric. |
| `Archive.zip` | Packaged artefact produced for final hand-in. |

## Level Breakdown

### Level 1 – Classic Arcade Challenge

- Core mechanics: running, climbing, jumping, hammer combat.
- Barrel physics with per-barrel cooldowns and cross-level jump validation to prevent accidental scoring.
- Time pressure overlay and state transitions (title ↔ play ↔ win/lose).
- Clean code foundation: `GameplayScreen`, `Entity` hierarchy, collision helpers, `ScoreManager`.

### Level 2 – Combat Focus & AI

- Adds Donkey Kong health bar, bullet HUD and level-to-level score carry-over.
- Introduces blaster power-up, bullets and banana projectiles with shared `Projectile` base class.
- Normal and intelligent monkeys inherit from `Monkey`, using patrol paths and AI hooks (auto-direction change, timed banana throws).
- Expanded collision rules: hammer beats monkeys, bananas ignore hammer, bullets damage DK & mobs.
- Level-specific UI built dynamically from `app.properties`.

## Technical Notes

- **Architecture & Interfaces:** `Entity` + `Movable`/`Collidable`/`Destroyable`/`Weapon` interfaces keep behaviours modular and encourage reuse.
- **Screen & State Machine:** `ShadowDonkeyKong` drives a `GameState` enum and `LevelManager` to coordinate titles, levels and endings without duplicating logic.
- **Physics & Collision Handling:** Consistent gravity constants, terminal velocities, ladder tolerances and platform placement helpers eliminate jitter and tunnelling.
- **Scoring System:** `ScoreManager` tracks base score vs. time bonus so Level 2 can inherit Level 1 progress fairly.
- **Config-Driven Design:** All entity placements, fonts, HUD coordinates and AI patrol distances live in `res/app.properties`.
- **Tooling:** `generate_git_log.sh` (and the timestamped `.txt` outputs) document development cadence for assessment compliance.

## Supporting Material

- **Project Development Journal** — Open `project-journal-website/index.html` to review the interactive dev log with timelines, translations, theme toggle and animated code snippets.
- **`project_report.md`** — Detailed write-up that can double as appendix material for portfolios or cover letters.
- **Git Histories** — Both project folders include a formatted commit log (`1488802.txt`) generated from the submission repo.

## Portfolio Snapshot

- 30+ Java classes, meticulously documented physics and AI behaviours, zero magic numbers.
- Demonstrated mastery of object-oriented patterns, state machines and real-time collision detection.
- Polished presentation assets (journal website + report) ready for recruiters or MSc applications.


---

# Shadow Donkey Kong（中文版）

> SWEN20003 满分课程项目，以 Java 复刻街机经典《Donkey Kong》，并在第二关加入射击与敌人 AI 等扩展玩法。

## 项目概览

仓库包含两个里程碑：

- **Project 1**：完成核心玩法——跑跳、梯子、桶、锤子、计分、状态机与整套游戏循环。
- **Project 2**：在此基础上升维，加入多关卡框架、智能猴子、投射物（香蕉与子弹）、灵活 UI 与更精细的游戏体验。

所有逻辑基于 Bagel 2D 引擎，实现遵循面向对象设计，并通过外部配置文件驱动参数与关卡数据。

## 核心亮点

- 🎮 两个完整可玩的关卡：第一关靠锤子近战击败大金刚，第二关引入血量系统与远程战斗。
- 🧠 敌人 AI 完整体：普通猴子巡逻，智能猴子定时丢香蕉；玩家可拾取光束枪进行射击。
- 🧰 架构稳固：实体继承体系 + 行为接口（Movable/Collidable/Destroyable/Weapon）+ 通用 `GameplayScreen` 框架。
- 📊 计分体系完善：跳桶/砸桶/击杀得分、时间加成、HUD 显示以及跨关卡分数继承。
- 🌐 `project-journal-website/` 包含中英双语的互动开发日志，附带暗黑/亮色主题与时间线叙事，可直接作为简历素材。

## 操作方式

- `← / →`：左右移动（支持空中微调）。
- `↑ / ↓`：攀爬梯子。
- `SPACE`：跳跃。
- 拾到锤子后自动切换为锤子形态。
- `F`：在第二关、持有光束枪且有弹药时开火。
- `ESC`：退出游戏。
- 标题界面：`ENTER` 进入第一关，`2` 直接跳到第二关。

## 启动指南

```bash
# 依赖：Java 17+、Maven 3.x

# 在任一项目目录运行
./run.sh

# 或手动执行
mvn clean compile dependency:copy-dependencies -DoutputDirectory=target/dependency
java -Djava.library.path=target/dependency -cp target/classes:target/dependency/* ShadowDonkeyKong
```

`run.sh` 会自动复制 Bagel 所需的本地依赖并配置类路径。

## 目录结构

| 路径 | 说明 |
| --- | --- |
| `dazd1-project-1/DonkeyKong_Skeleton/` | 第一阶段提交，涵盖基础玩法实现。 |
| `dazd1-project-1/DonkeyKongsolution/` | 早期参考实现，便于回顾迭代历程。 |
| `dazd1-project-2/DonkeyKong_Skeleton/` | 第二阶段提交，多关卡与 AI 扩展版。 |
| `dazd1-project-2/project-journal-website/` | 开发日志（HTML 形式，支持双语与动画展示）。 |
| `project_report.md` | 600+ 行的设计/物理/AI/测试详解，可直接引用到申请或面试材料。 |
| `1488802.txt` | 评审要求的 Git 提交记录示例文件。 |
| `Archive.zip` | 最终打包交付版本。 |

## 关卡亮点

### 第一关：经典街机体验

- 玩家跑跳、梯子、锤子玩法完整落地。
- 桶具有重力与冷却机制，严格判定跨层跳跃防止误计分。
- 状态机管理首页 / 游戏中 / 结束界面，确保流程顺滑。
- 通过 `GameplayScreen`、`Entity`、`ScoreManager` 等抽象实现整洁代码结构。

### 第二关：战斗元素与 AI

- 大金刚拥有血量，HUD 动态显示血条与子弹剩余，分数跨关卡继承。
- 新增光束枪、子弹、香蕉投射物，共用 `Projectile` 抽象类。
- 普通猴子、智能猴子继承 `Monkey`，具备巡逻路径、自动换向与定时远程攻击。
- 更复杂的碰撞规则：锤子克制猴子，香蕉无视锤子，子弹可削血或击杀敌人。
- UI 坐标、文本、巡逻路径等全部配置化，便于调优。

## 技术要点

- **架构与接口**：实体基类 + 行为接口让平台、角色、武器等实现解耦、易扩展。
- **状态机**：`ShadowDonkeyKong` 管理 `GameState` 枚举与 `LevelManager`，多关卡流程免重复代码。
- **物理与碰撞**：统一的重力、终端速度、梯子对齐容差与平台贴合，避免穿模与抖动。
- **计分系统**：`ScoreManager` 分离基础分与时间奖励，实现公平的关卡结算。
- **配置驱动**：`res/app.properties` 统一管理实体坐标、字体、HUD 坐标与 AI 巡逻数据。
- **工具支持**：`generate_git_log.sh` 符合课程提交规范，帮助自动生成提交记录。

## 展示素材

- **项目开发日志**：打开 `project-journal-website/index.html`，快速浏览项目故事、时间线、代码片段与可视化数据。
- **`project_report.md`**：深入的技术报告，可做附录或求职材料。
- **Git 历史**：两个项目目录均保留格式化的提交日志，体现迭代节奏与质量。

## 简历卖点

- 30+ Java 类覆盖实时物理、AI、UI、状态机等模块，全流程无魔法数字。
- 面向对象与接口驱动设计，展示高可维护性与扩展能力。
- 额外产出高质量文档与展示网站，证明交付与沟通能力。

---


