**基于 brave 和 zipkip 的分布式服务跟踪工具包**

---

环境：servlet 3.1 ; brave 5.8.0 ; zipkin-server 2.18.3 ; dubbo 2.7.0 ; 

愿景：采用只引入该 jar 包的方式，便能够达到对 dubbo 或者 web 项目的服务进行跟踪

架构：话不多，拦截器

当前实现：A （web项目并集成了dubbo）-》 B （不对外提供接口，集成了dubbo），A 的服务可以追踪。

<br>

完完全全是最基础的，等待着大家来更新...(假装有人关注)
