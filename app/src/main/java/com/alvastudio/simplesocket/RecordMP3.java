package com.alvastudio.simplesocket;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.example.androidlame.AndroidLame;
import com.example.androidlame.LameBuilder;

import java.io.IOException;
import java.io.OutputStream;

import static com.alvastudio.simplesocket.Config.Config.IN_SAMPLE_RATE;

public class RecordMP3 extends Thread {


    private OutputStream mOutputStream;
    private boolean isRecording;


    RecordMP3(OutputStream outputStream){
        this.mOutputStream = outputStream;
    }

    @Override
    public void run() {
        super.run();
    }

    private void startRecording() {

        int minBuffer = AudioRecord.getMinBufferSize(IN_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        AudioRecord audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.VOICE_COMMUNICATION, IN_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, minBuffer * 2);


        short[] buffer = new short[IN_SAMPLE_RATE * 2 * 5];

        byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

        AndroidLame androidLame = new LameBuilder()
                .setInSampleRate(IN_SAMPLE_RATE)
                .setOutChannels(1)
                .setOutBitrate(32)
                .setOutSampleRate(IN_SAMPLE_RATE)
                .build();


        audioRecord.startRecording();

        int bytesRead;

        while (isRecording) {

            bytesRead = audioRecord.read(buffer, 0, minBuffer);

            if (bytesRead > 0) {

                int bytesEncoded = androidLame.encode(buffer, buffer, bytesRead, mp3buffer);

                if (bytesEncoded > 0) {
                    try {

                        mOutputStream.write(mp3buffer, 0, bytesEncoded);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        int outputMp3buf = androidLame.flush(mp3buffer);

        if (outputMp3buf > 0) {
            try {

                mOutputStream.write(mp3buffer, 0, outputMp3buf);

                mOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        audioRecord.stop();
        audioRecord.release();
        androidLame.close();
    }


    @Override
    public boolean isInterrupted() {
        isRecording = false;
        return super.isInterrupted();
    }
}
