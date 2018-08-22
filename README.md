# EasyDataAndroid
an android lib that make Save , load , Remove easy for you on EXTERNAL,INTERNAL,CACHE dirs 
for bitmap , string , byteArray , Object

# How To Use
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
same for ByteRepo,StringRepo,ObjectRepo

# TODO:
1. <s>Bitmap Repo</s>
2. <s>Byte Repo</s>
3. String Repo
4. Object Repo
5. add GetAll , RemoveAll
6. Test all
7. key-value object repo using sqlite
8. encryption mode (for sensitive data like password,...)
9. Async/sync methods

# Thanks
[stackoverflow.com](https://stackoverflow.com/)

# LICENCE
[LICENSE](https://github.com/ali77gh/EasyDataAndroid/blob/master/LICENSE)


