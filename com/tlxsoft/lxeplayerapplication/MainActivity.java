package com.tlxsoft.lxeplayerapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    float actiomovejl = 0.0f;
    long actiondowntime = 0;
    float actiondownx = 0.0f;
    float actiondowny = 9.0f;
    /* access modifiers changed from: private */
    public AudioPlayManager audioPlayManager = null;
    /* access modifiers changed from: private */
    public Bitmap bm = null;
    float fangdabeishu = 1.0f;
    long fangdaplaycount = 0;
    Paint gbzqpaint;
    Handler handler = new Handler();
    String infotext;
    int infotextfontcolor;
    String infotextfontname;
    int infotextfontsize;
    int infotextfontstyle;
    Paint infotextpaint;
    int infox;
    int infoy;
    boolean is2zhi = false;
    boolean isactiondown = false;
    boolean isactionmoved = false;
    boolean iscanfangda = true;
    boolean isdjtx = true;
    boolean isgbzq = true;
    boolean isintentopen = false;
    boolean ispaused;
    boolean ispaused_seekpre;
    boolean isquit = false;
    boolean isseekbarmoving = false;
    boolean isstarted = false;
    int iszc;
    boolean iszdgs = true;
    /* access modifiers changed from: private */
    public ImageView iv;
    int[] ivlocation = new int[2];
    int jiecount = 0;
    int kzbeishu = 1;
    Paint leftclickpaint;
    int lserrorcount = 0;
    String lxefilename;
    /* access modifiers changed from: private */
    public int lxframecount;
    /* access modifiers changed from: private */
    public int lxframedelay;
    Canvas mCanvas = null;
    /* access modifiers changed from: private */
    public String mimastring;
    /* access modifiers changed from: private */
    public int nowframei;
    int nowjieindex = 0;
    Bitmap pausebitmap;
    int pauseiconr = 0;
    Drawable pausevector;
    int preorientation = 0;
    Runnable r = new Runnable() {
        public void run() {
            if (MainActivity.this.isstarted && MainActivity.this.bm != null && !MainActivity.this.isquit) {
                if (MainActivity.this.ispaused) {
                    MainActivity.this.handler.postDelayed(this, (long) MainActivity.this.lxframedelay);
                    return;
                }
                panLog.wtf("videoplaying", "1");
                if (MainActivity.this.nowframei < MainActivity.this.lxframecount) {
                    if (MainActivity.this.wavziptype > 0 && MainActivity.this.audioPlayManager.starttime > 0) {
                        MainActivity mainActivity = MainActivity.this;
                        long unused = mainActivity.starttime = mainActivity.audioPlayManager.starttime;
                    }
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    int access$300 = (int) (((((long) (MainActivity.this.lxframedelay / 2)) + elapsedRealtime) - MainActivity.this.starttime) / ((long) MainActivity.this.lxframedelay));
                    if (access$300 >= MainActivity.this.nowframei) {
                        if (MainActivity.this.kzbeishu > 1 && access$300 > MainActivity.this.nowframei + MainActivity.this.kzbeishu) {
                            MainActivity mainActivity2 = MainActivity.this;
                            int unused2 = mainActivity2.nowframei = (((mainActivity2.nowframei - 1) / MainActivity.this.kzbeishu) + 1) * MainActivity.this.kzbeishu;
                            if (MainActivity.this.nowframei >= MainActivity.this.lxframecount) {
                                MainActivity mainActivity3 = MainActivity.this;
                                int unused3 = mainActivity3.nowframei = mainActivity3.lxframecount - 1;
                            }
                        }
                        MainActivity mainActivity4 = MainActivity.this;
                        mainActivity4.DrawNextFrameBmp(mainActivity4.nowframei);
                        if (MainActivity.this.iszc == 0 && MainActivity.this.fangdabeishu > 1.0f && MainActivity.this.nowframei % (1000 / MainActivity.this.lxframedelay) == 0) {
                            MainActivity.this.fangdaplaycount++;
                            if (MainActivity.this.fangdaplaycount > 60) {
                                MainActivity mainActivity5 = MainActivity.this;
                                mainActivity5.fangdabeishu = 1.0f;
                                mainActivity5.bmchanged();
                                MainActivity mainActivity6 = MainActivity.this;
                                mainActivity6.iscanfangda = false;
                                Toast.makeText(mainActivity6, R.string.WZCBBRFD, 0).show();
                            }
                        }
                        MainActivity.access$108(MainActivity.this);
                        MainActivity.this.drawwzcorinf();
                        MainActivity.this.seekBar.setProgress(MainActivity.this.nowframei);
                        if (MainActivity.this.seekBar.getVisibility() == 0) {
                            if ((MainActivity.this.nowframei - MainActivity.this.showseekbarframei) * MainActivity.this.lxframedelay > 5000) {
                                MainActivity.this.seekBar.setVisibility(4);
                            } else {
                                MainActivity mainActivity7 = MainActivity.this;
                                mainActivity7.drawtimeandico(mainActivity7.ispaused, MainActivity.this.nowframei);
                            }
                        }
                        panLog.wtf("videoplaying", "2");
                        MainActivity.this.iv.setImageBitmap(MainActivity.this.bm);
                        long access$3002 = ((long) MainActivity.this.lxframedelay) - ((SystemClock.elapsedRealtime() - elapsedRealtime) % ((long) MainActivity.this.lxframedelay));
                        if (access$300 > MainActivity.this.nowframei) {
                            MainActivity.this.handler.postDelayed(this, 1);
                        } else {
                            MainActivity.this.handler.postDelayed(this, access$3002);
                        }
                        panLog.wtf("videoplaying", "3");
                        return;
                    }
                    MainActivity.this.handler.postDelayed(this, (long) MainActivity.this.lxframedelay);
                } else if (MainActivity.this.jiecount == 1) {
                    if (MainActivity.this.isintentopen) {
                        MainActivity.this.finish();
                        return;
                    }
                    if (!MainActivity.this.ispaused) {
                        MainActivity.this.pause();
                    }
                    int unused4 = MainActivity.this.nowframei = 0;
                    MainActivity mainActivity8 = MainActivity.this;
                    mainActivity8.DrawGotoFrameBmp(mainActivity8.nowframei);
                    MainActivity.this.redrawbm();
                    MainActivity.this.handler.postDelayed(this, (long) MainActivity.this.lxframedelay);
                } else if (MainActivity.this.jiecount <= 1) {
                } else {
                    if (MainActivity.this.nowjieindex < MainActivity.this.jiecount - 1) {
                        MainActivity.this.closejie();
                        MainActivity.this.nowjieindex++;
                        MainActivity mainActivity9 = MainActivity.this;
                        int unused5 = mainActivity9.openjie(mainActivity9.nowjieindex);
                    } else if (MainActivity.this.isintentopen) {
                        MainActivity.this.finish();
                    } else {
                        if (!MainActivity.this.ispaused) {
                            MainActivity.this.pause();
                        }
                        MainActivity.this.closejie();
                        int unused6 = MainActivity.this.openjie(0);
                        MainActivity.this.redrawbm();
                        MainActivity.this.handler.postDelayed(this, (long) MainActivity.this.lxframedelay);
                    }
                }
            }
        }
    };
    Bitmap resumebitmap;
    Drawable resumevector;
    Paint rightclickpaint;
    int screenHeight;
    int screenWidth;
    SeekBar seekBar;
    Handler seekbarhandler = new Handler();
    int[] seekbarlocation = new int[2];
    long seekbarpremovetime = 0;
    Runnable seekbarr = new Runnable() {
        public void run() {
            int i;
            if (MainActivity.this.isseekbarmoving) {
                panLog.wtf("MainActivity onProgressChanged", "0");
                if (MainActivity.this.nowframei != MainActivity.this.seekBar.getProgress()) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    if (elapsedRealtime - MainActivity.this.seekbarpremovetime > 300) {
                        MainActivity mainActivity = MainActivity.this;
                        int unused = mainActivity.nowframei = mainActivity.seekBar.getProgress();
                        panLog.wtf("MainActivity onProgressChanged1", String.valueOf(MainActivity.this.nowframei));
                        MainActivity mainActivity2 = MainActivity.this;
                        mainActivity2.DrawGotoFrameBmp(mainActivity2.nowframei);
                        i = MainActivity.this.nowframei;
                        panLog.wtf("MainActivity onProgressChanged2", String.valueOf(MainActivity.this.nowframei));
                        MainActivity.this.seekbarpremovetime = elapsedRealtime;
                    } else {
                        MainActivity.this.DrawBmp();
                        i = MainActivity.this.seekBar.getProgress();
                    }
                    MainActivity mainActivity3 = MainActivity.this;
                    mainActivity3.drawtimeandico(mainActivity3.ispaused_seekpre, i);
                    MainActivity.this.drawwzcorinf();
                    MainActivity.this.iv.setImageBitmap(MainActivity.this.bm);
                }
                MainActivity.this.handler.postDelayed(this, 100);
            }
        }
    };
    Bitmap settingbitmap;
    Drawable settingvector;
    int showseekbarframei;
    /* access modifiers changed from: private */
    public long starttime;
    TextView textView;
    TextView textView_ts;
    Rect timetextbounds;
    Paint timetextpaint;
    PowerManager.WakeLock wakeLock;
    String wavfilename;
    int wavziptype = 0;
    int wzcfangdatishi = 0;

    public native ByteBuffer DirectBufferFromJNI();

    public native boolean DrawBmpFromJNI(Object obj, boolean z);

    public native boolean DrawGotoFrameBmpFromJNI(Object obj, int i, boolean z);

    public native boolean DrawNextFrameBmpFromJNI(Object obj, int i, boolean z);

    public native boolean DrawcursorFromJNI(Object obj);

    public native int GetHeightFromJNI();

    public native int GetWidthFromJNI();

    public native int GetkzbeishuJNI();

    public native int GetlxframecountFromJNI();

    public native int GetlxframedelayFromJNI();

    public native int GetwavziptypeFromJNI();

    public native boolean MemFromJNI(Object obj);

    public native int SetFilenameFromJNI(String str, String str2);

    public native byte[] gejiecaptionJNI(int i);

    public native int getcursorxJNI();

    public native int getcursoryJNI();

    public native int getgbzqcolorbhJNI();

    public native int getgbzqtmdJNI();

    public native int getgbzqtxtypeJNI(int i);

    public native byte[] getinfotextJNI();

    public native int getinfotextfontcolorJNI();

    public native byte[] getinfotextfontnameJNI();

    public native int getinfotextfontsizeJNI();

    public native int getinfotextfontstyleJNI();

    public native int getinfoxJNI();

    public native int getinfoyJNI();

    public native int getiszcJNI();

    public native int openjieJNI(int i);

    public native boolean setaddcanvasxyJNI(int i, int i2);

    public native String stringFromJNI();

    public native int testmimaJNI(String str);

    static /* synthetic */ int access$108(MainActivity mainActivity) {
        int i = mainActivity.nowframei;
        mainActivity.nowframei = i + 1;
        return i;
    }

    static {
        System.loadLibrary("native-lib");
    }

    /* access modifiers changed from: package-private */
    public void drawtimeandico(boolean z, int i) {
        drawtimetext(gettimestring(i * this.lxframedelay) + "/" + gettimestring(this.lxframecount * this.lxframedelay));
        if (z) {
            drawresumeico();
        } else {
            drawpauseico();
        }
    }

    /* access modifiers changed from: package-private */
    public void redrawbm() {
        DrawBmp();
        if (this.seekBar.getVisibility() == 0) {
            drawtimeandico(this.ispaused, this.nowframei);
        }
        drawwzcorinf();
        this.iv.setImageBitmap(this.bm);
    }

    /* access modifiers changed from: package-private */
    public void xiuzhengfontsize() {
        int i = (int) (((float) ((this.infotextfontsize * 13) / 10)) / this.fangdabeishu);
        if (i < 30) {
            i = 30;
        }
        this.infotextpaint.setTextSize((float) i);
        this.timetextpaint.setTextSize(60.0f / (((this.fangdabeishu - 1.0f) / 2.0f) + 1.0f));
    }

    /* access modifiers changed from: package-private */
    public void bmchanged() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.screenWidth = displayMetrics.widthPixels;
        this.screenHeight = displayMetrics.heightPixels;
        int GetWidthFromJNI = GetWidthFromJNI();
        int GetHeightFromJNI = GetHeightFromJNI();
        float f = this.fangdabeishu;
        if (f > 1.0f) {
            GetWidthFromJNI = (int) (((float) GetWidthFromJNI) / f);
            int i = (this.screenHeight * GetWidthFromJNI) / this.screenWidth;
            if (i <= GetHeightFromJNI) {
                GetHeightFromJNI = i;
            }
        }
        Bitmap bitmap = this.bm;
        if (bitmap != null && bitmap.isRecycled()) {
            this.bm.recycle();
        }
        this.bm = Bitmap.createBitmap(GetWidthFromJNI, GetHeightFromJNI, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.bm);
        xiuzhengfontsize();
        redrawbm();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.orientation == 2 && this.preorientation == 1) {
            float f = this.fangdabeishu;
            if (f > 1.0f) {
                this.fangdabeishu = f / 2.0f;
                if (this.fangdabeishu < 1.0f) {
                    this.fangdabeishu = 1.0f;
                }
            }
        } else if (configuration.orientation == 1 && this.preorientation == 2) {
            float f2 = this.fangdabeishu;
            if (f2 > 1.0f) {
                this.fangdabeishu = f2 * 2.0f;
                if (this.fangdabeishu > 10.0f) {
                    this.fangdabeishu = 10.0f;
                }
            }
        }
        this.preorientation = configuration.orientation;
        bmchanged();
    }

    public void onDestroy() {
        this.isquit = true;
        this.mimastring = null;
        this.lxefilename = null;
        closefile();
        super.onDestroy();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        if (this.isstarted && view.getId() == R.id.imageView) {
            this.iv.getLocationOnScreen(this.ivlocation);
            int[] iArr = this.ivlocation;
            int rawX = ((int) motionEvent.getRawX()) - iArr[0];
            int rawY = ((int) motionEvent.getRawY()) - iArr[1];
            this.seekBar.getLocationOnScreen(this.seekbarlocation);
            int[] iArr2 = this.seekbarlocation;
            int i = iArr2[0];
            int[] iArr3 = this.ivlocation;
            iArr2[0] = i - iArr3[0];
            iArr2[1] = iArr2[1] - iArr3[1];
            switch (motionEvent.getAction()) {
                case 0:
                    if (rawY + 20 <= this.seekbarlocation[1]) {
                        this.is2zhi = false;
                        this.isactionmoved = false;
                        this.isactiondown = false;
                        if (this.seekBar.getVisibility() == 0) {
                            panLog.wtf("testdj", String.valueOf(rawX) + " " + String.valueOf(rawY) + " " + String.valueOf(this.iv.getWidth() / 2) + " " + String.valueOf(this.iv.getHeight() / 2) + " " + String.valueOf(this.pauseiconr));
                            int width = ((rawX - (this.iv.getWidth() / 2)) * (rawX - (this.iv.getWidth() / 2))) + ((rawY - (this.iv.getHeight() / 2)) * (rawY - (this.iv.getHeight() / 2)));
                            int i2 = this.pauseiconr;
                            if (width < i2 * i2) {
                                pause();
                                return true;
                            }
                            int i3 = this.pauseiconr;
                            if (((rawX - (this.iv.getWidth() / 2)) * (rawX - (this.iv.getWidth() / 2))) + (((rawY - (this.iv.getHeight() / 2)) - (this.pauseiconr * 2)) * ((rawY - (this.iv.getHeight() / 2)) - (i3 * 2))) < i3 * i3) {
                                showPopupMenu(this.textView_ts);
                                return true;
                            }
                        }
                        this.isactiondown = true;
                        this.actiondownx = (float) rawX;
                        this.actiondowny = (float) rawY;
                        this.actiomovejl = 0.0f;
                        this.actiondowntime = SystemClock.elapsedRealtime();
                        this.wzcfangdatishi = 0;
                        break;
                    } else {
                        if (this.seekBar.getVisibility() != 0) {
                            this.seekBar.setVisibility(0);
                            this.showseekbarframei = this.nowframei;
                            if (this.ispaused) {
                                redrawbm();
                            }
                        }
                        return true;
                    }
                case 1:
                    if (!this.is2zhi && this.isactiondown && !this.isactionmoved) {
                        if (SystemClock.elapsedRealtime() - this.actiondowntime >= 1000) {
                            this.iszdgs = true;
                            setaddcanvasxyJNI(0, 0);
                            if (this.ispaused) {
                                redrawbm();
                            }
                            Toast.makeText(this, "自动光标跟随", 0).show();
                        } else if (this.seekBar.getVisibility() != 0) {
                            this.seekBar.setVisibility(0);
                            this.showseekbarframei = this.nowframei;
                            if (this.ispaused) {
                                redrawbm();
                            }
                        } else {
                            this.seekBar.setVisibility(4);
                            if (this.ispaused) {
                                redrawbm();
                            }
                        }
                    }
                    this.is2zhi = false;
                    this.isactionmoved = false;
                    this.isactiondown = false;
                    break;
                case 2:
                    float f = 10.0f;
                    if (this.is2zhi || motionEvent.getPointerCount() != 1 || !this.isactiondown) {
                        if (motionEvent.getPointerCount() == 2) {
                            this.is2zhi = true;
                            float x = motionEvent2.getX(0) - motionEvent2.getX(1);
                            float y = motionEvent2.getY(0) - motionEvent2.getY(1);
                            float sqrt = (float) Math.sqrt((double) ((x * x) + (y * y)));
                            float f2 = this.actiomovejl;
                            if (f2 != 0.0f) {
                                if (f2 > 0.0f && ((double) (sqrt / f2)) > 1.1d) {
                                    if (!this.iscanfangda) {
                                        if (this.wzcfangdatishi == 0) {
                                            Toast.makeText(this, R.string.WZCBBRFD, 0).show();
                                            this.wzcfangdatishi = 1;
                                            break;
                                        }
                                    } else {
                                        float f3 = this.fangdabeishu;
                                        if (f3 < 10.0f) {
                                            double d = (double) f3;
                                            Double.isNaN(d);
                                            this.fangdabeishu = (float) (d + 0.2d);
                                            bmchanged();
                                            this.actiomovejl = sqrt;
                                            this.isactionmoved = true;
                                            break;
                                        }
                                    }
                                } else if (sqrt > 0.0f && ((double) (this.actiomovejl / sqrt)) > 1.1d) {
                                    float f4 = this.fangdabeishu;
                                    if (f4 > 1.0f) {
                                        double d2 = (double) f4;
                                        Double.isNaN(d2);
                                        this.fangdabeishu = (float) (d2 - 0.2d);
                                        if (this.fangdabeishu < 1.0f) {
                                            this.fangdabeishu = 1.0f;
                                        }
                                        bmchanged();
                                        this.actiomovejl = sqrt;
                                        this.isactionmoved = true;
                                        break;
                                    }
                                }
                            } else {
                                this.actiomovejl = sqrt;
                                if (this.seekBar.getVisibility() == 0) {
                                    this.seekBar.setVisibility(4);
                                    break;
                                }
                            }
                        }
                    } else {
                        float f5 = (float) rawX;
                        float f6 = f5 - this.actiondownx;
                        float f7 = (float) rawY;
                        float f8 = f7 - this.actiondowny;
                        float sqrt2 = (float) Math.sqrt((double) ((f6 * f6) + (f8 * f8)));
                        if (!this.isactionmoved) {
                            f = 50.0f;
                        }
                        if (sqrt2 > f) {
                            if (this.seekBar.getVisibility() == 0) {
                                this.seekBar.setVisibility(4);
                            }
                            if (this.fangdabeishu > 1.0f && this.iszdgs && !this.isactionmoved) {
                                Toast.makeText(this, "切换成手动状态,如要回到自动光标跟随请在画面上单指点击不移动1秒以上再松开", 0).show();
                            }
                            this.iszdgs = false;
                            setaddcanvasxyJNI(0 - ((int) ((f6 * ((float) this.bm.getWidth())) / ((float) this.iv.getWidth()))), 0 - ((int) ((f8 * ((float) this.bm.getWidth())) / ((float) this.iv.getWidth()))));
                            this.actiondownx = f5;
                            this.actiondowny = f7;
                            if (this.ispaused) {
                                redrawbm();
                            }
                            this.isactionmoved = true;
                            return true;
                        }
                    }
                    break;
            }
        }
        return true;
    }

    private void drawtimetext(String str) {
        this.timetextpaint.getTextBounds(str, 0, str.length(), this.timetextbounds);
        int width = (this.bm.getWidth() / 2) - (this.timetextbounds.width() / 2);
        this.pauseiconr = this.bm.getHeight() / 10;
        if (this.bm.getWidth() / 10 < this.pauseiconr) {
            this.pauseiconr = this.bm.getWidth() / 10;
        }
        this.mCanvas.drawText(str, (float) width, (float) ((((this.bm.getHeight() / 2) - this.pauseiconr) - this.timetextbounds.height()) - this.timetextbounds.top), this.timetextpaint);
        this.pauseiconr = (this.pauseiconr * this.iv.getWidth()) / this.bm.getWidth();
    }

    /* access modifiers changed from: private */
    public void drawwzcorinf() {
        int i;
        int i2;
        if (this.iszc == 0) {
            String string = getString(R.string.PMLXZJWZC);
            this.timetextpaint.getTextBounds(string, 0, string.length(), this.timetextbounds);
            this.mCanvas.drawText(string, (float) ((this.bm.getWidth() - this.timetextbounds.width()) - ((int) (20.0f / this.fangdabeishu))), (float) ((0 - this.timetextbounds.top) + ((int) (20.0f / this.fangdabeishu))), this.timetextpaint);
            return;
        }
        String str = this.infotext;
        if (str != null && !str.isEmpty()) {
            Paint paint = this.infotextpaint;
            String str2 = this.infotext;
            paint.getTextBounds(str2, 0, str2.length(), this.timetextbounds);
            if (((double) this.fangdabeishu) < 1.1d) {
                i2 = this.infox;
                i = (0 - this.timetextbounds.top) + this.infoy;
            } else {
                i2 = (this.bm.getWidth() - this.timetextbounds.width()) - ((int) (20.0f / this.fangdabeishu));
                i = (0 - this.timetextbounds.top) + ((int) (20.0f / this.fangdabeishu));
            }
            this.mCanvas.drawText(this.infotext, (float) i2, (float) i, this.infotextpaint);
        }
    }

    private void drawresumeico() {
        this.pauseiconr = this.bm.getHeight() / 10;
        if (this.bm.getWidth() / 10 < this.pauseiconr) {
            this.pauseiconr = this.bm.getWidth() / 10;
        }
        Rect rect = new Rect((this.bm.getWidth() / 2) - this.pauseiconr, (this.bm.getHeight() / 2) - this.pauseiconr, (this.bm.getWidth() / 2) + this.pauseiconr, (this.bm.getHeight() / 2) + this.pauseiconr);
        Rect rect2 = new Rect((this.bm.getWidth() / 2) - this.pauseiconr, (this.bm.getHeight() / 2) + this.pauseiconr, (this.bm.getWidth() / 2) + this.pauseiconr, (this.bm.getHeight() / 2) + (this.pauseiconr * 3));
        Bitmap bitmap = this.resumebitmap;
        if (bitmap == null) {
            this.resumevector.setBounds(rect);
            this.resumevector.draw(this.mCanvas);
            this.settingvector.setBounds(rect2);
            this.settingvector.draw(this.mCanvas);
        } else {
            this.mCanvas.drawBitmap(this.resumebitmap, new Rect(0, 0, bitmap.getWidth(), this.resumebitmap.getHeight()), rect, (Paint) null);
            this.mCanvas.drawBitmap(this.settingbitmap, new Rect(0, 0, this.settingbitmap.getWidth(), this.settingbitmap.getHeight()), rect2, (Paint) null);
        }
        this.pauseiconr = (this.pauseiconr * this.iv.getWidth()) / this.bm.getWidth();
    }

    private void drawpauseico() {
        this.pauseiconr = this.bm.getHeight() / 10;
        if (this.bm.getWidth() / 10 < this.pauseiconr) {
            this.pauseiconr = this.bm.getWidth() / 10;
        }
        Rect rect = new Rect((this.bm.getWidth() / 2) - this.pauseiconr, (this.bm.getHeight() / 2) - this.pauseiconr, (this.bm.getWidth() / 2) + this.pauseiconr, (this.bm.getHeight() / 2) + this.pauseiconr);
        Rect rect2 = new Rect((this.bm.getWidth() / 2) - this.pauseiconr, (this.bm.getHeight() / 2) + this.pauseiconr, (this.bm.getWidth() / 2) + this.pauseiconr, (this.bm.getHeight() / 2) + (this.pauseiconr * 3));
        Bitmap bitmap = this.pausebitmap;
        if (bitmap == null) {
            this.pausevector.setBounds(rect);
            this.pausevector.draw(this.mCanvas);
            this.settingvector.setBounds(rect2);
            this.settingvector.draw(this.mCanvas);
        } else {
            this.mCanvas.drawBitmap(this.pausebitmap, new Rect(0, 0, bitmap.getWidth(), this.pausebitmap.getHeight()), rect, (Paint) null);
            this.mCanvas.drawBitmap(this.settingbitmap, new Rect(0, 0, this.settingbitmap.getWidth(), this.settingbitmap.getHeight()), rect2, (Paint) null);
        }
        this.pauseiconr = (this.pauseiconr * this.iv.getWidth()) / this.bm.getWidth();
    }

    /* access modifiers changed from: private */
    public void pausewhenseek() {
        if (!this.ispaused) {
            this.ispaused = true;
            DrawBmp();
            if (this.seekBar.getVisibility() == 0) {
                drawtimeandico(this.ispaused_seekpre, this.nowframei);
            }
            drawwzcorinf();
            this.iv.setImageBitmap(this.bm);
        }
    }

    /* access modifiers changed from: private */
    public void pause() {
        if (!this.ispaused) {
            this.ispaused = true;
            AudioPlayManager audioPlayManager2 = this.audioPlayManager;
            if (audioPlayManager2 != null) {
                audioPlayManager2.ispaused = true;
            }
            getWindow().clearFlags(128);
            this.seekBar.setVisibility(0);
            redrawbm();
            return;
        }
        getWindow().addFlags(128);
        this.starttime = SystemClock.elapsedRealtime() - ((long) (this.nowframei * this.lxframedelay));
        AudioPlayManager audioPlayManager3 = this.audioPlayManager;
        if (audioPlayManager3 != null) {
            audioPlayManager3.ispaused = false;
        }
        this.ispaused = false;
        panLog.wtf("pause", "2");
    }

    public void onPause() {
        if (!this.isstarted) {
            super.onPause();
            return;
        }
        if (!this.ispaused) {
            pause();
        }
        super.onPause();
    }

    public void onResume() {
        if (!this.isstarted) {
            super.onResume();
            return;
        }
        if (this.ispaused) {
            pause();
        }
        super.onResume();
    }

    private String gettimestring(int i) {
        String str;
        int i2 = i / 1000;
        int i3 = i2 / 60;
        if (i3 > 0) {
            str = String.valueOf(i3) + ":";
        } else {
            str = "00:";
        }
        int i4 = i2 % 60;
        if (i4 == 0) {
            return str + "00";
        } else if (i4 < 10) {
            return str + "0" + String.valueOf(i4);
        } else {
            return str + String.valueOf(i4);
        }
    }

    /* access modifiers changed from: private */
    public void closejie() {
        if (this.isstarted) {
            if (this.mCanvas != null) {
                this.mCanvas = null;
            }
            Bitmap bitmap = this.bm;
            if (bitmap != null && !bitmap.isRecycled()) {
                this.bm.recycle();
                this.bm = null;
            }
            AudioPlayManager audioPlayManager2 = this.audioPlayManager;
            if (audioPlayManager2 != null) {
                audioPlayManager2.stopPlay();
                this.audioPlayManager = null;
            }
            this.isstarted = false;
        }
    }

    private void closefile() {
        closejie();
    }

    private int openfile() {
        this.wavziptype = GetwavziptypeFromJNI();
        int GetWidthFromJNI = GetWidthFromJNI();
        int GetHeightFromJNI = GetHeightFromJNI();
        this.lxframecount = GetlxframecountFromJNI();
        this.lxframedelay = GetlxframedelayFromJNI();
        this.nowframei = 0;
        this.kzbeishu = GetkzbeishuJNI();
        this.iszc = getiszcJNI();
        this.infox = getinfoxJNI();
        this.infoy = getinfoyJNI();
        this.infotextfontsize = getinfotextfontsizeJNI();
        this.infotextfontcolor = getinfotextfontcolorJNI();
        this.infotextfontstyle = getinfotextfontstyleJNI();
        int i = this.infotextfontcolor;
        this.infotextfontcolor = ((i & 16711680) >> 16) | ((i & 255) << 16) | (i & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
        this.infotextfontcolor |= ViewCompat.MEASURED_STATE_MASK;
        this.infotextpaint.setColor(this.infotextfontcolor);
        this.infotextpaint.setTextSize((float) ((this.infotextfontsize * 13) / 10));
        this.infotextpaint.setStyle(Paint.Style.FILL);
        byte[] bArr = getinfotextJNI();
        if (bArr != null) {
            try {
                this.infotext = new String(bArr, "GBK");
            } catch (UnsupportedEncodingException unused) {
            }
        }
        byte[] bArr2 = getinfotextfontnameJNI();
        if (bArr2 != null) {
            try {
                this.infotextfontname = new String(bArr2, "GBK");
            } catch (UnsupportedEncodingException unused2) {
            }
        }
        this.gbzqpaint = new Paint();
        int i2 = getgbzqcolorbhJNI();
        this.gbzqpaint.setColor((i2 == 1 ? 16776960 : i2 == 2 ? MotionEventCompat.ACTION_POINTER_INDEX_MASK : i2 == 3 ? 255 : i2 == 4 ? 16711680 : 0) | ((((10000 - getgbzqtmdJNI()) * 256) / 10000) << 24));
        this.gbzqpaint.setStyle(Paint.Style.FILL);
        this.leftclickpaint = new Paint();
        this.leftclickpaint.setColor(SupportMenu.CATEGORY_MASK);
        this.leftclickpaint.setStrokeWidth(3.0f);
        this.leftclickpaint.setStyle(Paint.Style.STROKE);
        this.rightclickpaint = new Paint();
        this.rightclickpaint.setColor(InputDeviceCompat.SOURCE_ANY);
        this.rightclickpaint.setStrokeWidth(3.0f);
        this.rightclickpaint.setStyle(Paint.Style.STROKE);
        float f = this.fangdabeishu;
        if (f > 0.0f) {
            int i3 = (int) (((float) GetWidthFromJNI) / f);
            int i4 = (this.screenHeight * i3) / this.screenWidth;
            if (i4 <= GetHeightFromJNI) {
                GetHeightFromJNI = i4;
            }
            if (i3 <= GetWidthFromJNI) {
                GetWidthFromJNI = i3;
            }
        } else if (f == -1.0f) {
            int i5 = (this.screenWidth * GetHeightFromJNI) / this.screenHeight;
            if (i5 > GetWidthFromJNI) {
                i5 = GetWidthFromJNI;
            }
            this.fangdabeishu = (float) (GetWidthFromJNI / i5);
            GetWidthFromJNI = i5;
        }
        xiuzhengfontsize();
        this.bm = Bitmap.createBitmap(GetWidthFromJNI, GetHeightFromJNI, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.bm);
        redrawbm();
        if (this.wavziptype > 0) {
            this.audioPlayManager = new AudioPlayManager();
            this.wavfilename = getApplicationContext().getFilesDir().getAbsolutePath();
            int i6 = this.wavziptype;
            if (i6 == 5) {
                this.wavfilename += "/1.mp3";
            } else if (i6 == 7) {
                this.wavfilename += "/1.aac";
            }
            this.audioPlayManager.startPlay(this.wavfilename, 0);
            if (this.ispaused) {
                this.audioPlayManager.ispaused = true;
            }
        }
        this.iv.setOnTouchListener(this);
        this.seekBar.setMax(this.lxframecount - 1);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                panLog.wtf("MainActivity onStartTrackingTouch", "1");
                MainActivity mainActivity = MainActivity.this;
                mainActivity.isseekbarmoving = true;
                mainActivity.ispaused_seekpre = mainActivity.ispaused;
                if (!MainActivity.this.ispaused) {
                    MainActivity.this.pausewhenseek();
                }
                MainActivity.this.seekbarhandler.post(MainActivity.this.seekbarr);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                panLog.wtf("MainActivity onStopTrackingTouch1", String.valueOf(MainActivity.this.nowframei) + " " + String.valueOf(seekBar.getProgress()));
                if (!MainActivity.this.isseekbarmoving) {
                    onStartTrackingTouch(seekBar);
                    panLog.wtf("MainActivity xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", "0");
                }
                if (MainActivity.this.wavziptype > 0) {
                    int progress = seekBar.getProgress();
                    MainActivity.this.audioPlayManager.stopPlay();
                    panLog.wtf("MainActivity stopPlay ed", String.valueOf(progress));
                    AudioPlayManager unused = MainActivity.this.audioPlayManager = new AudioPlayManager();
                    MainActivity.this.audioPlayManager.startPlay(MainActivity.this.wavfilename, MainActivity.this.lxframedelay * progress);
                    panLog.wtf("MainActivity startPlay ed", String.valueOf(progress));
                    if (MainActivity.this.ispaused_seekpre) {
                        MainActivity.this.audioPlayManager.ispaused = true;
                    }
                }
                if (MainActivity.this.nowframei != seekBar.getProgress()) {
                    int unused2 = MainActivity.this.nowframei = seekBar.getProgress();
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.DrawGotoFrameBmp(mainActivity.nowframei);
                } else {
                    MainActivity.this.DrawBmp();
                }
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity2.drawtimeandico(mainActivity2.ispaused_seekpre, MainActivity.this.nowframei);
                MainActivity.this.drawwzcorinf();
                MainActivity.this.iv.setImageBitmap(MainActivity.this.bm);
                panLog.wtf("MainActivity onStopTrackingTouch2", String.valueOf(MainActivity.this.nowframei));
                MainActivity mainActivity3 = MainActivity.this;
                mainActivity3.isseekbarmoving = false;
                mainActivity3.showseekbarframei = mainActivity3.nowframei;
                if (!MainActivity.this.ispaused_seekpre) {
                    MainActivity.this.pause();
                    panLog.wtf("MainActivity onStopTrackingTouch3", String.valueOf(MainActivity.this.nowframei));
                }
            }
        });
        this.isstarted = true;
        this.starttime = SystemClock.elapsedRealtime();
        this.handler.postDelayed(this.r, (long) this.lxframedelay);
        return 1;
    }

    /* access modifiers changed from: private */
    public int openjie(int i) {
        int openjieJNI = openjieJNI(i);
        if (openjieJNI <= 0) {
            return openjieJNI;
        }
        this.nowjieindex = i;
        return openfile();
    }

    /* access modifiers changed from: private */
    public int openfile(String str, String str2) {
        int SetFilenameFromJNI = SetFilenameFromJNI(str, str2);
        if (SetFilenameFromJNI < 0) {
            return SetFilenameFromJNI;
        }
        this.jiecount = SetFilenameFromJNI;
        this.nowjieindex = 0;
        return openfile();
    }

    private void showMMInputDialog() {
        final EditText editText = new EditText(this);
        editText.setInputType(129);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入播放密码");
        builder.setView(editText);
        builder.setPositiveButton("确定", (DialogInterface.OnClickListener) null);
        final AlertDialog create = builder.create();
        create.show();
        Button button = create.getButton(-1);
        this.lserrorcount = 0;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String unused = MainActivity.this.mimastring = editText.getText().toString();
                MainActivity mainActivity = MainActivity.this;
                if (mainActivity.testmimaJNI(mainActivity.mimastring) == 0) {
                    if (MainActivity.this.lserrorcount >= 3) {
                        MainActivity.this.textView_ts.setText("密码错误,无法播放");
                        create.dismiss();
                    }
                    editText.setText(BuildConfig.FLAVOR);
                    Toast.makeText(MainActivity.this, "密码错误,请重新输入", 0).show();
                    MainActivity.this.lserrorcount++;
                    return;
                }
                create.dismiss();
                MainActivity mainActivity2 = MainActivity.this;
                int access$900 = mainActivity2.openfile(mainActivity2.lxefilename, MainActivity.this.mimastring);
                if (access$900 <= 0) {
                    MainActivity.this.textView_ts.setText("打开文件失败，返回码:" + String.valueOf(access$900));
                }
            }
        });
    }

    private void showPopupMenu(View view) {
        MenuItem menuItem;
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popupmenu);
        boolean z = true;
        if (this.isgbzq) {
            popupMenu.getMenu().findItem(R.id.menu_gbzq).setChecked(true);
        }
        if (this.isdjtx) {
            popupMenu.getMenu().findItem(R.id.menu_djtx).setChecked(true);
        }
        if (this.iszdgs) {
            popupMenu.getMenu().findItem(R.id.menu_zdgs).setChecked(true);
        }
        Menu menu = popupMenu.getMenu();
        int i = R.id.menu_jie1;
        int i2 = 0;
        menu.findItem(R.id.menu_jie1).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie2).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie3).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie4).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie5).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie6).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie7).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie8).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie9).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_jie10).setVisible(false);
        if (this.jiecount > 1) {
            while (i2 < this.jiecount && i2 < 10) {
                int i3 = i2 + 1;
                if (i3 == z) {
                    menuItem = popupMenu.getMenu().findItem(i);
                } else if (i3 == 2) {
                    menuItem = popupMenu.getMenu().findItem(R.id.menu_jie2);
                } else if (i3 == 3) {
                    menuItem = popupMenu.getMenu().findItem(R.id.menu_jie3);
                } else if (i3 == 4) {
                    menuItem = popupMenu.getMenu().findItem(R.id.menu_jie4);
                } else if (i3 == 5) {
                    menuItem = popupMenu.getMenu().findItem(R.id.menu_jie5);
                } else if (i3 == 6) {
                    menuItem = popupMenu.getMenu().findItem(R.id.menu_jie6);
                } else if (i3 == 7) {
                    menuItem = popupMenu.getMenu().findItem(R.id.menu_jie7);
                } else if (i3 == 8) {
                    menuItem = popupMenu.getMenu().findItem(R.id.menu_jie8);
                } else if (i3 == 9) {
                    menuItem = popupMenu.getMenu().findItem(R.id.menu_jie9);
                } else {
                    menuItem = i3 == 10 ? popupMenu.getMenu().findItem(R.id.menu_jie10) : null;
                }
                menuItem.setVisible(z);
                byte[] gejiecaptionJNI = gejiecaptionJNI(i2);
                if (gejiecaptionJNI != null) {
                    try {
                        String str = new String(gejiecaptionJNI, "GBK");
                        menuItem.setTitle("第" + String.valueOf(i3) + "节 " + str);
                    } catch (UnsupportedEncodingException unused) {
                    }
                }
                i2 = i3;
                z = true;
                i = R.id.menu_jie1;
            }
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                String str = menuItem.isChecked() ? "不选中" : "选中";
                switch (menuItem.getItemId()) {
                    case R.id.menu_djtx:
                        MainActivity mainActivity = MainActivity.this;
                        mainActivity.isdjtx = true ^ mainActivity.isdjtx;
                        Toast.makeText(MainActivity.this.getApplicationContext(), str + menuItem.getTitle(), 0).show();
                        break;
                    case R.id.menu_fangda:
                        MainActivity.this.fangdabeishu *= 2.0f;
                        if (MainActivity.this.fangdabeishu > 10.0f) {
                            MainActivity.this.fangdabeishu = 10.0f;
                        }
                        MainActivity.this.bmchanged();
                        Toast.makeText(MainActivity.this, "可以直接在画面上用双指缩放", 0).show();
                        break;
                    case R.id.menu_gbzq:
                        MainActivity mainActivity2 = MainActivity.this;
                        mainActivity2.isgbzq = true ^ mainActivity2.isgbzq;
                        Toast.makeText(MainActivity.this.getApplicationContext(), str + menuItem.getTitle(), 0).show();
                        break;
                    case R.id.menu_help2:
                        Intent intent = new Intent();
                        intent.setClassName(BuildConfig.APPLICATION_ID, "com.tlxsoft.lxeplayerapplication.HelpActivity");
                        MainActivity.this.startActivity(intent);
                        break;
                    case R.id.menu_jie1:
                        MainActivity.this.closejie();
                        int unused = MainActivity.this.openjie(0);
                        break;
                    case R.id.menu_jie10:
                        MainActivity.this.closejie();
                        int unused2 = MainActivity.this.openjie(9);
                        break;
                    case R.id.menu_jie2:
                        MainActivity.this.closejie();
                        int unused3 = MainActivity.this.openjie(1);
                        break;
                    case R.id.menu_jie3:
                        MainActivity.this.closejie();
                        int unused4 = MainActivity.this.openjie(2);
                        break;
                    case R.id.menu_jie4:
                        MainActivity.this.closejie();
                        int unused5 = MainActivity.this.openjie(3);
                        break;
                    case R.id.menu_jie5:
                        MainActivity.this.closejie();
                        int unused6 = MainActivity.this.openjie(4);
                        break;
                    case R.id.menu_jie6:
                        MainActivity.this.closejie();
                        int unused7 = MainActivity.this.openjie(5);
                        break;
                    case R.id.menu_jie7:
                        MainActivity.this.closejie();
                        int unused8 = MainActivity.this.openjie(6);
                        break;
                    case R.id.menu_jie8:
                        MainActivity.this.closejie();
                        int unused9 = MainActivity.this.openjie(7);
                        break;
                    case R.id.menu_jie9:
                        MainActivity.this.closejie();
                        int unused10 = MainActivity.this.openjie(8);
                        break;
                    case R.id.menu_zdgs:
                        MainActivity mainActivity3 = MainActivity.this;
                        mainActivity3.iszdgs = !mainActivity3.iszdgs;
                        if (!MainActivity.this.iszdgs) {
                            Toast.makeText(MainActivity.this, "在画面上单指拖动画面取消自动光标跟随", 0).show();
                            break;
                        } else {
                            MainActivity.this.setaddcanvasxyJNI(0, 0);
                            Toast.makeText(MainActivity.this, "快捷设置自动光标跟随：请在画面上单指点击不移动1秒以上再松开", 0).show();
                            break;
                        }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String str;
        Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
        if (query == null) {
            return uri.getPath();
        }
        if (query.moveToFirst()) {
            str = query.getString(query.getColumnIndex("_data"));
        } else {
            str = uri.getPath();
        }
        query.close();
        return str;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        String realPathFromURI;
        super.onCreate(bundle);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.screenWidth = displayMetrics.widthPixels;
        this.screenHeight = displayMetrics.heightPixels;
        this.preorientation = getResources().getConfiguration().orientation;
        if (this.preorientation == 1) {
            this.fangdabeishu = -1.0f;
        }
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        this.ispaused = false;
        setContentView((int) R.layout.activity_main);
        this.textView_ts = (TextView) findViewById(R.id.textView_ts);
        String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/jm2.lxe";
        String stringExtra = getIntent().getStringExtra("extra_data");
        if (stringExtra != null) {
            this.isintentopen = true;
            str = stringExtra;
        }
        Intent intent = getIntent();
        if ("android.intent.action.VIEW".equals(intent.getAction()) && (realPathFromURI = getRealPathFromURI(this, intent.getData())) != null) {
            str = realPathFromURI;
        }
        this.seekBar = (SeekBar) findViewById(R.id.seekBar);
        this.iv = (ImageView) findViewById(R.id.imageView);
        this.timetextpaint = new Paint();
        this.timetextpaint.setColor(SupportMenu.CATEGORY_MASK);
        this.timetextpaint.setTextSize(60.0f);
        this.timetextpaint.setStyle(Paint.Style.FILL);
        this.timetextbounds = new Rect();
        this.pausebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause_name);
        this.resumebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play2_name);
        this.settingbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_setting_name);
        if (this.pausebitmap == null) {
            this.pausevector = ContextCompat.getDrawable(this, R.drawable.ic_pause_name);
            this.resumevector = ContextCompat.getDrawable(this, R.drawable.ic_play2_name);
            this.settingvector = ContextCompat.getDrawable(this, R.drawable.ic_setting_name);
        }
        this.infotextpaint = new Paint();
        this.lxefilename = str;
        int openfile = openfile(this.lxefilename, (String) null);
        if (openfile == -100) {
            showMMInputDialog();
        } else if (openfile <= 0) {
            this.textView_ts.setText("打开文件失败，返回码:" + String.valueOf(openfile));
            this.iv.setVisibility(4);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawgbzq(int i) {
        if (this.isgbzq) {
            this.mCanvas.drawCircle((float) getcursorxJNI(), (float) getcursoryJNI(), 16.0f, this.gbzqpaint);
        }
        if (this.isdjtx) {
            int i2 = getgbzqtxtypeJNI(i);
            if (i2 == 1 || i2 == 3) {
                this.mCanvas.drawCircle((float) getcursorxJNI(), (float) getcursoryJNI(), 16.0f, this.leftclickpaint);
                if (i2 == 3) {
                    this.mCanvas.drawCircle((float) getcursorxJNI(), (float) getcursoryJNI(), 22.0f, this.leftclickpaint);
                }
            } else if (i2 == 2) {
                this.mCanvas.drawCircle((float) getcursorxJNI(), (float) getcursoryJNI(), 16.0f, this.rightclickpaint);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean DrawNextFrameBmp(int i) {
        if (!DrawNextFrameBmpFromJNI(this.bm, i, false)) {
            return false;
        }
        drawgbzq(i);
        DrawcursorFromJNI(this.bm);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean DrawBmp() {
        if (!DrawBmpFromJNI(this.bm, false)) {
            return false;
        }
        drawgbzq(this.nowframei);
        DrawcursorFromJNI(this.bm);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean DrawGotoFrameBmp(int i) {
        if (!DrawGotoFrameBmpFromJNI(this.bm, i, false)) {
            return false;
        }
        drawgbzq(i);
        DrawcursorFromJNI(this.bm);
        return true;
    }
}
