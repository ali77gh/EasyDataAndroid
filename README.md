# EasyDataAndroid
an android lib that make Save , load , Remove easy for you on EXTERNAL,INTERNAL,CACHE dirs 
for bitmap , string , byteArray , Object,...

# How To Use
for EXTERNAL mode add following permission to manifest.xml and also request for permission for api > 21
~~~xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
~~~
then
~~~java
BitmapRepo bitmapRepo = new BitmapRepo(activity, GRepo.Mode.LOCAL);
//modes -> LOCAL , EXTERNAL , CACHE
//Save
bitmapRepo.Save("fileName", myBitmap);
        
 //Load
 if (bitmapRepo.CheckExist("fileName"))
   myBitmap = bitmapRepo.Load("fileName");
        
 //remove
 bitmapRepo.Remove("fileName");
~~~
see [wiki](https://github.com/ali77gh/EasyDataAndroid/wiki) for more samples

# TODO:
1. <s>Bitmap Repo</s>
2. <s>Byte Repo</s>
3. <s>String Repo</s>
4. <s>Object Repo</s>
5. <s>add GetAll , RemoveAll</s>
6. <s>Test all</s>
7. <s>encryption mode (for sensitive data like password,...) </s>
8. <s>Async/sync methods</s>
9. jitpack

# Thanks
[stackoverflow.com](https://stackoverflow.com/)

# LICENCE
[MIT](https://github.com/ali77gh/EasyDataAndroid/blob/master/LICENSE)
