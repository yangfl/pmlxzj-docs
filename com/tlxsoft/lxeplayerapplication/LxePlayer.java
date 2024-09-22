package com.tlxsoft.lxeplayerapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class LxePlayer extends AppCompatActivity {
    /* access modifiers changed from: private */
    public ArrayAdapter<String> adapter = null;
    /* access modifiers changed from: private */
    public Cursor cursor = null;
    /* access modifiers changed from: private */
    public ArrayList<String> data = null;
    /* access modifiers changed from: private */
    public int dataindex;
    /* access modifiers changed from: private */
    public Handler dialoghandler = new Handler();
    /* access modifiers changed from: private */
    public Runnable dialogr = new Runnable() {
        public void run() {
            LxePlayer.this.ShowSDSZDialog("提示", "此APP需要文件读写权限(文件都不让读怎么播放呢?)，此APP无法正常运行。请在下面弹出的设置窗体上点 权限管理 再点 读写手机存储 再点 允许。设置后重新运行此APP。", "OK");
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<String> filenameslist = null;
    int filescount = 0;
    /* access modifiers changed from: private */
    public Handler handler = null;
    /* access modifiers changed from: private */
    public int idindex;
    boolean isshowrpform = false;
    /* access modifiers changed from: private */
    public ListView listView = null;
    private Runnable r = new Runnable() {
        public void run() {
            if (LxePlayer.this.shuaxinstutas != 0) {
                LxePlayer.this.cursor.getString(LxePlayer.this.idindex);
                String string = LxePlayer.this.cursor.getString(LxePlayer.this.dataindex);
                int i = LxePlayer.this.cursor.getInt(LxePlayer.this.sizeindex);
                int indexOf = LxePlayer.this.filenameslist.indexOf(string);
                if (indexOf < 0) {
                    LxePlayer.this.shuaxinfindindex++;
                    LxePlayer.this.data.add(LxePlayer.this.shuaxinfindindex, "<font color='#646464'><small>" + LxePlayer.this.getFilePath(string) + "</font><br><font color='#0080FF'><big><big>" + LxePlayer.this.getFileName(string) + "</font><br><font color='#646464'><small>(大小:" + LxePlayer.this.filesizestring(i) + ")</font>");
                    LxePlayer.this.filenameslist.add(LxePlayer.this.shuaxinfindindex, string);
                    LxePlayer lxePlayer = LxePlayer.this;
                    lxePlayer.filescount = lxePlayer.filescount + 1;
                    LxePlayer.this.adapter.notifyDataSetChanged();
                } else {
                    LxePlayer.this.shuaxinfindindex = indexOf;
                }
                if (LxePlayer.this.cursor.moveToNext()) {
                    LxePlayer.this.handler.postDelayed(this, 1);
                } else {
                    LxePlayer.this.stopshuaxin();
                }
            }
        }
    };
    int screenHeight;
    int screenWidth;
    /* access modifiers changed from: private */
    public int scrolldowny;
    /* access modifiers changed from: private */
    public int scrollupdatestatus = 0;
    int shuaxinfindindex = 0;
    int shuaxinstutas = 0;
    /* access modifiers changed from: private */
    public int sizeindex;
    private TextView tv;

    public String getFileName(String str) {
        int lastIndexOf = str.lastIndexOf("/");
        int length = str.length();
        if (lastIndexOf == -1 || length == -1) {
            return null;
        }
        return str.substring(lastIndexOf + 1, length);
    }

    public String getFilePath(String str) {
        int lastIndexOf = str.lastIndexOf("/");
        return lastIndexOf != -1 ? str.substring(0, lastIndexOf + 1) : " ";
    }

    /* access modifiers changed from: private */
    public void stopshuaxin() {
        this.shuaxinstutas = 0;
        if (this.filenameslist.size() > 0 && this.filenameslist.get(0) == getString(R.string.SHUAXIN)) {
            this.data.remove(0);
            this.filenameslist.remove(0);
            this.adapter.notifyDataSetChanged();
        }
        Cursor cursor2 = this.cursor;
        if (cursor2 != null) {
            cursor2.close();
            this.cursor = null;
        }
    }

    /* access modifiers changed from: private */
    public void startshuaxin() {
        this.cursor = getContentResolver().query(Uri.parse("content://media/external/file"), new String[]{"_id", "_data", "_size"}, "_data like ?", new String[]{"%._xe"}, (String) null);
        this.idindex = this.cursor.getColumnIndex("_id");
        this.dataindex = this.cursor.getColumnIndex("_data");
        this.sizeindex = this.cursor.getColumnIndex("_size");
        if (this.filescount == 0 && this.filenameslist.size() > 0) {
            while (this.data.size() > 0) {
                this.data.remove(0);
                this.filenameslist.remove(0);
            }
            this.adapter.notifyDataSetChanged();
        }
        Cursor cursor2 = this.cursor;
        if (cursor2 == null) {
            return;
        }
        if (cursor2.moveToFirst()) {
            if (this.filenameslist.size() == 0 || this.filenameslist.get(0) != getString(R.string.SHUAXIN)) {
                this.data.add("正在刷新录像文件列表(点击取消)");
                this.filenameslist.add(getString(R.string.SHUAXIN));
            }
            this.shuaxinfindindex = 0;
            this.shuaxinstutas = 1;
            this.handler.postDelayed(this.r, 0);
            return;
        }
        String str = "<font color='#0080FF'>" + getString(R.string.MEIFAXIANWENJIAN) + "</font>";
        this.data.add(this.shuaxinfindindex, str);
        this.filenameslist.add(this.shuaxinfindindex, str);
        this.adapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public String filesizestring(int i) {
        int i2 = i / 1048576;
        int i3 = i2 / 1024;
        if (i3 > 0) {
            return String.valueOf(i3) + "G";
        }
        return String.valueOf(i2) + "M";
    }

    public void ShowTSDialog(String str, String str2, String str3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setCancelable(false);
        builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!LxePlayer.this.isshowrpform) {
                    LxePlayer.this.dialoghandler.postDelayed(LxePlayer.this.dialogr, 1);
                }
            }
        });
        AlertDialog create = builder.create();
        create.show();
        create.getWindow().setGravity(48);
    }

    public void ShowSDSZDialog(String str, String str2, String str3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setCancelable(false);
        builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", LxePlayer.this.getPackageName(), (String) null));
                LxePlayer.this.startActivity(intent);
                LxePlayer.this.finish();
            }
        });
        builder.create().show();
    }

    public void checkPermission() {
        String[] strArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WAKE_LOCK"};
        int i = 0;
        try {
            i = getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 23 && i >= 23 && !checkPermissionAllGranted(strArr)) {
            ShowTSDialog("提示", "此APP需要文件读写权限和屏幕常亮权限,请在系统弹出的窗体上选择同意，否则此APP将无法正常运行。读文件权限用于获得录像文件列表和打开读取需要播放的文件;写文件权限用于保存播放过程需要的临时文件;屏幕常亮权限用于控制播放过程中屏幕不会自动息屏", "OK");
            ActivityCompat.requestPermissions(this, strArr, 1);
        }
    }

    private boolean checkPermissionAllGranted(String[] strArr) {
        for (String checkSelfPermission : strArr) {
            if (ContextCompat.checkSelfPermission(this, checkSelfPermission) != 0) {
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            int length = iArr.length;
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    z = true;
                    break;
                } else if (iArr[i2] != 0) {
                    break;
                } else {
                    i2++;
                }
            }
            if (z) {
                stopshuaxin();
                startshuaxin();
                this.isshowrpform = true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_lxe_player);
        checkPermission();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.screenWidth = displayMetrics.widthPixels;
        this.screenHeight = displayMetrics.heightPixels;
        this.tv = (TextView) findViewById(R.id.textView_ts);
        this.tv.setText(BuildConfig.FLAVOR);
        this.listView = (ListView) findViewById(R.id.listView);
        this.filenameslist = new ArrayList<>();
        this.data = new ArrayList<>();
        this.adapter = new PanAdapter(this, R.layout.mylist, this.data);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (LxePlayer.this.filescount != 0) {
                    if (LxePlayer.this.filenameslist.get(i) == LxePlayer.this.getString(R.string.SHUAXIN)) {
                        LxePlayer.this.stopshuaxin();
                        return;
                    }
                    String str = (String) LxePlayer.this.filenameslist.get(i);
                    if (!new File(str).exists()) {
                        LxePlayer lxePlayer = LxePlayer.this;
                        Toast.makeText(lxePlayer, lxePlayer.getString(R.string.WENJIANBUCUNZAI), 0).show();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("extra_data", str);
                    intent.setClassName(BuildConfig.APPLICATION_ID, "com.tlxsoft.lxeplayerapplication.MainActivity");
                    LxePlayer.this.startActivity(intent);
                }
            }
        });
        this.scrollupdatestatus = 0;
        this.listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (LxePlayer.this.filenameslist.get(0) == LxePlayer.this.getString(R.string.SHUAXIN)) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case 0:
                        int unused = LxePlayer.this.scrollupdatestatus = 0;
                        if (LxePlayer.this.listView.getFirstVisiblePosition() == 0) {
                            int unused2 = LxePlayer.this.scrollupdatestatus = 1;
                            int unused3 = LxePlayer.this.scrolldowny = (int) motionEvent.getY();
                            break;
                        }
                        break;
                    case 1:
                        if (LxePlayer.this.listView.getFirstVisiblePosition() == 0 && LxePlayer.this.scrollupdatestatus == 2) {
                            if (((int) motionEvent.getY()) - LxePlayer.this.scrolldowny >= 0) {
                                LxePlayer.this.data.remove(0);
                                LxePlayer.this.data.add(0, "正在刷新录像文件列表(点击取消)");
                                LxePlayer.this.filenameslist.remove(0);
                                LxePlayer.this.filenameslist.add(0, LxePlayer.this.getString(R.string.SHUAXIN));
                                LxePlayer.this.adapter.notifyDataSetChanged();
                                LxePlayer.this.startshuaxin();
                            } else {
                                LxePlayer.this.data.remove(0);
                                LxePlayer.this.filenameslist.remove(0);
                                LxePlayer.this.adapter.notifyDataSetChanged();
                            }
                        }
                        int unused4 = LxePlayer.this.scrollupdatestatus = 0;
                        break;
                    case 2:
                        if (LxePlayer.this.scrollupdatestatus == 1 && ((int) motionEvent.getY()) - LxePlayer.this.scrolldowny > LxePlayer.this.screenHeight / 5) {
                            LxePlayer.this.data.add(0, "下拉刷新,上拉取消");
                            LxePlayer.this.filenameslist.add(0, LxePlayer.this.getString(R.string.XIALASHUAXIN));
                            LxePlayer.this.adapter.notifyDataSetChanged();
                            int unused5 = LxePlayer.this.scrollupdatestatus = 2;
                            int unused6 = LxePlayer.this.scrolldowny = (int) motionEvent.getY();
                            return true;
                        } else if (LxePlayer.this.scrollupdatestatus == 2) {
                            if (((int) motionEvent.getY()) - LxePlayer.this.scrolldowny < 0) {
                                LxePlayer.this.data.remove(0);
                                LxePlayer.this.filenameslist.remove(0);
                                LxePlayer.this.adapter.notifyDataSetChanged();
                                int unused7 = LxePlayer.this.scrollupdatestatus = 3;
                            }
                            int unused8 = LxePlayer.this.scrolldowny = (int) motionEvent.getY();
                            return true;
                        } else if (LxePlayer.this.scrollupdatestatus == 3) {
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
        this.handler = new Handler();
        startshuaxin();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.menu_about) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, "com.tlxsoft.lxeplayerapplication.AboutActivity");
            startActivity(intent);
            return true;
        } else if (itemId != R.id.menu_help) {
            return true;
        } else {
            Intent intent2 = new Intent();
            intent2.setClassName(BuildConfig.APPLICATION_ID, "com.tlxsoft.lxeplayerapplication.HelpActivity");
            startActivity(intent2);
            return true;
        }
    }
}
