> Redis 的消息队列不是专业的消息队列，它没有非常多的高级特性，
   没有 ack 保证，如果对消息的可靠性有着极致的追求，那么它就不适合使用。
- 阻塞读
> 可是如果队列空了，客户端就会陷入 pop 的死循环，不停地 pop，没有数据，接着再 pop， 又没有数据。这就是浪费生命的空轮询。空轮询不但拉高了客户端的 CPU，redis 的 QPS 也 会被拉高，如果这样空轮询的客户端有几十来个，Redis 的慢查询可能会显著增多。

阻塞读在队列没有数据的时候，会立即进入休眠状态，一旦数据到来，则立刻醒过来。消
息的延迟几乎为零。用 blpop/brpop 替代前面的 lpop/rpop，就完美解决了上面的问题