# cloud-security
刚刚开始

运行环境下必须先开起activeMQ  现在还未把AMQ集成进这套系统中

启动顺序  eureka -> permission -> eureka-client -> ribbon

#现阶段完成  -->
	当前pom关联 annotation包的 jar包,  会自动扫描到当前jar包所有的@RequestMapping@PostMapping@GetMapping
	并使用MQ  从提供者 发送到消费者
	
#下一阶段 -->
	把消费者接收到的数据  存放进入数据库,进行action权限的目录管理
	并开发 action权限目录管理 平台.
	对于平台的action,提前将信息由@ConParameter注解进行注册进入数据库
	开发平台用户权限 已提供登录平台.
#最终阶段 -->
	完成一套完整的权限管理 系统.