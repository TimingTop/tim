## 代码分层

### 

多路 reactor 模型实现

https://www.cnblogs.com/niuyourou/p/13113685.html



```










第一层 ：         connector        就是 network 层

    功能点： 1. Endpoint   保存整个 网络连接的 业务，对象等
           2. Connector   直接对应的是 jdk socket层,拿到  rawData 
           3. 上面的 rawData 传给 channel ，
           4. channel 里面有个 parser 解码器

        

3. RawData(二进制流) --> （进入协议解析）
           
```