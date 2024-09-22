package com.tlxsoft.lxeplayerapplication;

import android.media.AudioTrack;
import android.os.Process;
import android.os.SystemClock;

/* compiled from: MainActivity */
class AudioPlayManager {
    private static volatile AudioPlayManager mInstance = null;
    private static int mMode = 1;
    private static final int mStreamType = 3;
    /* access modifiers changed from: private */
    public AudioDecManager decManager;
    /* access modifiers changed from: private */
    public boolean isStart = false;
    public boolean ispaused = false;
    public int mAudioFormat = 2;
    /* access modifiers changed from: private */
    public AudioTrack mAudioTrack;
    public int mBitspersample;
    public int mChannelConfig = 4;
    public int mChannelCount;
    private int mMinBufferSize;
    private Thread mPlayThread;
    public int mSampleRateInHz = 22050;
    Runnable playRunnable = new Runnable() {
        public void run() {
            try {
                Process.setThreadPriority(-19);
                while (!AudioPlayManager.this.isStart = false) {
                    if (AudioPlayManager.this.ispaused) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        byte[] pCMData = AudioPlayManager.this.decManager.getPCMData();
                        if (pCMData == null) {
                            try {
                                Thread.sleep(50);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        } else {
                            int state = AudioPlayManager.this.mAudioTrack.getState();
                            AudioTrack unused = AudioPlayManager.this.mAudioTrack;
                            if (state == 0) {
                                AudioPlayManager.this.initData();
                            }
                            int state2 = AudioPlayManager.this.mAudioTrack.getState();
                            AudioTrack unused2 = AudioPlayManager.this.mAudioTrack;
                            if (state2 != 3) {
                                AudioPlayManager.this.mAudioTrack.play();
                            }
                            AudioPlayManager.this.mAudioTrack.write(pCMData, 0, pCMData.length);
                            int unused3 = AudioPlayManager.this.playedwavlength = AudioPlayManager.this.playedwavlength + pCMData.length;
                            double access$400 = (double) AudioPlayManager.this.playedwavlength;
                            Double.isNaN(access$400);
                            double d = access$400 * 1.0d;
                            double d2 = (double) (((AudioPlayManager.this.mSampleRateInHz * AudioPlayManager.this.mChannelCount) * AudioPlayManager.this.mBitspersample) / 8);
                            Double.isNaN(d2);
                            double d3 = (d / d2) * 1000.0d;
                            AudioPlayManager.this.starttime = SystemClock.elapsedRealtime() - ((long) d3);
                        }
                    }
                }
                panLog.wtf("syplay isStart = false", "1");
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    };
    /* access modifiers changed from: private */
    public int playedwavlength;
    public long starttime = 0;

    /* access modifiers changed from: private */
    public void initData() {
        this.mMinBufferSize = AudioTrack.getMinBufferSize(this.mSampleRateInHz, this.mChannelConfig, this.mAudioFormat);
        this.mAudioTrack = new AudioTrack(3, this.mSampleRateInHz, this.mChannelConfig, this.mAudioFormat, this.mMinBufferSize, mMode);
    }

    public static AudioPlayManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioPlayManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioPlayManager();
                }
            }
        }
        return mInstance;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:6|7|8|9|10) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x001d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void destroyThread() {
        /*
            r4 = this;
            r0 = 0
            r1 = 0
            r4.isStart = r0     // Catch:{ Exception -> 0x0024 }
            java.lang.Thread r0 = r4.mPlayThread     // Catch:{ Exception -> 0x0024 }
            if (r0 == 0) goto L_0x001f
            java.lang.Thread$State r0 = java.lang.Thread.State.RUNNABLE     // Catch:{ Exception -> 0x0024 }
            java.lang.Thread r2 = r4.mPlayThread     // Catch:{ Exception -> 0x0024 }
            java.lang.Thread$State r2 = r2.getState()     // Catch:{ Exception -> 0x0024 }
            if (r0 != r2) goto L_0x001f
            r2 = 500(0x1f4, double:2.47E-321)
            java.lang.Thread.sleep(r2)     // Catch:{ Exception -> 0x001d }
            java.lang.Thread r0 = r4.mPlayThread     // Catch:{ Exception -> 0x001d }
            r0.interrupt()     // Catch:{ Exception -> 0x001d }
            goto L_0x001f
        L_0x001d:
            r4.mPlayThread = r1     // Catch:{ Exception -> 0x0024 }
        L_0x001f:
            r4.mPlayThread = r1     // Catch:{ Exception -> 0x0024 }
            goto L_0x0028
        L_0x0022:
            r0 = move-exception
            goto L_0x002b
        L_0x0024:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0022 }
        L_0x0028:
            r4.mPlayThread = r1
            return
        L_0x002b:
            r4.mPlayThread = r1
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tlxsoft.lxeplayerapplication.AudioPlayManager.destroyThread():void");
    }

    private void startThread() {
        destroyThread();
        this.isStart = true;
        if (this.mPlayThread == null) {
            this.mPlayThread = new Thread(this.playRunnable);
            this.mPlayThread.start();
        }
    }

    public void startPlay(String str, int i) {
        try {
            this.decManager = new AudioDecManager();
            this.decManager.startDec(str, i);
            this.mSampleRateInHz = this.decManager.samplerate;
            this.mChannelCount = this.decManager.channelcount;
            this.mBitspersample = this.decManager.bitspersample;
            if (this.decManager.channelcount == 1) {
                this.mChannelConfig = 4;
            } else {
                this.mChannelConfig = 12;
            }
            this.mAudioFormat = this.decManager.bittype;
            double d = (double) (this.mSampleRateInHz * this.mChannelCount * (this.mBitspersample / 8));
            double d2 = (double) i;
            Double.isNaN(d2);
            Double.isNaN(d);
            this.playedwavlength = (int) (d * (d2 / 1000.0d));
            initData();
            this.ispaused = false;
            this.starttime = 0;
            startThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        if (this.decManager != null) {
            try {
                destroyThread();
                if (this.mAudioTrack != null) {
                    if (this.mAudioTrack.getState() == 1) {
                        this.mAudioTrack.stop();
                    }
                    if (this.mAudioTrack != null) {
                        this.mAudioTrack.release();
                    }
                }
                if (this.decManager != null) {
                    this.decManager.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
