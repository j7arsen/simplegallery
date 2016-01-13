package com.j7arsen.simple.gallery.utils;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by arsen on 11.01.2016.
 */
public class Utils {

    private Context _context;

    public Utils(Context context) {
        this._context = context;
    }

    public ArrayList<String> getFilePaths(String path) {
        ArrayList<String> filePaths = new ArrayList<String>();

        File directory = new File(path);

        if (directory.isDirectory()) {
            File[] listFiles = directory.listFiles();

            if (listFiles.length > 0) {

                for (int i = 0; i < listFiles.length; i++) {

                    String filePath = listFiles[i].getAbsolutePath();

                    if (isSupportedFile(filePath)) {
                        filePaths.add(filePath);
                    }
                }
            } else {
                Toast.makeText(
                        _context,
                        path
                                + " is empty. Please load some images!",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("Error!");
            alert.setMessage(path
                    + " directory path is not valid!");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return filePaths;
    }

    private boolean isSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (AppConstants.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }

}
