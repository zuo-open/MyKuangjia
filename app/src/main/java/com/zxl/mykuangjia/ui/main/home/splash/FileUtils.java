package com.zxl.mykuangjia.ui.main.home.splash;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class FileUtils {

    //将文件内容解析成对象
    public static <T extends Serializable> T parseFile(File file) {
        ObjectInputStream in = null;
        T t = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            t = (T) in.readObject();
        } catch (EOFException e) {
            // ... this is fine
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return t;
    }
}
