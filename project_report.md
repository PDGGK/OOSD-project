# Donkey Kong 游戏项目报告

## 1. 项目概述

DonkeyKong_Skeleton 是一个基于 Java 和 Bagel 游戏引擎开发的经典 Donkey Kong 游戏的实现。该游戏复刻了经典街机游戏的核心玩法，玩家控制马里奥角色在平台间移动，躲避桶，爬梯子，并最终到达大金刚处获得胜利。

### 主要功能

- 玩家控制角色（马里奧）在多个平台之间移动和跳跃
- 通过梯子在不同高度的平台之间攀爬
- 跳跃躲避或使用锤子摧毁滚动的桶
- 收集锤子获得打击桶的能力
- 计分系统：根据跳桶次数和使用锤子击碎桶计算分数
- 游戏时间限制和时间奖励
- 多个游戏状态：标题画面、游戏进行中、胜利结束和失败结束

## 2. 项目架构

项目采用面向对象设计，具有清晰的包结构和类层次结构：

### 包结构
- `src/` - 源代码根目录
  - `entities/` - 游戏实体类（包含 Barrel.java, DonkeyKong.java, Entity.java, Hammer.java, Ladder.java, Platform.java, Player.java）
  - `game/` - 游戏管理和状态类（包含 GameState.java, ScoreManager.java）
  - `screens/` - 游戏屏幕类（包含 GameOverScreen.java, GameplayScreen.java, Screen.java, TitleScreen.java）
  - `IOUtils.java` - 工具类，用于读取属性文件
  - `ShadowDonkeyKong.java` - 主游戏类
- `res/` - 资源目录，包含图像、字体和属性文件

### 核心类层次结构

```
AbstractGame (Bagel引擎)
└── ShadowDonkeyKong
```

```
Entity (抽象基类)
├── Player
├── DonkeyKong
├── Platform
├── Ladder
├── Barrel
└── Hammer
```

```
Screen (接口)
├── TitleScreen
├── GameplayScreen
└── GameOverScreen
```

```
GameState (枚举)
```

## 3. 核心类详解

### 3.1 主游戏类

#### ShadowDonkeyKong
主游戏类，继承自Bagel引擎的AbstractGame类，负责初始化游戏世界、管理游戏状态和屏幕切换。

**主要功能：**
- 加载配置文件和资源
- 初始化各个游戏屏幕
- 管理游戏状态转换
- 实现游戏循环逻辑
- 处理基本输入（如ESC键退出）

**关键方法：**
- `update(Input input)` - 实现游戏循环的核心逻辑
- `handleStateTransition(GameState newState)` - 处理状态转换并切换屏幕
- `main(String[] args)` - 程序入口点，创建并启动游戏

#### IOUtils
工具类，提供读取属性文件的功能。

**主要功能：**
- 加载游戏配置文件和消息文件
- 处理文件读取异常

**关键方法：**
- `readPropertiesFile(String path)` - 读取并返回指定路径的属性文件

### 3.2 实体类

#### Entity
所有游戏实体的抽象基类，提供位置、碰撞检测和绘制功能。

**主要属性：**
- `x, y` - 实体的中心坐标
- `image` - 实体的图像

**关键方法：**
- `draw()` - 绘制实体
- `abstract update()` - 更新实体状态（子类必须实现）
- `getBoundingBox()` - 获取实体的碰撞盒
- 各种getter和setter方法

#### Player
代表玩家控制的马里奥角色，继承自Entity，实现了移动、跳跃和与梯子交互的功能。

**主要属性：**
- `facingRight` - 角色朝向
- `onGround, onLadder` - 状态标志
- `verticalVelocity` - 垂直速度
- `hasHammer` - 是否持有锤子
- `currentLadder` - 当前使用的梯子引用
- `previousY` - 上一帧的Y坐标，用于碰撞检测和解析

