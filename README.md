Life Run
=========
  *注：此README.md文档借鉴自开源项目：eoecn/android-app*
本项目采用 Apache 授权协议，欢迎大家在这个基础上进行改进，并与大家分享。

本项目为个人练习，同时用于参加了某比赛，把代码开源给大家共同学习，有不足支持希望大家指正。

由于我现在还处于新手阶段，写的代码不乏各种漏洞残缺之处，希望大家谅解。

同时，送上我在学习Android开发时经常看到的一句话

	————分享是一种信仰,开源是一种态度！

  
# **项目简析** #

  *注：本文假设你已经有Android开发环境*

  本文以eclipse为例<br>
  启动Eclipse，导入本项目。
  推荐使用Android 4.0 以上版本的SDK：<br>
  
  target=android-14

## **一、工程目录结构** ##
根目录<br>
>├ librarys <br>
>├ source  <br> 
>├ LICENCE.txt <br>
>├ README.md <br>

目录简要解释<br>
根目录<br>
>├ librarys --引用的开源类库 <br>
>├ source --源代码 <br> 
>├ LICENCE.txt --开源协议 <br>
>├ README.md --项目帮助及项目信息 <br>

## **二、源代码目录结构** ##

**1、librarys目录** <br>
>├ ActionBarSherlock --ActonBar的开源类库，用于适兼容2.X系统 <br>
>└ ViewPagerIndicator --用于制作开始界面 <br>

**2、source目录** <br>
>├ src  <br> 
>├ libs <br>
>├ res <br>
>├ AndroidManifest.xml <br>
>├ proguard-project.txt <br>
>└ project.properties <br>

**3、source/src目录** <br>
src目录用于存放工程的包及java源码文件。
下面是src目录的子目录：

> src <br>

>├ nuist.wcw.Activity --存放Activity <br>
>├ nuist.wcw.controller --存放控制器，用于控制activity（当时犯2，仿照MVC来写 - -） <br>
>├ nuist.wcw.model --存放模型以及一些工具类 <br>
>├ nuist.wcw.service --存放系统服务类 <br>
>└ nuist.wcw.widget --存放重写的组件 <br>

**4、libs目录** <br>
libs目录用于存放项目引用的第三方jar包。

libs目录里的jar包文件：

libs
>├ jsoup-1.7.2.jar --解析HTML时用的Jsoup<br>

**5、res目录** <br>
res目录存放工程用到的图片、布局、样式等资源文件。<br>
res目录的子目录：<br>

res <br>
>├ anim <br>
>├ drawable <br>
>├ drawable-hdpi <br>
>├ drawable-ldpi <br>
>├ drawable-mdpi <br>
>├ drawable-xhdpi <br>
>├ drawable-xxhdpi <br>
>├ layout <br>
>├ menu <br>
>├ values <br>
>├ values-sw600dp <br>
>├ values-sw720dp-land <br>
>├ values-v11 <br>
>└ values-v14 <br>

**4、AndroidManifest.xml**<br>
AndroidManifest.xml用于设置应用程序的版本、主题、用户权限及注册Activity等组件及其他配置。

## **三、程序功能流程** ##
**1、APP启动流程**

AndroidManifest.xml注册的启动Activity是"nuist.wcw.Activity.WelcomeActivity"，然后进入到主界面，对应的

Activity是“nuist.wcw.Activity.RunActivity”


**2.程序功能**<br>
 (1)计时记距离<br>
 (2)历史记录<br>
 (3)设置<br>
 (4)彩蛋<br>
