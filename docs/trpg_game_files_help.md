# TRPG模组文件说明

如有不懂的地方建议加群咨询。群号:783679747

## 文件例子速览

[古茂密林之中.json](import/古茂密林之中.json)

## 文件说明

该文件格式为json文件，模组文件需要放置在MCL程序目录下的 `data/indi.eiriksgata.rulateday-dice/trpg-game` 文件夹里面

第一层结构为：

```json
{
  "name": "",
  "describe": "",
  "introduction": "",
  "author": "",
  "version": "",
  "importData": {},
  "config": {},
  "option": {},
  "event": {}
}

```

|参数名|类型|说明|
|----|----|----|
|name|string|模组名称|
|describe|string|模组描述|
|introduction|string|简介|
|author|string|作者名称|
|version|string|版本|
|importData|Object|导入数据，目前仅用于存储角色数据|
|config|Object|配置项，目前用于配置每个选项的选择次数，当选项数值<1时，将不会出现|
|option|Object|选项数据|
|event|Object|事件数据|

Object类型的参数，将会在下面单独详细介绍

## importData

目前该集合内仅有一个role，因此是属于固定格式。

数据格式参考:

```json
{
  "importData": {
    "role": {
      "player": {
        "name": "",
        "describe": "player属于玩家属性，但是在这里填写的数据是不会有任何作用的，建议是通过游戏进程来获取东西。作者可以通过开始的时候送一些物品。",
        "attribute": "",
        "consumables": ""
      },
      "system": {
        "describe": "系统数据",
        "attribute": "动机概率50必然发生100"
      },
      "艾莉莎": {
        "describe": "同行调查员之一",
        "attribute": "力量50体质50体型65敏捷70外貌75智力55意志60教育55幸运75会计60侦查80聆听70",
        "consumables": "生命11魔法6理智60"
      },
      "莫比乌斯": {
        "describe": "同行调查员之一",
        "attribute": "力量50体质50体型65敏捷60外貌65智力80意志60教育55幸运75生命11魔法6理智60神秘学70社会学70",
        "consumables": "生命11魔法6理智60"
      }
    }
  }
}
```

1. player该项在这里仅用作占用块，实际数值要通过 `.trpg-role-set` 指令设置。
2. system 一般用于存储一些概率变量，用于选项中的检测使用。
3. attribute 为属性值，属性一般用于做检测 例如D100 < 属性值，这里属性包含技能,后面option中会详细介绍。
4. consumables 消耗品，在这里的数值，都可以自定义的进行检测 > 或 <  或 = 你给定的数值，后面option中会详细介绍。
5. 当你需要额外增加角色数据时，可以在role集合内 继续添加 数个Map 以达到添加数个角色。

## option

该内容为事件选项，每个事件下方才会携带选项。

```json
{
  "option": {
    "B1001": {
      "text": "随机动机",
      "detection": {
        "role": "system",
        "attribute": "动机概率",
        "success": {
          "text": "来自银行、抵押公司等等的债务使你的财务状况陷入了危机。在绝望之中你选择向高利贷者求助。你必须在这周结束时拿到现金，不然高利贷者就要让你吃点苦头。你只需要1000美金就能脱离苦海……",
          "nextNode": "2",
          "update": {
            "role": "player",
            "attribute": "身负债务=100,信誉=10",
            "consumables": "美元=20"
          }
        },
        "fail": {
          "text": "你和绑架犯西德尼·哈里斯，还有他的党羽，很早就认识了。你们曾经一起长大，但不是朋友。哈里斯曾经是你人生中的灾星。一朝被欺负，十年难抬头……从那以后，你过得还不错，哈里斯则每况愈下。现在胜负已分，是你比他强了。这就是一个向他复仇的机会，以法律的名义……",
          "nextNode": "2",
          "update": {
            "role": "player",
            "attribute": "正义达人=100,信誉=20",
            "consumables": "绷带+2,美元+100"
          }
        }
      }
    },
    "B1002": {
      "text": "当众吵嚷",
      "detection": {
        "role": "system",
        "attribute": "动机概率",
        "success": {
          "text": "虽然你不清楚这样做有什么意义，但是你的行为引起了警长的注意，他瞪了你一眼。并且警告你这里是警局，不要在此捣乱，注意你的言行。",
          "nextNode": "3",
          "update": {
            "consumables": "警长好感度-1"
          }
        },
        "fail": {
          "text": "当你刚想当众吵嚷的时候，旁边有一名热心市民阻止了你的行为，显然他留意到你想捣乱。她小声的跟你说到:[这里是警局，你注意点，小心被警长给抓起来]，你听到她如此说到后停止了自己这般迷惑的行为。",
          "nextNode": "3"
        }
      }
    }
  }
}
```
从上方的例子中可以看出，option 是一个Map `B1001`作为Key 这个编码是作为选项ID，用于在事件下方作绑定，这样可以提高复用率。
