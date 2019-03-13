package nonworkingcode.grid.panel;

import android.content.Context;

import com.downloader.FileUtils;
import com.formationapps.nameart.helper.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caliber fashion on 9/18/2017.
 */

public class FrameFile {
    private static List<File> fileList = new ArrayList<>();

    public static List<File> getFrameFileList(Context context) {
        if (fileList.size() > 1) {
            return fileList;
        }
        File rootFile = FileUtils.getDataDir(context, AppUtils.BORDERFOLDERNAME);
        String rootPath = rootFile.getAbsolutePath();
        String fileName[] = rootFile.list();
        for (String name : fileName) {
            File f = new File(rootPath + "/" + name);
            fileList.add(f);
        }
        return fileList;
    }
}