**关键方法：**
- `update()` - 更新角色状态和位置，应用重力
- `handleInput(Input input, int windowWidth, int windowHeight)` - 处理玩家输入
- `startClimbing(Ladder ladder)` - 开始爬梯子
- `stopClimbing()` - 停止爬梯子
- `collectHammer()` - 收集锤子
- `getPreviousY()` - 获取上一帧的Y坐标，用于平台穿透检测

**物理特性：**
- 重力常数：0.2
- 终端速度：10.0
- 移动速度：3.5像素/帧
- 爬梯子速度：2.0像素/帧
- 跳跃初速度：-5.0（向上为负）

#### Platform
平台实体，提供支撑其他实体的表面。

**主要功能：**
- 检测与其他实体的碰撞，特别是顶部碰撞
- 为实体提供站立平台
- 处理实体与平台的碰撞响应

**关键方法：**
- `isCollidingFromTop(Entity entity)` - 检测实体是否从上方与平台碰撞
- `placeEntityOnTop(Entity entity)` - 将实体放置在平台顶部
- `overlaps(Entity entity)` - 检测实体是否与平台重叠

#### Ladder
梯子实体，允许玩家在不同高度的平台间攀爬。

**主要功能：**
- 为玩家提供攀爬机制
- 检测玩家是否可以开始攀爬或停止攀爬
- 受重力影响，可以放置在平台上

**关键方法：**
- `canPlayerClimb(Player player)` - 检测玩家是否可以攀爬该梯子
- `isPlayerAtTopOfLadder(Player player)` - 检测玩家是否位于梯子顶部
- `isPlayerAtBottomOfLadder(Player player)` - 检测玩家是否位于梯子底部
- `update()` - 更新梯子状态，应用重力

**物理特性：**
- 重力常数：0.2
- 终端速度：5.0

#### Barrel
桶实体，玩家需要躲避或摧毁的障碍物。

**主要属性：**
- `verticalVelocity` - 垂直速度
- `onGround` - 是否位于平台上
- `destroyed` - 是否被摧毁

**关键方法：**
- `update()` - 更新桶状态，应用重力
- `destroy()` - 标记桶为已摧毁
- `draw()` - 重写绘制方法，已摧毁的桶不再显示

**物理特性：**
- 重力常数：0.2
- 终端速度：5.0
- 桶被摧毁时会为玩家提供100分

#### DonkeyKong
大金刚实体，玩家需要到达的目标。

**主要功能：**
- 作为游戏的终点目标
- 检测与平台的碰撞（防止掉落）

**关键方法：**
- `update()` - 更新状态

#### Hammer
锤子实体，玩家可以收集的道具，使用后可以摧毁桶。

**主要属性：**
- `collected` - 是否已被收集

**关键方法：**
- `collect()` - 标记锤子为已收集
- `draw()` - 重写绘制方法，已收集的锤子不再显示

### 3.3 屏幕类

#### Screen (接口)
定义所有游戏屏幕必须实现的方法。

**主要方法：**
- `GameState update(Input input)` - 更新屏幕状态，返回下一个游戏状态
- `void draw()` - 绘制屏幕内容

#### TitleScreen
游戏标题屏幕，显示游戏标题和开始指令。

**主要功能：**
- 显示游戏标题
- 显示游戏指令
- 处理开始游戏的输入

**关键方法：**
- `update(Input input)` - 检测按键开始游戏
- `draw()` - 绘制标题画面

#### GameplayScreen
游戏主屏幕，包含游戏的核心逻辑，管理所有游戏实体和它们之间的交互。

**主要功能：**
- 初始化和维护所有游戏实体
- 处理玩家输入
- 检测各种碰撞（玩家-平台、玩家-梯子、玩家-桶等）
- 管理游戏状态
- 更新和显示分数
- 追踪游戏时间

**关键方法：**
- `update(Input input)` - 更新所有实体并处理玩家输入
- `draw()` - 绘制所有实体和UI元素
- `checkPlatformCollisions()` - 检测所有实体与平台的碰撞
- `checkPlayerLadderInteractions(Input input)` - 处理玩家与梯子的交互
- `checkBarrelJumps()` - 检测玩家跳过桶的动作并计分
- `checkPlayerCollisions()` - 检测玩家与其他实体的碰撞

