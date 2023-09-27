# 本地启动说明
```text
可选择如下参数启动
添加启动参数: 
VM options: 

-javaagent:D:\project\fire-digital-platform\lib\aspectjweaver-1.8.13.jar
-javaagent:D:\project\fire-digital-platform\lib\trace-agent.jar
```
# 如何打包
```text
mvn clean install -Dmaven.test.skip=true
```
# linxu启动说明
```text
nohup.out 文件需要定期清理
nohup java -javaagent:aspectjweaver-1.8.13.jar -javaagent:trace-agent.jar -jar fire-digital-platform.jar &
```
# 领域模型说明
```text
整个项目往大的方面说就是只有三种模块
    1.对外暴露能力模块(提供接口、web接口等，提供给第三方调用)
    2.自身处理业务模块、提供基础能力模块(自己处理内部业务，处理外部请求处理的业务逻辑)
    3.调用外部能力模块(调用第三方功能，不限于第三方应用接口、Redis和MySQL等，都属于第三方提供的能力)

所以项目基本分层模块如下：
    application: 对外暴露接口能力
    domain: 自己领域内的功能，业务处理
    infrastructure: 外部，外部功能使用，外部存储服务
```
```text
细分如下: 
Application:主要是多进程管理及调度，多线程管理及调度，多协程调度和状态机管理，等等。
    api:提供给外部调用暴露的服务
        request:入参实体 (DTO结尾表示为应用交互间传输的对象,VO结尾表示与前端交互的对象)
        response:出参实体(DTO结尾表示为应用交互间传输的对象,VO结尾表示与前端交互的对象)
    service:没有任何复杂的逻辑，用于编排协调服务
            (1.例如调用外部第三方服务不需要任何业务逻辑，可以直接到[infrastructure.external]找到对应的client。
             2.例如调用本项目的domain-service，部分入参需要做转换，我们就在此层做转换，逻辑层专注于做逻辑)
domain:主要是领域模型的实现，包括领域对象的确立，这些对象的生命周期管理及关系，领域服务的定义，领域事件的发布，等等。
    service:应用复杂的业务逻辑主要在此层实现(专注于做逻辑实现)
    common:公共使用，例如工具类这些
    entity:对应数据表实体类对象
infrastructure:主要是业务平台，编程框架，第三方库的封装，基础算法，等等。
    external:外部第三方服务调用在此实现
    mapper:本应用与持久层交互在此实现
```
```text
领域模型只能按照此层次顺序调用对应能力[applicetion->domain->infrastructure]
对象实体只能往下穿透传递，不能向上穿透传递，entity数据要提供出去必须在applicetion-service中转做实体类转换成response实体类数据映射 
```
# 支付说明
```text
https://opendocs.alipay.com/open/194/106078?pathHash=193f2039#%E6%8E%A5%E5%85%A5%E6%96%B9%E5%BC%8F
```
