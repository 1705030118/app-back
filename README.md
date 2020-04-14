# Feed流
## 特点
- 读写严重不平衡，读多写少，一般读写比例都在10；1，甚至100：1之上。
## 发布Feed流程
当你发布一条Feed消息的时候，流程是这样的：
1. Feed消息先进入一个队列服务。
2. 先从关注列表中读取到自己的粉丝列表，以及判断自己是否是大V。
3. 将自己的Feed消息写入个人页Timeline（发件箱）。如果是大V，写入流程到此就结束了。
4. 如果是普通用户，还需要将自己的Feed消息写给自己的粉丝，如果有100个粉丝，那么就要写给100个用户，包括Feed内容和Feed ID。
5. 第三步和第四步可以合并在一起，使用BatchWriteRow接口一次性将多行数据写入TableStore。
6. 发布Feed的流程到此结束。

## 读取Feed流流程
当刷新自己的Feed流的时候，流程是这样的：
1. 先去读取自己关注的大V列表
2. 去读取自己的收件箱，只需要一个GetRange读取一个范围即可，范围起始位置是上次读取到的最新Feed的ID，结束位置可以使当前时间，也可以是MAX，建议是MAX值。由于之前使用了主键自增功能，所以这里可以使用GetRange读取。
3. 如果有关注的大V，则再次并发读取每一个大V的发件箱，如果关注了10个大V，那么则需要10次访问。
4. 合并2和3步的结果，然后按时间排序，返回给用户。

至此，使用推拉结合方式的发布，读取Feed流的流程都结束了。
# Hacker News算法
r=(P – 1) / (t + 2)^1.8
- P:投票数,-1是把自己投的过滤掉
- T:发布到现在的时间间隔,单位小时,+2防止除数太小
- G:重力加速度,它的数值大小决定了排名随时间下降的速度快慢

时效性强
