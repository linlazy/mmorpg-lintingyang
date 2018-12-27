# mmorpg-lintingyang

# 战斗系统
1.每隔一段时间（全局配置）回复1点MP
## 攻击对象
攻击单体玩家  
## 示例
- 玩家actorId=7平A玩家targetId=8
{"module":"fight","command":"attack","actorId":7,"targetId": 8}
- 玩家actorId=7使用技能skillId=1攻击玩家targetId=8
{"module":"fight","command":"attack","actorId":7,"skillId": 1,"targetId": 8}
攻击单体怪物

## 技能攻击
1.每次使用技能消耗MP点数（技能配置）
2.每次使用技能冷却一段时间（技能配置）
## 伤害计算
伤害计算 = 自身攻击力-对方防御力（至少1点伤害）
自身攻击力 = 等级x20+装备攻击力+技能攻击力
防御力 = 等级x19+装备防御力
