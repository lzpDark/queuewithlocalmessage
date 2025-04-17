
一个spring-boot的例子，用【本地消息表方案】解决数据库和消息队列的事务一致问题：

1. 发布消息的时候，首先本地事务里同时提交业务数据和【本地消息记录】

2. 再异步发送消息，收到broker的confirm后删除对应的本地消息记录

3. TODO：应该还要有一个定时任务服务，扫描【本地消息表】，发消息到消息队列，成功后【删本地消息记录】

#### 依赖：
- RabbitMQ消息队列
- MySQL数据库
- spring-amqp



#### 参考：
- [rabbitmq docs](https://www.rabbitmq.com/docs)
- [qmq本地消息表实现事务一致性](https://github.com/qunarcorp/qmq/blob/master/docs/cn/transaction.md)