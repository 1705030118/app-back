# Redis Cluster
- 原理
- 容错机制
# 原理

Redis可以使用主从同步，从从同步。第一次同步时，主节点做一次bgsave，并同时将后续修改操作记录到内存buffer，待完成后将rdb文件全量同步到复制节点，复制节点接受完成后将rdb镜像加载到内存。加载完成后，再通知主节点将期间修改的操作记录同步到复制节点进行重放就完成了同步过程。
- 槽位定位算法

Redis Cluster将所有数据划分为16384个slots，每个节点负责其中一部分槽位。Cluster默认会对key值使用crc16算法进行hash得到一个整数值，然后用这个证书值对16384进行取模来得到具体槽位。

# 容错

Redis Cluster可以为每个主节点设置若干个从节点，单主节点故障时，集群会自动将其中某个从节点提升为主节点。如果某个主节点没有从节点，那么当它发生故障时，集群将完全处于不可用状态。不过Redis可以设置成允许部分节点故障。
# 可能下线与确定下线

Redis集群节点采用 Gossip协议来广播自己的状态以及自己对整个集群认知的改变。比
如一个节点发现某个节点失联了 (PFail)，它会将这条信息向整个集群广播，其它节点也就可
以收到这点失联信息。如果一个节点收到了某个节点失联的数量 (PFailCount)已经达到了集
群的大多数，就可以标记该节点为确定下线状态 (Fail)，然后向整个集群广播，强迫其它节
点也接收该节点已经下线的事实，并立即对该失联节点进行主从切换。
# Redis Cluster的优势和不足

- 优势

1. 无中心架构。

2. 数据按照slot存储分布在多个节点，节点间数据共享，可动态调整数据分布。

3. 可扩展性，可线性扩展到1000个节点，节点可动态添加或删除。

4. 高可用性，部分节点不可用时，集群仍可用。通过增加Slave做standby数据副本，能够实现故障自动failover，节点之间通过gossip协议交换状态信息，用投票机制完成Slave到Master的角色提升。

5. 降低运维成本，提高系统的扩展性和可用性。

- 不足

1. Client实现复杂，驱动要求实现Smart Client，缓存slots mapping信息并及时更新，提高了开发难度，客户端的不成熟影响业务的稳定性。目前仅JedisCluster相对成熟，异常处理部分还不完善，比如常见的“max redirect exception”。

2. 节点会因为某些原因发生阻塞（阻塞时间大于clutser-node-timeout），被判断下线，这种failover是没有必要的。

3. 数据通过异步复制,不保证数据的强一致性。

4. 多个业务使用同一套集群时，无法根据统计区分冷热数据，资源隔离性较差，容易出现相互影响的情况。

5. Slave在集群中充当“冷备”，不能缓解读压力，当然可以通过SDK的合理设计来提高Slave资源的利用率。
- 实现

redis5.0集群创建方式改为了C编写的redis-cli创建，不用再安装麻烦的ruby了。