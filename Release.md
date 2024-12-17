## 介绍

文件夹备注工具桌面版，需要传入一个`目标目录`启动，启动后出现一个非常简洁的`对话框`：包含一个`文本输入框`，
以接收`备注文本`，一个`取消`按钮，一个`确定`按钮。点击按钮即可关闭程序。

## 三种启动方式

1.使用 java 命令启动

```
# 传入目标文件夹目录
java -cp C:\comments-desktop-1.0\class sample.Main "C:\Target_Dir"
```


2.bat启动

```
# 传入目标文件夹目录
bin\comment.bat "C:\Target_Dir"
``` 

3.注册表启动（推荐）

```
#注册表新增项及对应的(default)值

Windows Registry Editor Version 5.00

[HKEY_CLASSES_ROOT\Directory\Background\shell\Comments]
@=备注

[HKEY_CLASSES_ROOT\Directory\Background\shell\Comments\command]
@=C:\java\bin\javaw.exe -cp C:\comments-desktop-1.0\class sample.Main "%v."

```

注：具体java目录以及class目录请根据个人情况修改，完成以上配置后，鼠标右键菜单(context-menu)会增加`备注`一项，点击备注则会启动该程序，并将当前目录传入参数。

警告：注册表属敏感操作，请谨慎使用，误操作会导致系统无法正常工作