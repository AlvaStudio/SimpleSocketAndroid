package com.alvastudio.simplesocket;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidlame.AndroidLame;
import com.example.androidlame.LameBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordTest extends AppCompatActivity {

    int minBuffer;
    int inSamplerate = 8000;

    String filePath = Environment.getExternalStorageDirectory() + File.separator + "testrecord.mp3";

    boolean isRecording = false;

    AudioRecord audioRecord;
    AndroidLame androidLame;
    FileOutputStream outputStream;

    TextView statusText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_test);

        Button start = findViewById(R.id.startRecording);
        Button stop = findViewById(R.id.stopRecording);

        statusText = findViewById(R.id.statusText);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    new Thread() {
                        @Override
                        public void run() {
                            isRecording = true;
                            startRecording();
                        }
                    }.start();

                } else
                    Toast.makeText(RecordTest.this, "Already recording", Toast.LENGTH_SHORT).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = false;
            }
        });

    }

    private void startRecording() {

        minBuffer = AudioRecord.getMinBufferSize(inSamplerate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);


        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.VOICE_COMMUNICATION, inSamplerate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, minBuffer * 2);


        short[] buffer = new short[inSamplerate * 2 * 5];

        byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

        try {
            outputStream = new FileOutputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        androidLame = new LameBuilder()
                .setInSampleRate(inSamplerate)
                .setOutChannels(1)
                .setOutBitrate(32)
                .setOutSampleRate(inSamplerate)
                .build();


        audioRecord.startRecording();

        int bytesRead = 0;

        while (isRecording) {


            bytesRead = audioRecord.read(buffer, 0, minBuffer);


            if (bytesRead > 0) {


                int bytesEncoded = androidLame.encode(buffer, buffer, bytesRead, mp3buffer);


                if (bytesEncoded > 0) {
                    try {

                        outputStream.write(mp3buffer, 0, bytesEncoded);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }




        int outputMp3buf = androidLame.flush(mp3buffer);


        if (outputMp3buf > 0) {
            try {

                outputStream.write(mp3buffer, 0, outputMp3buf);

                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        audioRecord.stop();
        audioRecord.release();
        androidLame.close();
        isRecording = false;
    }
}
