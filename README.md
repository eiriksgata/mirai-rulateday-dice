## Mirai-rulateday-dice

本项目是Mirai-Console的插件项目。

`Mirai-rulateday-dice`插件是实现TRPG骰子系统。骰子核心逻辑处理方法项目请移步至另一个项目 `trpg-java-dice`.

该项目主要作为集成`Mirai-Console`的调用方法框架模板，用于控制台加载其程序的主要实现，并打包生成jar插件。



**拓展**

同时该模块还可以负责插件载入块处理，DTO、作为Server 等处理。
因为SpringBoot运行消耗过量的内存，因此这里暂时不使用。
