# 起步

**快速使用**
* 在以构建好的Mirai-Console的应用程序目录下 将 [rulateday-server-1.0-SNAPSHOT-jar-with-dependencies.jar](https://github.com/Eiriksgata/mirai-rulateday-dice/releases/tag/v0.1.0) 放入 plugins 文件夹中即可
* Mirai-Console程序目录下 libs文件夹需要包含的文件以及版本: mirai-console-1.0.0+  & mirai-core-all.1.3.3 & mirai-console-terminal-1.0.0
* 怪物图片库下载:[mm-image](https://github.com/Eiriksgata/rulateday-dnd5e-wiki/tree/master/docs/mm-image)下载怪物的图片后需要将其放在**Mirai-Console**目录下的`data\rulateday\dnd5eMMImage`文件夹里面

参考文件目录结构:(如果没有dnd5eMMImage文件夹则需要自己创建)
```text
└── mirai-console
    ├── config
    ├── data
        └── image
		└── record
		└── rulateday
			└── rulateday.db
			└── dnd5eMMImage
				└── 阿尔法穴居攫怪.png
				└── 阿兰寇拉鹰人.png
				└── .......
    └── plugins
```
