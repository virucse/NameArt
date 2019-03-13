package com.gallery.asynchronusloader;

import java.io.InputStream;
import java.io.OutputStream;

public class LazyUtils {
    public static void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] bytes = new byte[1024];
            while (true) {
                int count = is.read(bytes, 0, 1024);
                if (count != -1) {
                    os.write(bytes, 0, count);
                } else {
                    return;
                }
            }
        } catch (Exception e) {
        }
    }
}
