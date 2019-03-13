package nonworkingcode.pip.util;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Environment;

import com.downloader.FileUtils;
import com.formationapps.nameart.helper.AppUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caliber Fashion on 12/16/2016.
 */

public final class PipManager {
    static final List<PipModel> list;
    private static List<Rect> rectList;
    private static String[] pointString;
    private static List<PIPFileModel> fileList;

    static {
        list = new ArrayList();
        fileList = new ArrayList<PIPFileModel>();
        rectList = new ArrayList();
        String str = "65_34,118_111,128_194,97_61,146_345,126_47,302_50,192_53,107_31,77_106,232_168,71_128,63_49,204_180,181_182,74_124,65_116,166_280,93_44,178_244,109_50,83_77,101_85,66_91,63_65,28_62,169_159,346_122,75_236,221_226,52_219,166_106,40_224,68_151,61_33,108_160";
        pointString = str.split(",");
    }

    public static List<PipModel> getModelList() {
        if (list.size() > 0) {
            return list;
        }
        try {
            String[] arrNames = AppUtils.mContext.getAssets().list("pip");
            for (int i = 0; i < arrNames.length; i++) {
                list.add(new PipModel(arrNames[i] + "/icon1.jpg",
                        arrNames[i] + "/cover.png", arrNames[i] + "/mask.png", pointString[i] + ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<PIPFileModel> getFileModelList() {
        if (fileList.size() > 0) {
            return fileList;
        }
        String rootPath = FileUtils.getDataDir(AppUtils.mContext, AppUtils.PIP).getAbsolutePath();
        File file = new File(rootPath);
        if (file.exists() && file.list().length > 3) {
            String subFolders[] = file.list();
            for (int i = 0; i < subFolders.length; i++) {
                File icon = new File(rootPath + "/" + subFolders[i] + "/icon1.jpg");
                File cover = new File(rootPath + "/" + subFolders[i] + "/cover.png");
                File mask = new File(rootPath + "/" + subFolders[i] + "/mask.png");
                fileList.add(new PIPFileModel(icon,
                        cover, mask, pointString[i] + ""));
            }
        }
        return fileList;
    }

    public static void saveData(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String[] arrNames = AppUtils.mContext.getAssets().list("pip");
            for (int i = 0; i < arrNames.length; i++) {
                String str = readTextFromAssets(context, "pip/" + arrNames[i] + "/size.txt");
                stringBuilder.append(str).append(",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String filename = "abc.txt";
            File myFile = new File(Environment
                    .getExternalStorageDirectory(), filename);
            if (!myFile.exists())
                myFile.createNewFile();
            FileOutputStream fos;
            byte[] data = stringBuilder.toString().getBytes();
            try {
                fos = new FileOutputStream(myFile);
                fos.write(data);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readTextFromAssets(Context context, String fileName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            String mLine = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (mLine != null) {
                sb.append(mLine);
                mLine = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<PipModel> createList() {
        if (list.size() > 0) {
            return list;
        }
        rectList.add(new Rect(63, 65, 304, 571));
        //rectList.add(new Rect(30, 55, 422, 551));
        rectList.add(new Rect(68, 151, 328, 606));
        rectList.add(new Rect(146, 345, 403, 597));
        rectList.add(new Rect(28, 62, 481, 592));
        rectList.add(new Rect(169, 159, 575, 455));
        rectList.add(new Rect(302, 50, 555, 557));
        rectList.add(new Rect(77, 106, 472, 501));
        rectList.add(new Rect(40, 224, 334, 576));
        rectList.add(new Rect(346, 122, 557, 561));
        rectList.add(new Rect(118, 111, 494, 543));
        rectList.add(new Rect(97, 61, 454, 505));//10,1
        rectList.add(new Rect(63, 49, 497, 480));//11,10
        rectList.add(new Rect(204, 180, 455, 431));//12,11
        rectList.add(new Rect(221, 226, 417, 374));//,12
        rectList.add(new Rect(108, 160, 461, 450));//,13
        rectList.add(new Rect(181, 182, 433, 434));//,14
        rectList.add(new Rect(128, 194, 475, 603));//,15
        rectList.add(new Rect(74, 124, 516, 502));//,16
        rectList.add(new Rect(65, 116, 434, 416));//,17
        rectList.add(new Rect(166, 280, 412, 453));//,18
        rectList.add(new Rect(93, 44, 499, 447));//,19
        rectList.add(new Rect(126, 47, 486, 442));//,2
        rectList.add(new Rect(178, 244, 388, 384));//,20
        rectList.add(new Rect(52, 219, 396, 560));//,21
        rectList.add(new Rect(109, 50, 546, 536));//,22
        rectList.add(new Rect(83, 77, 509, 389));//,23
        rectList.add(new Rect(101, 85, 422, 585));//,24
        rectList.add(new Rect(66, 91, 339, 364));//,25
        rectList.add(new Rect(192, 53, 587, 553));//,3
        rectList.add(new Rect(166, 106, 447, 290));//,4
        rectList.add(new Rect(107, 31, 473, 274));//,5
        rectList.add(new Rect(58, 29, 334, 609));//,6
        rectList.add(new Rect(232, 168, 383, 593));//,7
        rectList.add(new Rect(71, 128, 539, 549));//,8
        rectList.add(new Rect(75, 236, 480, 431));//,9
        try {
            String[] arrNames = AppUtils.mContext.getAssets().list("imagess/pip");
            for (int i = 0; i < rectList.size(); i++) {
                list.add(new PipModel(1, "pip_" + i, arrNames[i] + "/icon.jpg",
                        PipModel.EResType.ASSET, arrNames[i] + "/cover.png", new PointF(612.0f, 612.0f),
                        (Rect) rectList.get(i), arrNames[i] + "/mask.png"));
            }
        } catch (Exception e) {
        }
        return list;
    }
}
