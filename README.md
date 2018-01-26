#资源整合平台
* api-gateway 网关控制中心
* commons.api 各个服务的接口
- commons *项目依赖管理存在问题*
* eureka 服务注册中心
* config-repo 配置存放地址
* documents 项目文档
* script 项目可执行脚本
* …………

#需求描述
资源整合平台


#开发规范
+ 日期处理
>java中的日期处理一直是个问题，没有很好的方式去处理，所以才有第三方框架的位置比如joda。
 用1.8可以不用joda,所以本项目使用joda-time 
     
+ lombok
>lombok是一个可以通过简单的注解的形式来帮助我们简化消除一些必须有但显得很臃肿的 Java 代码的工具
> [eclepse安装]: https://projectlombok.org/setup/eclipse 

##开发工具
如果你想成为一个优秀的java程序员，请更换intellij idea. 使用idea的好处，请搜索谷歌。

###domain包名
根据很多java程序员的”经验”来看，一个数据库表则对应着一个domain对象，所以很多程序员在写代码时，包名则使用：com.xxx.domain ，
这样写好像已经成为了行业的一种约束，数据库映射对象就应该是domain。但是你错了，domain是一个领域对象，往往我们再做传统java软件web开发中，
这些domain都是贫血模型，是没有行为的，或是没有足够的领域模型的行为的，所以，以这个理论来讲，这些domain都应该是一个普通的entity对象，
并非领域对象，所以请把包名改为:com.xxx.entity。

###DTO
数据传输我们应该使用DTO对象作为传输对象，这是我们所约定的，因为很长时间我一直都在做移动端api设计的工作，有很多人告诉我，
他们认为只有给手机端传输数据的时候(input or output)，这些对象成为DTO对象。请注意！这种理解是错误的，只要是用于网络传输的对象，
我们都认为他们可以当做是DTO对象，比如电商平台中，用户进行下单，下单后的数据，订单会发到OMS 或者 ERP系统，这些对接的返回值以及入参也叫DTO对象。

我们约定某对象如果是DTO对象，就将名称改为XXDTO,比如订单下发OMS：OMSOrderInputDTO。

###DTO转化

正如我们所知，DTO为系统与外界交互的模型对象，那么肯定会有一个步骤是将DTO对象转化为BO对象或者是普通的entity对象，让service层去处理。
网上有很多工具，支持浅拷贝或深拷贝的Utils. 

###bean的验证
hibernate提供的jsr 303实现，我觉得目前仍然是很优秀的，具体如何使用，我不想讲，因为谷歌上你可以搜索出很多答案!


