# 插件系统配置文件说明

## 文件位置

文件所在路径为当前程序运行目录下的 `config/indi.eiriksgata.rulateday-dice/config.json`

## 详细配置项说明

| 配置项                        | 类型            | 说明              | 默认值                                     |
|----------------------------|---------------|-----------------|-----------------------------------------|
| file.version               | string        | 当前配置文件版本        | 1.0.3                                   |
| master.QQ.number           | string        | 骰主QQ            |                                         |
| reply.at.user              | boolean       | 回复消息时是否使用@用户的形式 | false                                   |
| auto.accept.friend.request | boolean       | 自动接受好友申请        | true                                    |
| auto.accept.group.request  | boolean       | 自动接受群邀请         | true                                    |   
| ai-drawing                 | {}            | ai绘图配置项         | {"url": "","remote": true,"userId": ""} |
| instructions.prefix.list   | List\<String> | 指令前缀配置          | [".","。"]                               |


后续有待添加....