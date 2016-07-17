package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.ui.Upload.Receiver;

@SuppressWarnings("serial")
public class UploadReceiver  implements Receiver{
    private String fileName;
    private String mtype;
    private boolean sleep;
    private int total = 0;

    public OutputStream receiveUpload(String filename, String mimetype) {
        fileName = filename;
        mtype = mimetype;
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                total++;
                if (sleep && total % 10000 == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mtype;
    }

    public void setSlow(boolean value) {
        sleep = value;
    }
}

