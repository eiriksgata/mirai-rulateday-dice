## Mirai-rulateday-dice ##

本项目是Mirai-Console的插件项目。

`Mirai-rulateday-dice`插件是实现TRPG骰子系统。
骰子核心逻辑处理方法项目请移步至另一个项目 `trpg-java-dice`.该项目主要作为集成`Mirai-Console`的调用方法框架模板，用于控制台加载其程序的主要实现，并打包生成jar插件。

此模板集合了大部分的 `mirai-console` 插件所需要的配置项，开发者也可以直接使用该模板进行开发。

如果你是一名纯粹的 Java+Maven 开发者 该项目是一个很好的学习模板。


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
* 项目 `trpg-java-dice` 提供了指令注解反射处理。因此在当前项目中只需要实现指令的类方法使用即可。


**文件说明**
* `ruleteday.db` 为 SQLite的数据库文件，其中主要用于保存用户的骰子属性数据以及规则书和疯狂状态的描述内容。当运行时需要放在运行的当前目录下，日后会进行文件相应位置更改，或者主动生成本地文件。避免用户出现使用问题。
* `mirai-ruleteday.log` 日志文件


**使用说明**
* 该项目是建立在Mirai-Console的框架下运行的插件，因此需要先了解构造出一个 Mirai-Console 的运行程序。如果不懂如何使用的，可以和我联系。
* 该项目是由Java编写，因此可以通过相应的IDE生成jar文件，然后放入到 Mirai-Console的插件目录下即可。
* 如果需要自定义返回文本，则可以通过7z等解压程序打开 jar文件 然后将其里面的 custom-text.properties 文件复制出来进行修改，然后添加到jar里面进行覆盖。
* 此外其他的配置文件也是如此修改(后续可能会考虑特殊的情况再进行设置项修改)
* `ruleteday.db`文件需要放置在 Mirai-Console 的当前程序目录下 (日后还会更改成单独的插件文件夹使用)

**编译好的jar文件**

* 暂无(当前项目处于快速迭代阶段，因此暂时不发布，请等待v1.0.0版本发布)

**构建说明**

* 暂无


**拓展**

同时该模块还可以负责插件载入块处理，DTO、作为Server 等处理。因为SpringBoot运行消耗过量的内存，因此这里暂时不使用。

现以加入 `Mybatis + SQLite` 项目依赖项，作为数据存储的持久层使用。

**快速导航**
## [trpg-java-dice](https://github.com/Eiriksgata/trpg-java-dice) ##