**内部类：**
- `BarrelJumpInfo` - 追踪每个桶的跳跃状态的内部类
  - `isAboveBarrel` - 玩家是否在桶上方
  - `hasJumpedOver` - 是否已记录跳跃
  - `cooldownTimer` - 重复计分冷却时间
  - `lastPlayerY` - 追踪玩家Y坐标变化
  - `side` - 记录玩家相对于桶的位置（左侧、右侧或无）

**主要属性：**
- `currentFrame` - 当前游戏帧
- `maxFrames` - 游戏最大帧数，用于时间限制
- `JUMP_COOLDOWN` - 对同一个桶的跳跃冷却时间（30帧）

#### GameOverScreen
游戏结束屏幕，显示胜利或失败信息和最终分数。

**主要功能：**
- 显示游戏结果（胜利或失败）
- 显示最终分数
- 处理重新开始游戏的输入

**关键方法：**
- `update(Input input)` - 检测按键重新开始游戏
- `draw()` - 绘制结束画面和分数

### 3.4 游戏管理类

#### GameState
游戏状态枚举，定义游戏的不同状态。

**状态：**
- `TITLE` - 标题画面
- `PLAYING` - 游戏进行中
- `GAME_OVER_WIN` - 玩家胜利
- `GAME_OVER_LOSE` - 玩家失败

#### ScoreManager
分数管理器，处理游戏分数计算和显示。

**主要功能：**
- 记录和更新游戏分数
- 计算剩余时间奖励
- 绘制分数

**关键方法：**
- `addScore(int points)` - 增加分数
- `addTimeBonus(int remainingSeconds)` - 添加时间奖励
- `getScore()` - 获取当前总分（基础分数+时间奖励）
- `draw()` - 绘制分数
- `reset()` - 重置分数系统

**计分规则：**
- 跳过桶：每次30分（`BARREL_JUMP_SCORE`）
- 摧毁桶：每个100分（`BARREL_DESTROY_SCORE`）
- 时间奖励：剩余秒数×3分（`TIME_BONUS_MULTIPLIER`）
- 时间奖励只计算一次

## 4. 游戏核心机制实现

### 4.1 物理系统
游戏实现了简单的物理系统，包括：

- **重力**：应用于玩家、桶和梯子
- **平台碰撞**：确保实体能站在平台上
- **跳跃**：玩家按空格键时给予向上的初速度

### 4.2 跳桶计分系统
GameplayScreen类实现了复杂的跳桶检测机制：

- 使用BarrelJumpInfo内部类追踪每个桶的跳跃状态
- 检测玩家是否从桶的上方跳过
- 防止重复计分（通过冷却时间）
- 为每次成功跳桶增加分数

### 4.3 梯子攀爬系统
梯子攀爬系统通过以下步骤实现：

1. 检测玩家是否接近梯子
2. 根据键盘输入开始/停止攀爬
3. 攀爬时禁用重力，允许上下移动
4. 离开梯子时重新启用重力

### 4.4 锤子收集与使用系统
锤子系统实现：

1. 玩家碰到锤子时，锤子被标记为已收集
2. 玩家状态更新为持有锤子
3. 更换玩家图像为持锤子的图像
4. 持有锤子时碰到桶，桶被摧毁并加分

## 5. 代码亮点

### 5.1 模块化设计
项目采用了良好的模块化设计，将不同功能分散到不同类中：

- 实体类只负责自己的状态和行为
- 屏幕类管理游戏流程和实体间交互
- 游戏状态管理集中在ShadowDonkeyKong类中

### 5.2 面向对象原则应用
项目充分应用了面向对象编程原则：

- **封装**：私有成员变量，公共访问方法
- **继承**：Entity基类派生出各种实体类
- **多态**：Screen接口有多种实现
- **抽象**：使用抽象类和接口定义通用行为

### 5.3 碰撞检测优化
游戏实现了高效的碰撞检测系统：

