# TextSearchEngine

采用springboot的文本搜索引擎，部署后访问前端网页即可搜索并得到结果。

# Module分层：  
层次分3层  
Model层负责文件读写，数据存储，预处理(去停止词和stem)。  
Persistence负责搜索语句的处理(符号处理，构建语法树)，Search Module算法和Retrieval算法。  
Web层负责与前端交互，处理post和get请求。  


# Model  
Model层目录：  
Data，包含自定义的数据结构  
Tools，包含文件读取，写入和分析策略的容器以及引入的stemmer文件  
Strategy，包含文件读取，写入和分析策略  
Factory，抽象工厂，负责生产对应的Strategy  
Transaction，包含本模块所有的过程业务，preProcessing类，数据存储用的docRespository，以及综合处理的Manager  

Interface，包含接口文件，用于与下一层的交流。  


# Persistence  
Persistence层目录：  
Data，包含自定义的数据结构  
Tools，包含Search Module算法和Retrieval算法策略的容器以及搜索语句的处理工具  
Strategy，包含Search Module算法和Retrieval算法策略  
Factory，抽象工厂，负责生产对应的Strategy  
Transaction，包含搜索语句语法树相关的类，自定义符号的处理，以及综合处理的Manager  

Adapter，包含适配器文件，用于与上一层的交流。  
Interface，包含接口文件，用于与下一层的交流。  


# Web  
Web层目录：  
Data，包含自定义的数据结构  
Transaction，包含处理post和get请求的两个Manager  
Adapter，包含适配器文件，用于与上一层的交流。  

# 目录解释  

# Model层  
Model层的工作主要是读取文件，解析，保存数据，并返回数据给下一层  

其中文件的读写和分析采用三类文件，Write，Read，analysis。每一类对应一个容器，通过容器驱动策略来运算，策略通过工厂类获得。  
Read定义了怎么读取一个文件并转化为对应的输出类型  
Write定义了怎么将输入类型的数据写为一个文件类型  
Analysis定义了不同的数据类型之间的转化  

PreProcessing负责文本的预处理  
DocRepository负责解析文档，保存数据到hashmap中并将分析好的数据缓存下来以便下次使用  


# Persistence层  
Persistence层的工作主要是根据分析数据处理搜索请求，并返回结果给下一层  

其中数据的分析采用两类，Search Module算法和Retrieval算法。每一类对应一个容器，通过容器驱动策略来运算，策略通过工厂类获得。  
Search Module搜索返回对应文件以及一段包含搜索词的文本  
Retrieval返回对应文件以其的结果分数  

Symbols管理搜索语句中的符号，用于定义搜索语句的关键符号。  
BoolTree定义语法树节点，包括结构节点AND OR NOT和数据节点，用于解析搜索语句，并生成对应的树结构，通过给所有的数据节点设置对应的数据，根节点就可以输出搜索结果  


# Web层  
Web层的工作主要是接收搜索请求，并返回结果给客户端  

支持post和get两种请求  


# 其他目录  
Assets包含要处理的文件  
Save包含自动生成的缓存数据  
html包含客户端网页，运行服务器后可通过网页来查询  
