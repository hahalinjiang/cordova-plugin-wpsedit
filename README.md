**************1.打开wps**************

###cordova plugin add https://github.com/hahalinjiang/cordova-plugin-wpsedit
`docPath 下载的文档地址
fileName 文档名称
params3 批注的名称`
```var docPath="http://wechat.jsnotary.org/Eapp/_files/android/phone/ccc.doc";
    var fileName="ccc.doc";
   cordova.plugins.WPSPlugin.OpenWps(docPath, fileName,"张三", function(success){
   alert(success);
   }, function(fail){
      alert(fail);
   };```
   
## 2.上传服务器
   url 要上传的服务器地址
   fileUrl 本地文件地址
   paramName  上传的参数名称
      cordova.plugins.WPSPlugin.upLoadFile(url,fileUrl,paramName, function(success){
      alert(success);
      }, function(fail){
         alert(fail);
      };
      
      
## 3.删除本地文件
     fileUrl 本地文件地址
        cordova.plugins.WPSPlugin.deleteFile(fileUrl, function(success){
        alert(success);
        }, function(fail){
           alert(fail);
        }; 
   
   

# cordova-plugin-wpsedit
