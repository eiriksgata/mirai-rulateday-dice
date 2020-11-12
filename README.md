## Mirai-rulateday-dice ##

本项目是Mirai-Console的插件项目。

`Mirai-rulateday-dice`插件是实现TRPG骰子系统。
骰子核心逻辑处理方法项目请移步至另一个项目 `trpg-java-dice`.该项目主要作为集成`Mirai-Console`的调用方法框架模板，用于控制台加载其程序的主要实现，并打包生成jar插件。

此模板集合了大部分的 `mirai-console` 插件所需要的配置项，开发者也可以直接使用该模板进行开发。

如果你是一名纯粹的Java+Maven开发者 该项目是一个很好的学习模板。


**依赖项目体系**
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
* [More](https://github.com/Eiriksgata/mirai-rulateday-dice/tree/master/rulateday-server/src/main/java/indi/eiriksgata/rulateday/instruction)


**代码结构简易说明**
项目 `trpg-java-dice` 提供了指令注解反射处理。
因此在当前项目中只需要实现指令的类方法使用即可。


**文件说明**
* `ruleteday.db` 为 SQLite的数据库文件，其中主要用于保存用户的骰子属性数据以及规则书和疯狂状态的描述内容。
* `mirai-ruleteday.log` 日志文件


**拓展**

同时该模块还可以负责插件载入块处理，DTO、作为Server 等处理。因为SpringBoot运行消耗过量的内存，因此这里暂时不使用。

现以加入 `Mybatis + SQLite` 项目依赖项，作为数据存储的持久层使用。