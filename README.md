## Woodpecker
Woodpecker是一个易使用、高效、通用、可扩展的数据库测试框架，可评测数据库的性能、功能、以及运行的稳定性。Woodpecker提供了具有强表达力、可高效构造测试案例的测试定义语言，通过使用Java CC解析器，实现了复杂、完备的语义解析机制，并通过Java的反射机制将关键字映射到命令调用。用户不需要理解复杂的底层解析，即可使用简单的测试关键字来实现对数据库的一键评测。

Woodpecker实现了轻量级的细粒度统计信息收集与分析，支持在测试结束后自动生成测试结果报告，并支持自动化的回归测试。  Woodpecker支持适配多种主流数据库系统，并提供数据库访问其他数据库的接口以供后续开发，测试案例库可以在多种语法兼容的数据库系统间共享复用。

## Getting Started
本项目采用Maven作为包管理工具，可以用IDEA直接Clone下来，入口是controller包中的TestController类，推荐Java8以上

目前适配mysql5.7，postgresql

## Folder Structure

Woodpecker项目包含以下文件，其中：
- `src`: 源代码

- `config`: Woodpecker配置文件，数据库配置文件

- `database_instance`: 可导入的一些数据库instance

- `ideal_result_set`: 理想结果集文件夹，在功能测试中，一些需要对比结果的功能需要测试人员手动写理想结果，Woodpecker会将数据库执行的结果和理想结果进行对比，来验证数据库的功能是否符合预期

- `log`: Woodpecker.log日志

- `middle_result`: 中间结果集，由测试Case转变，Woodpecker直接执行的是中间结果集中的SQL语句

- `test_case`: 测试Case，需要使用Woodpecker工具定义的测试定义语言(TDL)编写，要写在测试Group中

- `case_reports`: 用来存储测试结果集

- `sysbench_results`: 测试结束后提供sysbench测试报告

- `fonts`: 存储测试结果报告中需要用到的字体文件

- `monitor_info`: 存储从远端服务器拉取的监控文件

- `tools`: 存储通用测试工具的源码包

- `mysql_tests`:  存储mysql_test_framework的测试案例

  





