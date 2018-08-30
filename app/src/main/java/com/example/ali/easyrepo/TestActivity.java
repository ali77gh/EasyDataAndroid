package com.example.ali.easyrepo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.abstracts.onSaveCompleted;
import com.example.easyrepolib.repos.BitmapRepo;
import com.example.easyrepolib.repos.ByteRepo;
import com.example.easyrepolib.repos.ObjectRepo;
import com.example.easyrepolib.repos.StringRepo;
import com.example.easyrepolib.security.DeviceKeyGenerator;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    private TextView log;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        log = findViewById(R.id.loger);
        image = findViewById(R.id.image);

        //run tests
        BitmapTest();
        //StringTest();
        //ByteTest();
        //ObjectTest();
    }

    private void BitmapTest() {

        final BitmapRepo bitmapRepo = new BitmapRepo(this, GRepo.Mode.LOCAL);

        Bitmap mBitmap = getBitmapForTest();

       bitmapRepo.SaveAsync("filename", mBitmap, this, new onSaveCompleted() {
           @Override
           public void onSaveComplete() {
               bitmapRepo.LoadAsync("filename", TestActivity.this, new BitmapRepo.OnBitmapLoad() {
                   @Override
                   public void onBitmapLoad(final Bitmap bitmap) {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               image.setImageBitmap(bitmap);
                           }
                       });

                   }
               });
           }
       });

        log(DeviceKeyGenerator.Generate(this,"MySecret"));

    }

    private void ByteTest() {

        ByteRepo byteRepo = new ByteRepo(this, GRepo.Mode.LOCAL);

        byte[] bytes = new byte[4];

        bytes[0] = 1;
        bytes[1] = 1;
        bytes[2] = 0;
        bytes[3] = 1;

        if (byteRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("saving...");
        byteRepo.Save("test", bytes);
        log("saved");

        if (byteRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("loading...");
        byte[] loadedBytes = byteRepo.Load("test");
        log("loaded");

        log("READED:" + loadedBytes);

        log("bytes");
        for (File file:byteRepo.GetAll()){
            log("--"+file.getName());
        }



        log("removing...");
        byteRepo.Remove("test");
        log("removed");

        if (byteRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("------------string test done-----------");

    }

    private void StringTest() {

        StringRepo stringRepo = new StringRepo(this, GRepo.Mode.LOCAL);

        String string = "write this file";

        if (stringRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("saving...");
        stringRepo.Save("test", string);
        log("saved");

        if (stringRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("loading...");
        String loadedString = stringRepo.Load("test");
        log("loaded");

        log("READED:" + loadedString);

        log("strings");
        for (File file:stringRepo.GetAll()){
            log("--"+file.getName());
        }

        stringRepo.RemoveAll();

        log("strings");
        for (File file:stringRepo.GetAll()){
            log("--"+file.getName());
        }

        log("removing...");
        stringRepo.Remove("test");
        log("removed");

        if (stringRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("------------string test done-----------");

    }

    private void ObjectTest() {

        ObjectRepo objectRepo = new ObjectRepo<Model>(this, GRepo.Mode.LOCAL);

        Model model = new Model();
        model.name = "ali";
        model.Lname = "gh";
        model.age = 20;

        objectRepo.Save("object",model);

        Model ReadedModel = (Model) objectRepo.Load("object", Model.class);

        log("ReadedModel:");
        log("--" + ReadedModel.name);
        log("--" + ReadedModel.Lname);
        log("--" + ReadedModel.age);

        log("objects");
        for (File file:objectRepo.GetAll()){
            log("--"+file.getName());
        }

        objectRepo.Remove("object");
        log("removed");

        if (objectRepo.CheckExist("object")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

    }

    private void log(String msg) {
        log.append(msg + "\n");
    }

    private Bitmap getBitmapForTest() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }
}