- 使用边界盒（BoundingBox）进行初步碰撞检测
- 针对特定实体交互（如跳桶）使用精确碰撞检测
- 跟踪上一帧位置以检测穿透碰撞

## 6. 项目配置和资源管理

### 6.1 属性文件
游戏使用外部属性文件管理配置：

- `app.properties` - 游戏基本配置（窗口大小、实体初始位置等）
- `message_en.properties` - 游戏文本消息

### 6.2 资源文件
游戏资源存放在res目录：

- 角色和实体图像
- 字体文件
- 背景图像

## 7. 总结

DonkeyKong_Skeleton项目是一个设计精良的游戏实现，展示了面向对象设计原则在游戏开发中的应用。项目结构清晰，代码组织合理，功能实现完整。

项目的核心亮点包括：
- 清晰的类层次结构和包组织
- 完善的碰撞检测系统
- 灵活的状态管理机制
- 可扩展的设计，便于添加新功能

这个项目不仅实现了基本的游戏功能，还提供了良好的代码结构和设计模式示例，是学习游戏开发和面向对象编程的优秀案例。

## 8. 实现重难点及解决方案

在DonkeyKong_Skeleton项目的开发过程中，团队面临了多个技术挑战和难点。以下是主要的实现难点及其解决方案：

### 8.1 精确的碰撞检测系统

**难点**：游戏中需要处理多种复杂的碰撞情况，包括：玩家与平台、玩家与梯子、玩家与桶和大金刚的碰撞，以及确定玩家是否跳过桶。

**解决方案**：
1. **分层碰撞检测**：将碰撞检测分为多个层次，使用不同的检测方法处理不同类型的碰撞。
2. **边界盒算法**：使用`getBoundingBox()`方法获取实体的碰撞盒，快速进行初步碰撞检测。
3. **细粒度碰撞检测**：对于平台碰撞，实现了`isCollidingFromTop()`方法，只检测从上方发生的碰撞。
4. **历史位置追踪**：记录玩家和实体的上一帧位置，使用`previousY`属性防止"穿透"现象。
5. **方向性碰撞**：为跳桶检测实现了方向性碰撞，追踪玩家相对于桶的位置（左侧、上方或右侧）。

实现代码片段：
```java
// Platform.java中的顶部碰撞检测
public boolean isCollidingFromTop(Entity entity) {
    double entityBottomY = entity.getY() + entity.getHeight()/2;
    double entityCenterX = entity.getX();
    
    double previousBottomY = entityBottomY;
    if (entity instanceof Player) {
        Player player = (Player) entity;
        previousBottomY = player.getPreviousY() + player.getHeight()/2;
    }
    
    boolean isWithinHorizontalBounds = entityCenterX >= this.getX() - this.getWidth()/2 && 
                                       entityCenterX <= this.getX() + this.getWidth()/2;
    double platformTopY = this.getY() - this.getHeight()/2;
    
    return isWithinHorizontalBounds && 
           entityBottomY >= platformTopY && 
           previousBottomY <= platformTopY;
}
```

### 8.2 复杂的跳桶检测系统

**难点**：准确检测玩家是否跳过桶是一个复杂问题，需要考虑多种情况，如何防止重复计分，如何确定有效跳跃等。

**解决方案**：
1. **内部类追踪**：实现了`BarrelJumpInfo`内部类专门追踪每个桶的跳跃状态。
2. **状态变量组合**：使用多个状态变量（`isAboveBarrel`、`hasJumpedOver`、`side`）共同确定跳跃状态。
3. **冷却时间机制**：实现了`cooldownTimer`（30帧）防止短时间内对同一个桶重复计分。
4. **Y坐标差异检测**：通过比较当前Y坐标和`lastPlayerY`确定玩家是否完成了向下的跳跃动作。
5. **侧边检测**：记录玩家相对于桶的侧边位置（从左侧跳到右侧或从右侧跳到左侧），只有完整穿过才计分。

