package com.example.ali.easyrepo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.repos.BitmapRepo;
import com.example.easyrepolib.repos.ByteRepo;
import com.example.easyrepolib.repos.ObjectRepo;
import com.example.easyrepolib.repos.StringRepo;

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
        StringTest();
        ByteTest();
        ObjectTest();
    }

    private void BitmapTest() {

        BitmapRepo bitmapRepo = new BitmapRepo(this, GRepo.Mode.EXTERNAL);

        Bitmap mBitmap = getBitmapForTest();

        if (bitmapRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("saving...");
        bitmapRepo.Save("test", mBitmap);
        log("saved");

        if (bitmapRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("loading...");
        Bitmap loadedBitmap = bitmapRepo.Load("test");
        log("loaded");

        image.setImageBitmap(loadedBitmap);

        log("removing...");
        bitmapRepo.Remove("test");
        log("removed");

        if (bitmapRepo.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("------------bitmap test done-----------");

    }

    private void ByteTest() {

        ByteRepo byteRepo = new ByteRepo(this, GRepo.Mode.EXTERNAL);

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

        StringRepo stringRepo = new StringRepo(this, GRepo.Mode.EXTERNAL);

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

        ObjectRepo objectRepo = new ObjectRepo<Model>(this, GRepo.Mode.EXTERNAL);

        Model model = new Model();
        model.name = "ali";
        model.Lname = "gh";
        model.age = 20;

        objectRepo.Save(model, "object");

        Model ReadedModel = (Model) objectRepo.Load("object", Model.class);

        log("ReadedModel:");
        log("--" + ReadedModel.name);
        log("--" + ReadedModel.Lname);
        log("--" + ReadedModel.age);

        objectRepo.Remove("object");
        log("removed");

        if (objectRepo.CheckExist("object", Model.class)) {
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
