# MyBlog
SpringBoot个人博客项目

持久层使用mybatis，service层用程序维护数据库数据一致性。

Blog————Tag（多对多）
Blog————Comment（一对多）
Blog————Type（多对一）
Blog————User（多对一）
Comemnt————ChildComment(二级关系)

删除blog会自动删除对应的BlogAndTag和对应的Comment，删除标签自动更新每个blog的对应的标签栏。
Union-Find思想单表构造楼中楼评论区。

目录中有blog.sql文件，可以直接执行生成对应的数据库和表

功能：前端展示 + 后台管理，添加了看板娘（老二次猿了）。

博客地址：http://118.89.184.40/

技术组合：

后端：Spring Boot + mybatis
数据库：MySQL
前端UI：Semantic UI框架 + thymeleaf模板
工具与环境：

IDEA
Maven 3.6.2
JDK 12
springboot 2.2.5

部署：腾讯云+linux

# blog1.1

对楼中楼部分做出优化和修改，加入了Redis类（还未用上）。