实现逻辑：
```
1. 获取玩家和桶的位置
2. 检查玩家是否在桶的上方
3. 如果玩家从桶上方移动到侧边且Y坐标增加（下落），则记录跳跃方向
4. 如果玩家从一侧完全穿过到另一侧，且之前记录了跳跃开始，则认为完成跳跃
5. 对完成跳跃的情况增加得分并重置跳跃状态
6. 应用冷却时间防止短时间内重复计分
```

### 8.3 梯子攀爬系统的实现

**难点**：梯子系统需要处理玩家攀爬的开始、进行和结束过程，以及与平台、重力系统的交互。

**解决方案**：
1. **状态标记**：使用`onLadder`标记玩家当前是否在攀爬状态。
2. **梯子引用**：维护对当前攀爬梯子的引用（`currentLadder`）。
3. **临界点检测**：实现了`isPlayerAtTopOfLadder()`和`isPlayerAtBottomOfLadder()`方法检测玩家是否到达梯子顶部或底部。
4. **重力控制**：在攀爬状态下禁用重力，允许玩家自由上下移动。
5. **多梯子处理**：实现了对场景中多个梯子的遍历和交互检测。

关键逻辑：
```java
// 检测玩家是否可以开始攀爬
public boolean canPlayerClimb(Player player) {
    double playerCenterX = player.getX();
    double ladderLeftEdge = this.getX() - this.getWidth()/2;
    double ladderRightEdge = this.getX() + this.getWidth()/2;
    
    boolean isWithinHorizontalBounds = playerCenterX >= ladderLeftEdge && 
                                       playerCenterX <= ladderRightEdge;
    
    double ladderTopY = this.getY() - this.getHeight()/2;
    double ladderBottomY = this.getY() + this.getHeight()/2;
    double playerY = player.getY();
    
    boolean isWithinVerticalBounds = playerY >= ladderTopY && 
                                     playerY <= ladderBottomY;
    
    return isWithinHorizontalBounds && isWithinVerticalBounds;
}

// GameplayScreen中的梯子交互检测
private void checkPlayerLadderInteractions(Input input) {
    // 检测是否可以开始攀爬
    if (!player.isOnLadder()) {
        for (Ladder ladder : ladders) {
            if (ladder.canPlayerClimb(player) && 
                (input.isDown(Keys.UP) || input.isDown(Keys.DOWN))) {
                player.startClimbing(ladder);
                break;
            }
        }
    } 
    // 检测是否结束攀爬
    else {
        Ladder currentLadder = player.getCurrentLadder();
        
        // 到达梯子顶部且按UP键，或者底部且按DOWN键
        if ((currentLadder.isPlayerAtTopOfLadder(player) && input.isDown(Keys.UP)) ||
            (currentLadder.isPlayerAtBottomOfLadder(player) && input.isDown(Keys.DOWN))) {
            player.stopClimbing();
        }
    }
}
```

### 8.4 实体状态管理与生命周期

**难点**：管理多个动态实体（如桶）的创建、更新和销毁过程，确保内存高效利用。

**解决方案**：
1. **标记与过滤模式**：使用`destroyed`标志而非直接移除对象，在更新循环中过滤已销毁实体。
2. **临时列表**：使用临时列表`remainingBarrels`收集未销毁的桶，避免在迭代过程中修改集合。
3. **分阶段处理**：实体的更新、碰撞检测和绘制分为不同阶段处理，保持逻辑清晰。
4. **延迟销毁**：在下一帧才真正移除已标记为销毁的实体，避免立即销毁造成的问题。

实现代码片段：
```java
// 更新桶，过滤掉已销毁的
List<Barrel> remainingBarrels = new ArrayList<>();
for (Barrel barrel : barrels) {
    // 只更新和保留未销毁的桶
    if (!barrel.isDestroyed()) {
        barrel.update();
        remainingBarrels.add(barrel);
    }
}
barrels.clear();
barrels.addAll(remainingBarrels);
```

### 8.5 游戏状态管理与屏幕切换

**难点**：游戏需要在不同状态间平滑切换（标题、游戏中、结束），并保持状态间的数据传递。

