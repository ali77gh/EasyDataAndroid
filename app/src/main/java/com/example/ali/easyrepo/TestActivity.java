package com.example.ali.easyrepo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.abstracts.onSaveCompleted;
import com.example.easyrepolib.repos.BitmapDAO;
import com.example.easyrepolib.repos.ByteDAO;
import com.example.easyrepolib.repos.ObjectDAO;
import com.example.easyrepolib.repos.SafeBox;
import com.example.easyrepolib.repos.StringDAO;
import com.example.easyrepolib.security.DeviceKeyGenerator;
import com.example.easyrepolib.sqlite.KeyValDb;

import java.io.File;
import java.util.List;

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
//        BitmapTest();
        StringTest();
        ByteTest();
        ObjectTest();
        GenericDAOTest();
    }

    private void BitmapTest() {

        final BitmapDAO bitmapDAO = new BitmapDAO(this, GRepo.Mode.LOCAL);

        Bitmap mBitmap = getBitmapForTest();

        bitmapDAO.SaveAsync("filename", mBitmap, this, new onSaveCompleted() {
            @Override
            public void onSaveComplete() {
                bitmapDAO.LoadAsync("filename", TestActivity.this, new BitmapDAO.OnBitmapLoad() {
                    @Override
                    public void onBitmapLoad(final Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    private void ByteTest() {

        ByteDAO byteDAO = new ByteDAO(this, GRepo.Mode.LOCAL);

        byte[] bytes = new byte[4];

        bytes[0] = 1;
        bytes[1] = 1;
        bytes[2] = 0;
        bytes[3] = 1;

        if (byteDAO.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("saving...");
        byteDAO.Save("test", bytes);
        log("saved");

        if (byteDAO.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("loading...");
        byte[] loadedBytes = byteDAO.Load("test");
        log("loaded");

        log("READED:" + loadedBytes);

        log("bytes");
        for (File file : byteDAO.GetAll()) {
            log("--" + file.getName());
        }


        log("removing...");
        byteDAO.Remove("test");
        log("removed");

        if (byteDAO.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("------------string test done-----------");

    }

    private void StringTest() {

        StringDAO stringDAO = new StringDAO(this, GRepo.Mode.LOCAL);

        String string = "write this file";

        if (stringDAO.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("saving...");
        stringDAO.Save("test", string);
        log("saved");

        if (stringDAO.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("loading...");
        String loadedString = stringDAO.Load("test");
        log("loaded");

        log("READED:" + loadedString);

        log("strings");
        for (File file : stringDAO.GetAll()) {
            log("--" + file.getName());
        }

        stringDAO.RemoveAll();

        log("strings");
        for (File file : stringDAO.GetAll()) {
            log("--" + file.getName());
        }

        log("removing...");
        stringDAO.Remove("test");
        log("removed");

        if (stringDAO.CheckExist("test")) {
            log("test is exist");
        } else {
            log("test is not exist ");
        }

        log("------------string test done-----------");

    }

    private void ObjectTest() {
        ObjectDAO o = new ObjectDAO(this, GRepo.Mode.LOCAL);

        User u = new User();
        u.name = "ali";

        o.Save("user",u);

        o.Load("user",User.class);

    }

    private void GenericDAOTest(){

        UserDao db = new UserDao(this);

        db.Drop();

        User u = new User();

        u.age = 18;
        u.name = "ali";
        u.lName = "ghahremani";

        db.Insert(u);
        u.name = "hassan";
        db.Insert(u);
        db.Insert(u);

        List<User> users = db.getAll();

        List<User> alis = db.getWithCondition(new KeyValDb.Condition() {
            @Override
            public boolean IsConditionTrue(Object object) {
                return  ((User) object).name.equals("ali");
            }
        });
    }

    private void TestSafeBox(){
        String key = DeviceKeyGenerator.Generate(this);
        SafeBox safeBox = new SafeBox(this,key);

        safeBox.Save("password","myPassword");

        safeBox.Load("password");
    }

    private void log(String msg) {
        log.append(msg + "\n");
    }

    private Bitmap getBitmapForTest() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }
}
