**基于 brave 和 zipkip 的分布式服务跟踪工具包**

---

**环境**：servlet 3.1 ; brave 5.8.0 ; zipkin-server 2.18.3 ; dubbo 2.7.0 ; 

**愿景**：采用只引入该 jar 包的方式，便能够达到对 dubbo 或者 web 项目的服务进行跟踪

**使用方式**：

1. maven 依赖该jar包
2. `com.gysoft.trace.LocalTracing` 中修改 zipkin 服务器地址（`http://localhost:9411/api/v2/spans`） 

**当前已实现功能**：A （web项目并集成了dubbo）调用了B （不对外提供接口，集成了dubbo），B 调用了 C （不对外提供接口，集成了dubbo）,现已能完成对整个服务的跟踪。

<br>

**进度：**

- 2019.11.02
实现了服务的链式追踪，后续规划：添加 annotation

<br>

---

完完全全是最基础的，等待着大家来更新...(假装有人关注)