**解决方案**：
1. **状态枚举**：使用`GameState`枚举明确定义游戏的所有可能状态。
2. **屏幕接口**：定义`Screen`接口统一所有屏幕的行为，简化切换逻辑。
3. **状态转换函数**：实现`handleStateTransition()`方法专门处理状态转换。
4. **数据传递**：在状态转换时传递关键数据（如分数）到新的屏幕。
5. **状态返回模式**：屏幕的`update()`方法返回下一个状态，使状态转换分离于状态逻辑。

关键逻辑：
```java
// ShadowDonkeyKong类中的状态转换处理
private void handleStateTransition(GameState newState) {
    currentState = newState;
    
    switch (currentState) {
        case TITLE:
            activeScreen = titleScreen;
            break;
        case PLAYING:
            // 创建新的游戏屏幕实例以重置游戏状态
            gameplayScreen = new GameplayScreen(GAME_PROPS, MESSAGE_PROPS);
            activeScreen = gameplayScreen;
            break;
        case GAME_OVER_WIN:
        case GAME_OVER_LOSE:
            // 创建游戏结束屏幕，传递最终分数
            int finalScore = gameplayScreen.getScore();
            activeScreen = new GameOverScreen(GAME_PROPS, MESSAGE_PROPS, currentState, finalScore);
            break;
    }
}
```

### 8.6 物理系统的精确实现

**难点**：实现一个可靠的物理系统，包括重力作用、终端速度限制和平台反弹。

**解决方案**：
1. **重力常量**：为不同实体类型定义了适当的重力常量（如Player为0.2，Barrel为0.2，Ladder为0.2）。
2. **终端速度限制**：实现了最大下落速度限制（如Player为10.0，Barrel为5.0），防止速度无限增长。
3. **条件应用**：只在特定条件下（不在地面且不在梯子上）应用重力，确保物理行为与游戏状态一致。
4. **速度重置**：在着地时重置垂直速度，防止累积效应。
5. **地面检测**：使用`onGround`标记确保地面状态正确传播到物理系统。

实现示例：
```java
// Player类的物理更新
@Override
public void update() {
    // 保存上一帧位置用于碰撞检测
    previousY = y;
    
    // 只有不在地面且不在梯子上时应用重力
    if (!onGround && !onLadder) {
        // 应用重力但限制最大下落速度
        verticalVelocity = Math.min(verticalVelocity + GRAVITY, TERMINAL_VELOCITY);
        y += verticalVelocity;
    } else if (onGround) {
        // 在地面上重置垂直速度
        verticalVelocity = 0;
    }
    // 在梯子上时垂直移动由输入控制
}
```

### 8.7 配置文件管理与资源加载

**难点**：游戏需要从外部配置文件加载大量设置和资源，确保模块化和可配置性。

**解决方案**：
1. **Properties API**：使用Java的Properties类加载和解析配置文件。
2. **专用工具类**：实现`IOUtils`类处理文件IO操作，集中异常处理。
3. **两级配置**：分离游戏配置（`app.properties`）和文本信息（`message_en.properties`）。
4. **严格类型转换**：使用恰当的类型转换（如parseInt、parseDouble）处理数值配置。
5. **默认值与错误处理**：为关键配置提供默认值，优雅处理配置错误。

实现样例：
```java
// IOUtils类中的配置文件加载
public static Properties readPropertiesFile(String filePath) {
    Properties props = new Properties();
    try (InputStream input = new FileInputStream(filePath)) {
        props.load(input);
    } catch (IOException e) {
        System.err.println("Error loading properties file: " + filePath);
        e.printStackTrace();
    }
    return props;
}

// 使用配置
int windowWidth = Integer.parseInt(gameProps.getProperty("window.width"));
double playerStartX = Double.parseDouble(gameProps.getProperty("mario.start.x"));
```

这些解决方案展示了项目在处理复杂游戏开发挑战时采用的技术策略和设计思路，体现了团队在面向对象设计和编程方面的专业能力。 