# book_ldrtc
此项目主要为(也包含其它章节一些代码)本书第8章微服务架构技术实现：各种RPC实现实例，其中框架生成代码，可以参考书中说明。
#代码结构说明
1. src/main/java/example/avro 为Avro模块
2. src/main/java/example/proto 为grpc所以来的protobuf3相关代码
3. src/main/java/io/grpc 为grpc官方部分测试代码
4. src/main/java/org/light/cxf: 为CXF相关实例
5. src/main/java/org/light/guava: 为guava cache实例
6. src/main/java/org/light/ice:  为Zero-ICE部分实例代码
7. src/main/java/org/light/jetty: 为Jetty嵌入式Servlet实例实现的WebService
8. src/main/java/org/light/ws:  为JDK本身的WebService实现实例
9. src/generated： 为框架命令声生成的相关代码
