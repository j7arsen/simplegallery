package com.j7arsen.simple.gallery.directorychoose;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by arsen on 09.01.2016.
 */
public class DirectoryChooserDialog {
    private String mSdcardDirectory = "";
    private Context mContext;
    private TextView mTitleView;

    private String mDir = "";
    private List<String> mSubdirs = null;
    private ChosenDirectoryListener mChosenDirectoryListener = null;
    private ArrayAdapter<String> mListAdapter = null;

    public interface ChosenDirectoryListener
    {
        public void onChosenDir(String chosenDir);
    }

    public DirectoryChooserDialog(Context context, ChosenDirectoryListener chosenDirectoryListener)
    {
        mContext = context;
        mSdcardDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        mChosenDirectoryListener = chosenDirectoryListener;

        try
        {
            mSdcardDirectory = new File(mSdcardDirectory).getCanonicalPath();
        }
        catch (IOException ioe)
        {
        }
    }



    public void chooseDirectory()
    {
        chooseDirectory(mSdcardDirectory);
    }

    public void chooseDirectory(String dir)
    {
        File dirFile = new File(dir);
        if (! dirFile.exists() || ! dirFile.isDirectory())
        {
            dir = mSdcardDirectory;
        }

        try
        {
            dir = new File(dir).getCanonicalPath();
        }
        catch (IOException ioe)
        {
            return;
        }

        mDir = dir;
        mSubdirs = getDirectories(dir);

        class DirectoryOnClickListener implements DialogInterface.OnClickListener
        {
            public void onClick(DialogInterface dialog, int item)
            {
                mDir += "/" + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
                updateDirectory();
            }
        }

        AlertDialog.Builder dialogBuilder =
                createDirectoryChooserDialog(dir, mSubdirs, new DirectoryOnClickListener());

        dialogBuilder.setPositiveButton("OK", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (mChosenDirectoryListener != null)
                {
                    mChosenDirectoryListener.onChosenDir(mDir);
                }
            }
        }).setNegativeButton("Cancel", null);

        final AlertDialog dirsDialog = dialogBuilder.create();

        dirsDialog.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if ( mDir.equals(mSdcardDirectory) )
                    {
                        return false;
                    }
                    else
                    {
                        mDir = new File(mDir).getParent();
                        updateDirectory();
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        });

        dirsDialog.show();
    }


    private List<String> getDirectories(String dir)
    {
        List<String> dirs = new ArrayList<String>();

        try
        {
            File dirFile = new File(dir);
            if (! dirFile.exists() || ! dirFile.isDirectory())
            {
                return dirs;
            }

            for (File file : dirFile.listFiles())
            {
                if ( file.isDirectory() )
                {
                    dirs.add( file.getName() );
                }
            }
        }
        catch (Exception e)
        {
        }

        Collections.sort(dirs, new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return o1.compareTo(o2);
            }
        });

        return dirs;
    }

    private AlertDialog.Builder createDirectoryChooserDialog(String title, List<String> listItems,
                                                             DialogInterface.OnClickListener onClickListener)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

        LinearLayout titleLayout = new LinearLayout(mContext);
        titleLayout.setOrientation(LinearLayout.VERTICAL);

        mTitleView = new TextView(mContext);
        mTitleView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        mTitleView.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
        mTitleView.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        mTitleView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mTitleView.setText(title);



        titleLayout.addView(mTitleView);

        dialogBuilder.setCustomTitle(titleLayout);

        mListAdapter = createListAdapter(listItems);

        dialogBuilder.setSingleChoiceItems(mListAdapter, -1, onClickListener);
        dialogBuilder.setCancelable(false);

        return dialogBuilder;
    }

    private void updateDirectory()
    {
        mSubdirs.clear();
        mSubdirs.addAll( getDirectories(mDir) );
        mTitleView.setText(mDir);

        mListAdapter.notifyDataSetChanged();
    }

    private ArrayAdapter<String> createListAdapter(List<String> items)
    {
        return new ArrayAdapter<String>(mContext,
                android.R.layout.select_dialog_item, android.R.id.text1, items)
        {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                if (v instanceof TextView)
                {
                    TextView tv = (TextView) v;
                    tv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                    tv.setEllipsize(null);
                }
                return v;
            }
        };
    }

}
