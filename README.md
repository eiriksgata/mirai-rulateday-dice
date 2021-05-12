## Mirai-rulateday-dice ##

**本项目是Mirai-Console的插件项目**
* `Mirai-rulateday-dice`插件是实现TRPG骰子系统。
* 骰子核心逻辑处理方法项目请移步至另一个项目 `trpg-java-dice`.该项目主要作为集成`Mirai-Console`的调用方法框架模板，用于控制台加载其程序的主要实现，并打包生成jar插件。
* 同时该项目也负责了主要的查询功能实现：DND5e法术详细、COC7技能规则、抽取疯狂症状等。
* 此模板集合了大部分的 `mirai-console` 插件所需要的配置项，开发者也可以直接使用该模板进行开发。
* 如果你是一名纯粹的 Java+Maven 开发者 该项目是一个很好的学习模板。
* 本项目中的一些查询数据文档: **[Rulateday-Dnd5e Wiki](https://keith404.gitee.io/rulateday-dnd5e-wiki/#/)**
* 如果你有什么疑问或者讨论方案，可以选择在在[Discussions](https://github.com/Eiriksgata/mirai-rulateday-dice/discussions)进行
* 本项目文档地址:[Mirai-rulateday-dice Document](https://keith404.gitee.io/mirai-rulateday-dice/#/)
* 如果想直接使用QQ机器人请添加QQ:`209135855`

**快速使用**
* 推荐使用MCL(mirai-console-loader)项目来自动下载最新Mirai运行所需文件。[releases](https://github.com/iTXTech/mirai-console-loader/releases),下载解压完毕后运行 **mcl.cmd**
* 在以构建好的Mirai-Console的应用程序目录下将最新版[rulateday-server-SNAPSHOT-jar-with-dependencies.jar](https://github.com/Eiriksgata/mirai-rulateday-dice/releases) 放入 plugins 文件夹中即可
* 怪物图片库下载:[mm-image](https://github.com/Eiriksgata/rulateday-dnd5e-wiki/tree/master/docs/mm-image)下载怪物的图片后需要将其放在**Mirai-Console**目录下的`data\rulateday\dnd5eMMImage`文件夹里面。
> 如果你没有下载也没有关系，图片会从网络服务器中拉取到本地，然后再由本地发送出去（网络不好的状况需要较长的时间处理）。

参考文件目录结构:
```text
└── mirai-console
    ├── config
    ├── data
        └── image
		└── record
		└── rulateday
			└── rulateday.db (该文件为数据库文件)
			└── dnd5eMMImage
				└── 阿尔法穴居攫怪.png
				└── 阿兰寇拉鹰人.png
				└── .......
    └── plugins
```

**依赖项目**
* [Mirai](https://github.com/mamoe/mirai)
* [Mirai-Console](https://github.com/mamoe/mirai-console)
* [trpg-java-dice](https://github.com/Eiriksgata/trpg-java-dice)


**主要开发技术**
* Java 8+ or OpenJDK 11
* Maven 
* SQLite + Mybatis


**已经实现的指令**
* [基础骰子类型](https://github.com/Eiriksgata/mirai-rulateday-dice/blob/master/rulateday-server/src/main/java/indi/eiriksgata/rulateday/instruction/RollController.java)
* [查询功能类型](https://github.com/Eiriksgata/mirai-rulateday-dice/blob/master/rulateday-server/src/main/java/indi/eiriksgata/rulateday/instruction/QueryController.java)
* 开关骰子指令：.boton | .botoff （可以@机器人.bot on 指定该机器人启动或者关闭）
* [More](https://github.com/Eiriksgata/mirai-rulateday-dice/tree/master/rulateday-server/src/main/java/indi/eiriksgata/rulateday/instruction)
* [详细指令集介绍](https://keith404.gitee.io/mirai-rulateday-dice/#/instruction)

**代码结构简易说明**
* 项目 `trpg-java-dice` 提供了指令注解反射处理。因此在当前项目中只需要实现指令的类方法使用即可。


**文件说明**
* `ruleteday.db` 为 SQLite的数据库文件，其中主要用于保存用户的骰子属性数据以及规则书和疯狂状态的描述内容。当运行时需要放在运行的当前目录下，日后会进行文件相应位置更改，或者主动生成本地文件。避免用户出现使用问题。

**构建说明**
* 暂无

**拓展**

同时该模块还可以负责插件载入块处理，DTO、作为Server 等处理。因为SpringBoot运行消耗过量的内存，因此这里暂时不使用。

现以加入 `Mybatis + SQLite` 项目依赖项，作为数据存储的持久层使用。

**快速导航**

QQ群:783679747


**为什么叫Rulateday?**
其实这是一个英语谐音
 R   U  late day
Are you late day 

