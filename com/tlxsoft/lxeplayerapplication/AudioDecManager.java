package com.tlxsoft.lxeplayerapplication;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.view.Surface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/* compiled from: MainActivity */
class AudioDecManager {
    private static final String TAG = "AudioCodec";
    private static volatile AudioDecManager mInstance;
    public int bitspersample;
    public int bittype;
    public int channelcount;
    private ArrayList<byte[]> chunkPCMDataContainer;
    /* access modifiers changed from: private */
    public boolean codeOver = false;
    Runnable decRunnable = new Runnable() {
        public void run() {
            while (!AudioDecManager.this.codeOver) {
                if (AudioDecManager.this.hcpmcdatasize / (((AudioDecManager.this.samplerate * AudioDecManager.this.channelcount) * (AudioDecManager.this.bittype == 2 ? 16 : 8)) / 8) > 2) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception unused) {
                    }
                } else {
                    AudioDecManager.this.srcAudioFormatToPCM();
                }
            }
            AudioDecManager.this.threadisquit = true;
        }
    };
    private MediaCodec.BufferInfo decodeBufferInfo;
    private ByteBuffer[] decodeInputBuffers;
    private ByteBuffer[] decodeOutputBuffers;
    private long decodeSize;
    ByteBuffer directbuffer;
    private String dstPath;
    private String encodeType;
    private long fileTotalSize;
    /* access modifiers changed from: private */
    public int hcpmcdatasize = 0;
    public int inbufsize;
    public boolean isstarted = false;
    int kstart = 0;
    private Thread mDecThread;
    private MediaCodec mediaDecode;
    private MediaExtractor mediaExtractor;
    int[] mp3samplesizes = {0, 0, 0};
    int mp3samplesizescount = 0;
    int mp3samplesizesindex = 0;
    public int samplerate;
    private String srcPath;
    boolean threadisquit = false;
    int wavziptype = 0;
    private ArrayList<byte[]> ysDataContainer;

    public native int GetdataFromJNI(Object obj);

    public native int GetwavbitspersampleFromJNI();

    public native int GetwavchannelcountFromJNI();

    public native int GetwavdataFromJNI(Object obj);

    public native int GetwavsamplerateFromJNI();

    public native int GetwavziptypeFromJNI();

    public native int resetstarttimeJNI(int i);

    public static AudioDecManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioDecManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioDecManager();
                }
            }
        }
        return mInstance;
    }

    private boolean initMediaDecode(int i) {
        try {
            this.wavziptype = GetwavziptypeFromJNI();
            if (this.wavziptype <= 2) {
                this.directbuffer = ByteBuffer.allocateDirect(22050);
                this.samplerate = GetwavsamplerateFromJNI();
                this.channelcount = GetwavchannelcountFromJNI();
                this.bitspersample = GetwavbitspersampleFromJNI();
                if (GetwavbitspersampleFromJNI() == 8) {
                    this.bittype = 3;
                } else {
                    this.bittype = 2;
                }
                resetstarttimeJNI(i);
                this.kstart = 0;
                return true;
            }
            if (this.wavziptype == 5) {
                this.directbuffer = ByteBuffer.allocateDirect(500000);
                resetstarttimeJNI(i);
                int GetdataFromJNI = GetdataFromJNI(this.directbuffer);
                byte[] bArr = new byte[GetdataFromJNI];
                this.directbuffer.get(bArr, 0, GetdataFromJNI);
                File file = new File(this.srcPath);
                file.deleteOnExit();
                try {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(bArr);
                    fileOutputStream.close();
                    this.kstart = resetstarttimeJNI(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else if (this.wavziptype == 7) {
                resetstarttimeJNI(i);
                this.directbuffer = ByteBuffer.allocateDirect(22050);
                int GetdataFromJNI2 = GetdataFromJNI(this.directbuffer);
                byte[] bArr2 = new byte[GetdataFromJNI2];
                this.directbuffer.get(bArr2, 0, GetdataFromJNI2);
                File file2 = new File(this.srcPath);
                file2.deleteOnExit();
                try {
                    file2.createNewFile();
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
                    fileOutputStream2.write(bArr2);
                    int i2 = 0;
                    while (true) {
                        if (i2 >= 20) {
                            break;
                        }
                        this.directbuffer.clear();
                        int GetdataFromJNI3 = GetdataFromJNI(this.directbuffer);
                        if (GetdataFromJNI3 <= 0) {
                            break;
                        }
                        byte[] bArr3 = new byte[GetdataFromJNI3];
                        this.directbuffer.get(bArr3, 0, GetdataFromJNI3);
                        fileOutputStream2.write(bArr3);
                        i2++;
                    }
                    fileOutputStream2.close();
                    this.kstart = resetstarttimeJNI(i);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return false;
                }
            }
            this.mediaExtractor = new MediaExtractor();
            this.mediaExtractor.setDataSource(this.srcPath);
            int i3 = 0;
            while (true) {
                if (i3 >= this.mediaExtractor.getTrackCount()) {
                    break;
                }
                MediaFormat trackFormat = this.mediaExtractor.getTrackFormat(i3);
                this.samplerate = trackFormat.getInteger("sample-rate");
                this.channelcount = trackFormat.getInteger("channel-count");
                this.bittype = trackFormat.containsKey("pcm-encoding") ? trackFormat.getInteger("pcm-encoding") : 2;
                if (this.bittype == 2) {
                    this.bitspersample = 16;
                } else if (this.bittype == 3) {
                    this.bitspersample = 8;
                }
                String string = trackFormat.getString("mime");
                if (string.startsWith("audio")) {
                    this.mediaExtractor.selectTrack(i3);
                    this.mediaDecode = MediaCodec.createDecoderByType(string);
                    this.mediaDecode.configure(trackFormat, (Surface) null, (MediaCrypto) null, 0);
                    break;
                }
                i3++;
            }
            MediaCodec mediaCodec = this.mediaDecode;
            if (mediaCodec == null) {
                panLog.wtf(TAG, "create mediaDecode failed");
                return false;
            }
            mediaCodec.start();
            this.decodeInputBuffers = this.mediaDecode.getInputBuffers();
            this.decodeOutputBuffers = this.mediaDecode.getOutputBuffers();
            this.decodeBufferInfo = new MediaCodec.BufferInfo();
            ByteBuffer byteBuffer = this.decodeInputBuffers[0];
            this.inbufsize = byteBuffer.capacity();
            if (this.wavziptype == 5) {
                ByteBuffer allocate = ByteBuffer.allocate(byteBuffer.capacity());
                this.mp3samplesizesindex = 0;
                this.mp3samplesizescount = 1;
                this.mp3samplesizes[this.mp3samplesizesindex] = this.mediaExtractor.readSampleData(allocate, 0);
                while (true) {
                    this.mediaExtractor.advance();
                    allocate.clear();
                    int readSampleData = this.mediaExtractor.readSampleData(allocate, 0);
                    if (readSampleData <= 0) {
                        return false;
                    }
                    int[] iArr = this.mp3samplesizes;
                    if (readSampleData == iArr[0]) {
                        this.mp3samplesizesindex = 0;
                        break;
                    }
                    int i4 = this.mp3samplesizescount;
                    if (i4 >= 3) {
                        return false;
                    }
                    this.mp3samplesizescount = i4 + 1;
                    this.mp3samplesizesindex++;
                    iArr[this.mp3samplesizesindex] = readSampleData;
                }
            }
            MediaExtractor mediaExtractor2 = this.mediaExtractor;
            if (mediaExtractor2 != null) {
                mediaExtractor2.release();
                this.mediaExtractor = null;
            }
            return true;
        } catch (IOException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public void putysData(byte[] bArr) {
        synchronized (AudioDecManager.class) {
            this.ysDataContainer.add(bArr);
        }
    }

    private byte[] getysData(int i) {
        synchronized (AudioDecManager.class) {
            if (this.ysDataContainer.isEmpty()) {
                while (true) {
                    this.directbuffer.clear();
                    int GetdataFromJNI = GetdataFromJNI(this.directbuffer);
                    if (GetdataFromJNI == 0) {
                        break;
                    } else if (this.wavziptype == 7) {
                        byte[] bArr = new byte[GetdataFromJNI];
                        this.directbuffer.get(bArr, 0, GetdataFromJNI);
                        this.ysDataContainer.add(bArr);
                        break;
                    } else if (this.wavziptype == 5) {
                        panLog.wtf("getysData", String.valueOf(GetdataFromJNI) + " " + String.valueOf(this.kstart));
                        int i2 = this.channelcount * 576 * (this.bitspersample / 8);
                        while (this.directbuffer.position() < GetdataFromJNI) {
                            int i3 = this.mp3samplesizes[this.mp3samplesizesindex];
                            this.mp3samplesizesindex++;
                            if (this.mp3samplesizesindex >= this.mp3samplesizescount) {
                                this.mp3samplesizesindex = 0;
                            }
                            if (this.directbuffer.position() + i3 <= GetdataFromJNI) {
                                if (this.kstart >= i2) {
                                    this.directbuffer.position(this.directbuffer.position() + i3);
                                    this.kstart -= i2;
                                } else {
                                    byte[] bArr2 = new byte[i3];
                                    this.directbuffer.get(bArr2);
                                    this.ysDataContainer.add(bArr2);
                                }
                            }
                        }
                        if (this.directbuffer.position() != GetdataFromJNI) {
                            panLog.wtf("getysData", "mp3 error");
                        }
                        panLog.wtf("getysData count", String.valueOf(this.ysDataContainer.size()));
                    }
                }
            }
            if (this.ysDataContainer.isEmpty()) {
                return null;
            }
            byte[] bArr3 = this.ysDataContainer.get(0);
            this.ysDataContainer.remove(bArr3);
            return bArr3;
        }
    }

    private void putPCMData(byte[] bArr) {
        synchronized (AudioDecManager.class) {
            this.hcpmcdatasize += bArr.length;
            this.chunkPCMDataContainer.add(bArr);
            if (!this.isstarted) {
                this.isstarted = true;
                panLog.wtf("putPCMData first", String.valueOf(bArr.length));
            }
            panLog.wtf("putPCMData", String.valueOf(bArr.length));
        }
    }

    public byte[] getPCMData() {
        synchronized (AudioDecManager.class) {
            if (this.chunkPCMDataContainer.isEmpty()) {
                return null;
            }
            byte[] bArr = this.chunkPCMDataContainer.get(0);
            this.hcpmcdatasize -= bArr.length;
            this.chunkPCMDataContainer.remove(bArr);
            return bArr;
        }
    }

    /* access modifiers changed from: private */
    public void srcAudioFormatToPCM() {
        int i = this.wavziptype;
        if (i != 0) {
            if (i <= 2) {
                this.directbuffer.clear();
                int GetwavdataFromJNI = GetwavdataFromJNI(this.directbuffer);
                if (GetwavdataFromJNI > 0) {
                    byte[] bArr = new byte[GetwavdataFromJNI];
                    this.directbuffer.get(bArr, 0, GetwavdataFromJNI);
                    putPCMData(bArr);
                    this.isstarted = true;
                    return;
                }
                return;
            }
            panLog.wtf("srcAudioFormatToPCM", "1");
            int i2 = 0;
            while (i2 < this.decodeInputBuffers.length - 1) {
                try {
                    Thread.sleep(5);
                } catch (Exception unused) {
                }
                int dequeueInputBuffer = this.mediaDecode.dequeueInputBuffer(100);
                if (dequeueInputBuffer >= 0) {
                    ByteBuffer byteBuffer = this.decodeInputBuffers[dequeueInputBuffer];
                    byteBuffer.clear();
                    byte[] bArr2 = getysData(byteBuffer.capacity());
                    if (bArr2 != null) {
                        if (bArr2 != null) {
                            int length = bArr2.length;
                            byteBuffer.put(bArr2, 0, length);
                            this.mediaDecode.queueInputBuffer(dequeueInputBuffer, 0, length, 0, 0);
                            this.decodeSize += (long) length;
                        }
                        i2++;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            panLog.wtf("srcAudioFormatToPCM", "2");
            int dequeueOutputBuffer = this.mediaDecode.dequeueOutputBuffer(this.decodeBufferInfo, 100);
            while (dequeueOutputBuffer >= 0) {
                ByteBuffer byteBuffer2 = this.decodeOutputBuffers[dequeueOutputBuffer];
                int i3 = this.decodeBufferInfo.size;
                int i4 = this.kstart;
                if (i3 <= i4) {
                    this.kstart = i4 - this.decodeBufferInfo.size;
                } else if (i4 > 0) {
                    byteBuffer2.get(new byte[i4], 0, i4);
                    byte[] bArr3 = new byte[(this.decodeBufferInfo.size - this.kstart)];
                    byteBuffer2.get(bArr3);
                    byteBuffer2.clear();
                    putPCMData(bArr3);
                    this.kstart = 0;
                } else if (i4 == 0) {
                    byte[] bArr4 = new byte[this.decodeBufferInfo.size];
                    byteBuffer2.get(bArr4);
                    byteBuffer2.clear();
                    putPCMData(bArr4);
                }
                this.mediaDecode.releaseOutputBuffer(dequeueOutputBuffer, false);
                dequeueOutputBuffer = this.mediaDecode.dequeueOutputBuffer(this.decodeBufferInfo, 10000);
            }
            panLog.wtf("srcAudioFormatToPCM", "3");
        }
    }

    private void startThread() {
        this.codeOver = false;
        if (this.mDecThread == null) {
            this.mDecThread = new Thread(this.decRunnable);
            this.mDecThread.start();
        }
        this.threadisquit = false;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:14|15|16|17|18) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0027 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void destroyThread() {
        /*
            r4 = this;
            r0 = 1
            r1 = 0
            r4.codeOver = r0     // Catch:{ Exception -> 0x002e }
        L_0x0004:
            boolean r0 = r4.threadisquit     // Catch:{ Exception -> 0x002e }
            if (r0 != 0) goto L_0x000e
            r2 = 10
            java.lang.Thread.sleep(r2)     // Catch:{ Exception -> 0x0004 }
            goto L_0x0004
        L_0x000e:
            java.lang.Thread r0 = r4.mDecThread     // Catch:{ Exception -> 0x002e }
            if (r0 == 0) goto L_0x0029
            java.lang.Thread$State r0 = java.lang.Thread.State.RUNNABLE     // Catch:{ Exception -> 0x002e }
            java.lang.Thread r2 = r4.mDecThread     // Catch:{ Exception -> 0x002e }
            java.lang.Thread$State r2 = r2.getState()     // Catch:{ Exception -> 0x002e }
            if (r0 != r2) goto L_0x0029
            r2 = 500(0x1f4, double:2.47E-321)
            java.lang.Thread.sleep(r2)     // Catch:{ Exception -> 0x0027 }
            java.lang.Thread r0 = r4.mDecThread     // Catch:{ Exception -> 0x0027 }
            r0.interrupt()     // Catch:{ Exception -> 0x0027 }
            goto L_0x0029
        L_0x0027:
            r4.mDecThread = r1     // Catch:{ Exception -> 0x002e }
        L_0x0029:
            r4.mDecThread = r1     // Catch:{ Exception -> 0x002e }
            goto L_0x0032
        L_0x002c:
            r0 = move-exception
            goto L_0x0035
        L_0x002e:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x002c }
        L_0x0032:
            r4.mDecThread = r1
            return
        L_0x0035:
            r4.mDecThread = r1
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tlxsoft.lxeplayerapplication.AudioDecManager.destroyThread():void");
    }

    private void setPath(String str) throws Exception {
        this.srcPath = str;
    }

    public boolean startDec(String str, int i) {
        try {
            setPath(str);
            this.hcpmcdatasize = 0;
            this.chunkPCMDataContainer = new ArrayList<>();
            this.ysDataContainer = new ArrayList<>();
            if (!initMediaDecode(i)) {
                return false;
            }
            startThread();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void release() {
        try {
            panLog.wtf("dec release", "1");
            destroyThread();
            panLog.wtf("dec release", "2");
            if (this.mediaDecode != null) {
                this.mediaDecode.stop();
                this.mediaDecode.release();
                this.mediaDecode = null;
            }
            panLog.wtf("dec release", "3");
            this.chunkPCMDataContainer.clear();
            this.ysDataContainer.clear();
            panLog.wtf("dec release", "4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopDec() {
        release();
    }
}
