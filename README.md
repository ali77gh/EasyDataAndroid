# EasyDataAndroid
an android lib that make Save , load , Remove easy for you on EXTERNAL,INTERNAL,CACHE dirs 
for bitmap , string , byteArray , Object,...

# installation
download [jar](https://github.com/ali77gh/EasyDataAndroid/releases/download/2.0.0/easydataandroid.jar) file <br>
[install it in simple 3 steps](https://github.com/ali77gh/EasyDataAndroid/wiki/install)
# How To Use
for EXTERNAL mode add following permission to manifest.xml and also request for permission for api > 21
~~~xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
~~~
then:
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
for async use:
~~~java
//save
bitmapRepo.SaveAsync("filename", mBitmap, activity, new onSaveCompleted() {
           @Override
           public void onSaveComplete() {
               
           }
});
//load
bitmapRepo.LoadAsync("filename", TestActivity.this, new BitmapRepo.OnBitmapLoad() {
           @Override
           public void onBitmapLoad(Bitmap bitmap) {
                  imageView.setImageBitmap(bitmap);//runing on ui thread
           }
});
~~~
see [wiki](https://github.com/ali77gh/EasyDataAndroid/wiki) for more samples

# Who Use this?
[UniTools android](https://github.com/unitools-apps/UniTools-android)

# LICENCE
[MIT](https://github.com/ali77gh/EasyDataAndroid/blob/master/LICENSE)
