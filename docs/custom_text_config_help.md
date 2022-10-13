# 自定义返回文本配置说明

## 文件位置

文件所在路径为当前程序运行目录下的 `config/indi.eiriksgata.rulateday-dice/custom-text.json`

## 随机回复说明

要想随机回复文本，仅需要使用数组的形式，添加多个回复文本，即可。
例如修改人品检测随机回复文本：

```json
{
  "dice.jrrp.success": [
    "你的今日人品为:{0}",
    "人品为:{0}",
    "您的人品为:{0}"
  ]
}
```

如上进行配置后，那么则随机从这3个中抽取一个进行回复。

## 配置文件一览

```json
{
  "cards.type.list.title": "当前存储库下有以下卡组类型:",
  "query.doc.lib.result.list.tail": "\n最多显示30条数据。如果需要查询更多信息，请输入更精准的关键字",
  "dice.pool.parameter.format.error": "参数格式不正确",
  "cards.draw.clear": "已清空当前牌堆所有数据。",
  "coc7.rule.not.found": "找不到COC7规则书内容",
  "instructions.all.result1": "骰子常用指令列表:\n.st 属性设置\n.ra|.rc 属性检测\n.rb 奖励骰|.rp惩罚骰\n.cr coc7规则书查询\n.dr dnd5e信息查询\n.ti 随机获取发疯情况\n.li 发疯后总结\n.sc 理智检测\n.rh 暗骰\n.set 设置默认骰\n.coc 随机coc7角色属性\n.dnd 随机dnd5e角色属性\n.r 随机数生成\n.rd 默认骰数值生成\n.ww 骰池功能\n.kkp 随机图片速览(测试)\n.atk 先攻指令系列，详情请看文档\n.botoff | .boton 启用骰子开关\n更多的指令详情请查看:https://eiriksgata.github.io/mirai-rulateday-dice/#/instruction\n备用文档:https://note.youdao.com/s/PZ3tqThT",
  "dice.sa.not.set.attribute": "你尚未设置属性，可以通过指令.st来进行设置",
  "text.ex-success": "极难成功",
  "cards.draw.not.found": "查询不到卡组名称，请使用.cards查看所有牌组。",
  "coc7.roll1": "{0}={1}",
  "cards.draw.add.success": "已将卡组[{0}],添加至当前牌堆。",
  "initiative.null": "目前先攻池内没有任何数据记录",
  "dice.sa.parameter.null": "你输入的指令参数中没有需要更改的数值",
  "coc7.attribute.ex-success": "{3}属性检测[{0}]:{1}/{2} 极难成功!",
  "dice.bot.off": "已关闭服务",
  "text.success": "成功",
  "cards.add.error": "增加卡组类型失败，可能卡组中已存在该卡组名。",
  "reply.at.user": false,
  "text.dif-success": "困难成功",
  "coc7.roll.hide": "进行了一次暗骰",
  "names.create.size.max": "参数范围在1-20内",
  "text.big-success": "大成功",
  "dr5e.rule.lib.result.list.tail": "\n最多显示20条数据。如果需要查询更多信息，请前往网站查询：https://eiriksgata.github.io/rulateday-dnd5e-wiki/#/",
  "file.version": "1.0.3",
  "dr5e.rule.lib.result.list.title": "查询结果存在多个，请在3分钟以内回复清单的数字来查阅内容:",
  "dice.set.attribute.success": "设置属性成功!",
  "dice.en.not.found.attribute": "你通过.st设置的属性中，不存在[{0}]这个技能",
  "api.request.error": "请求云端服务器接口失败。请联系相关开发人员QQ2353686862。",
  "initiative.show": "先攻顺序为:{0}",
  "dice.bot.on": "开启服务",
  "cards.draw.list": "卡池列表:",
  "text.big-fail": "大失败",
  "query.doc.lib.result.list.title": "查询结果存在多个，请在3分钟以内回复清单的数字来查阅内容:",
  "dice.sa.parameter.error": "你输入的指令参数中没有需要更改的数值",
  "cards.draw.hide.success": "抽出结果已私发。",
  "dice.set.attribute.error": "设置属性失败，参数不符合要求!",
  "cards.draw.success": "抽取结果：{0}",
  "dice.en.fail": "D100={0}/{1} [{2}] 成长失败!",
  "coc7.attribute.success": "{3}属性检测[{0}]:{1}/{2} 成功!",
  "dr5e.rule.not.parameter": "请输入关键字参数",
  "dice.base.parameter.error": "指令中的参数不正确。",
  "dice.sc.not-found.error": "找不到san属性",
  "coc7.sc.fail": "San check:{0}={1}/{2} 失败! 减少{3} 剩余 {4}",
  "instructions.help.result1": "插件名称:Rulateda v0.4.x by Eiriksgata\n反馈联系Github：https://github.com/Eiriksgata/mirai-rulateday-dice\n所有指令：.help指令\nDND5eWiki:https://keith404.gitee.io/rulateday-dnd5e-wiki/#/\n",
  "coc7.punishment.easy": "掷骰:P{0}={1} 惩罚骰:{2}={3}",
  "coc7.attribute.dif-success": "{3}属性检测[{0}]:{1}/{2} 困难成功!",
  "initiative.list.size.max": "先攻池骰子数量不能超过30个",
  "coc7.rule.multiple.query.result": "COC7查询结果有多个，请输入更为精准的关键字",
  "master.QQ.number": "",
  "dice.pool.parameter.range.error": "参数必须要 大于0 & 小于300",
  "coc7.attribute.big-success": "{3}属性检测[{0}]:{1}/{2} 大成功!",
  "coc7.sc.big-fail": "San check:{0}={1}/{2}大失败! 减少{3} 剩余 {4}",
  "auto.accept.friend.request": true,
  "dice.set.face.error": "骰子面数设置的参数不正确。",
  "dice.attribute.error": "指令格式错误。.st指令中不包含该属性，或者没有给定属性",
  "cards.add.parameter.format.error": "参数类型不正确，正确格式应该是[卡组名称 卡组数据]例如:麻将 白板,红中,发字,东风,南风",
  "initiative.delete.oneself": "已删除你的先攻骰",
  "dr5e.rule.lib.result.list.not.found": "查询不到结果，欢迎联系QQ:2353686862提供更多的数据",
  "cards.draw.hide.group.result": "在QQ群[{0}]的抽出结果为：{1}",
  "coc7.attribute.fail": "{3}属性检测[{0}]:{1}/{2} 失败!",
  "dice.jrrp.success": [
    "你的今日人品为:{0}"
  ],
  "dice.en.parameter.format.error": "请输入正确的参数形式",
  "cards.draw.not.data": "当前所在群的牌堆中无数据，请使用指令.drawAdd进行添加。",
  "text.fail": "失败",
  "dice.en.not.set.attribute": "你尚未设置你的个人属性可以通过.st进行设置",
  "coc7.bonus.easy": "掷骰:B{0}={1} 奖励骰:{2}={3}",
  "query.doc.lib.result.list.not.found": "自定义文档没找到相关内容.",
  "custom-text.version": "1.0.3",
  "dice.sc.instruct.error": "sc指令参数不正确",
  "coc7.role.create.size.max": "参数范围需要在1-20内",
  "dice.set.face.success": "设置默认骰子面数为:{0}",
  "dice.sa.update.success": "您的属性已更新:{0}=>{1}{2}",
  "coc7.sc.success": "San check:{0}={1}/{2} 成功! 减少{3} 剩余 {4}",
  "coc7.bonus.error": "参数数据错误,没有给定属性值。请直接使用.rp或.rb",
  "cards.draw.hide.friend.result": "在QQ[{0}]的抽出结果为：{1}",
  "initiative.delete.other": "已删除{0}的先攻骰",
  "coc7.bonus": "掷骰{0}:B{1}={2} 奖励骰:{3}={4}/{5}{6}",
  "cards.draw.not.parameter": "请输入添加的牌组名称,可以使用.cards查看。",
  "initiative.result.title": "{0}的先攻骰为:",
  "initiative.parameter.format.error": "先攻数值生成不符合要求，请符合整数型",
  "coc7.attribute.big-fail": "{3}属性检测[{0}]:{1}/{2} 大失败!",
  "auto.accept.group.request": true,
  "dice.pool.result": "{0}={1}",
  "cards.type.not.found": "当前存储库没有卡组类型,可以通过指令.cardsAdd进行添加。",
  "coc7.punishment": "掷骰{0}:P{1}={2} 惩罚骰:{3}={4}/{5}{6}",
  "cards.delete.success": "已删除该卡组。",
  "dice.en.success": "D100={0}/{1} [{2}] 成长成功！你当前的[{3}]为D10={4}+{5}={6}",
  "dr5e.role.create.size.max": "参数范围需要在1-20内",
  "dice.sa.not.found.attribute": "你的属性中，不存在该属性，请通过.st重新设置",
  "cards.add.success": "增加成功。",
  "initiative.clear": "已清空当前的先攻池",
  "dice.en.parameter.null": "请输入属性名和数值或者属性名"
}

```


## 配置项说明

暂无