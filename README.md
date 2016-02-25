# ZhuaWeiBo
1,下载最新版的eclipse（如果没有jdk，先安装jdk），这是下载地址http://eclipse.bluemix.net/packages/mars.1/data/eclipse-jee-mars-1-win32-x86_64.zip?cm_mc_uid=73164027415914540060051&cm_mc_sid_50200000=1456370545   eclipse里已经集成了git插件，从github导入整个项目。
2，运行前用chrome 网页调试工具（F12）模拟一个诺基亚N9手机浏览器，并在浏览器中登陆自己的微博，打开weibo.cn页面并复制request header 的所有内容粘贴替换HttpConfig/zhuye文件中，再打开http://weibo.cn/2671109275/follow 获得request header 复制替换到HttpConfig/following文件中。
3，客户端抓取运行的主函数在Weibo.weibo包下，运行后需要输入自己的名字然后回车就可以开始抓取了。如果有异常抛出欢迎微信联系我。或者email lzsnolimit@gmail.com
