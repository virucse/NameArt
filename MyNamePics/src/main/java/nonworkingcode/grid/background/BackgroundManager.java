package nonworkingcode.grid.background;

import android.content.Context;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.v4.content.ContextCompat;

import com.downloader.FileUtils;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackgroundManager {
    public static int MODE_COLOR;
    public static int MODE_DECO;
    public static int MODE_GRADIENT;
    public static int MODE_MATERIAL;
    public static int MODE_PATTERN;
    public static int MODE_TEXTURE;

    static {
        MODE_COLOR = 0;
        MODE_GRADIENT = 1;
        MODE_PATTERN = 2;
        MODE_DECO = 3;
        MODE_TEXTURE = 4;
        MODE_MATERIAL = 5;
    }

    private final Context mContext;
    private final int mMode;
    private final List<IBgResource> resList;

    public BackgroundManager(Context context, int mode) {
        this.resList = new ArrayList();
        this.mContext = context;
        this.resList.clear();
        this.mMode = mode;
        if (this.mMode == MODE_COLOR) {
            this.resList.add(initItem("color_cd0", 17170443));
            this.resList.add(initItem("color_cd30", 17170444));
            this.resList.add(initItem("color_cd1", R.color.color_cd1));
            this.resList.add(initItem("color_cd2", R.color.color_cd2));
            this.resList.add(initItem("color_cd3", R.color.color_cd3));
            this.resList.add(initItem("color_cd4", R.color.color_cd4));
            this.resList.add(initItem("color_cd5", R.color.color_cd5));
            this.resList.add(initItem("color_cd6", R.color.color_cd6));
            this.resList.add(initItem("color_cd7", R.color.color_cd7));
            this.resList.add(initItem("color_cd8", R.color.color_cd8));
            this.resList.add(initItem("color_cd9", R.color.color_cd9));
            this.resList.add(initItem("color_cd10", R.color.color_cd10));
            this.resList.add(initItem("color_cd11", R.color.color_cd11));
            this.resList.add(initItem("color_cd12", R.color.color_cd12));
            this.resList.add(initItem("color_cd13", R.color.color_cd13));
            this.resList.add(initItem("color_cd14", R.color.color_cd14));
            this.resList.add(initItem("color_cd15", R.color.color_cd15));
            this.resList.add(initItem("color_cd16", R.color.color_cd16));
            this.resList.add(initItem("color_cd17", R.color.color_cd17));
            this.resList.add(initItem("color_cd18", R.color.color_cd18));
            this.resList.add(initItem("color_cd19", R.color.color_cd19));
            this.resList.add(initItem("color_cd20", R.color.color_cd20));
            this.resList.add(initItem("color_cd21", R.color.color_cd21));
            this.resList.add(initItem("color_cd22", R.color.color_cd22));
            this.resList.add(initItem("color_cd23", R.color.color_cd23));
            this.resList.add(initItem("color_cd24", R.color.color_cd24));
            this.resList.add(initItem("color_cd25", R.color.color_cd25));
            this.resList.add(initItem("color_cd27", R.color.color_cd26));
            this.resList.add(initItem("color_cd28", R.color.color_cd27));
            this.resList.add(initItem("color_cd29", R.color.color_cd28));
            this.resList.add(initItem("color_cd31", R.color.color_cd29));
        } else if (this.mMode == MODE_GRADIENT) {
            this.resList.add(initGradientItem("gradient1", R.color.gradient_1_1, R.color.gradient_1_2, Orientation.LEFT_RIGHT, 0));
            this.resList.add(initGradientItem("gradient2", R.color.gradient_2_1, R.color.gradient_2_2, Orientation.LEFT_RIGHT, 0));
            this.resList.add(initGradientItem("gradient3", R.color.gradient_3_1, R.color.gradient_3_2, Orientation.TR_BL, 0));
            this.resList.add(initGradientItem("gradient4", R.color.gradient_4_1, R.color.gradient_4_2, Orientation.TOP_BOTTOM, 0));
            this.resList.add(initGradientItem("gradient5", R.color.gradient_5_1, R.color.gradient_5_2, Orientation.TL_BR, 0));
            this.resList.add(initGradientItem("gradient6", R.color.gradient_6_1, R.color.gradient_6_2, Orientation.TOP_BOTTOM, 0));
            this.resList.add(initGradientItem("gradient7", R.color.gradient_7_1, R.color.gradient_7_2, Orientation.TL_BR, 0));
            this.resList.add(initGradientItem("gradient8", R.color.gradient_8_1, R.color.gradient_8_2, Orientation.TOP_BOTTOM, 0));
            this.resList.add(initGradientItem("gradient9", R.color.gradient_9_1, R.color.gradient_9_2, Orientation.LEFT_RIGHT, 0));
            this.resList.add(initGradientItem("gradient10", R.color.gradient_10_1, R.color.gradient_10_2, Orientation.TL_BR, 0));
            this.resList.add(initGradientItem("gradient11", R.color.gradient_11_1, R.color.gradient_11_2, Orientation.TOP_BOTTOM, 0));
            this.resList.add(initGradientItem("gradient12", R.color.gradient_12_1, R.color.gradient_12_2, Orientation.TL_BR, 0));
            this.resList.add(initGradientItem("gradient13", R.color.gradient_13_1, R.color.gradient_13_2, Orientation.LEFT_RIGHT, 0));
            this.resList.add(initGradientItem("gradient14", R.color.gradient_14_1, R.color.gradient_14_2, Orientation.TL_BR, 0));
            this.resList.add(initGradientItem("gradient15", R.color.gradient_15_1, R.color.gradient_15_2, Orientation.TR_BL, 1));
            this.resList.add(initGradientItem("gradient16", R.color.gradient_16_1, R.color.gradient_16_2, Orientation.TOP_BOTTOM, 0));
            this.resList.add(initGradientItem("gradient17", R.color.gradient_17_1, R.color.gradient_17_2, Orientation.LEFT_RIGHT, 0));
            this.resList.add(initGradientItem("gradient18", R.color.gradient_18_1, R.color.gradient_18_2, Orientation.LEFT_RIGHT, 0));
            this.resList.add(initGradientItem("gradient19", R.color.gradient_19_1, R.color.gradient_19_2, Orientation.TOP_BOTTOM, 0));
            this.resList.add(initGradientItem("gradient20", R.color.gradient_20_1, R.color.gradient_20_2, Orientation.TR_BL, 0));
            this.resList.add(initGradientItem("gradient21", R.color.gradient_21_1, R.color.gradient_21_2, Orientation.TL_BR, 0));
            this.resList.add(initGradientItem("gradient22", R.color.gradient_22_1, R.color.gradient_22_2, Orientation.TL_BR, 0));
            this.resList.add(initGradientItem("gradient23", R.color.gradient_23_1, R.color.gradient_23_2, Orientation.TR_BL, 0));
            this.resList.add(initGradientItem("gradient24", R.color.gradient_24_1, R.color.gradient_24_2, Orientation.LEFT_RIGHT, 0));
            this.resList.add(initGradientItem("gradient25", R.color.gradient_25_1, R.color.gradient_25_2, Orientation.TR_BL, 0));
            this.resList.add(initGradientItem("gradient26", R.color.gradient_26_1, R.color.gradient_26_2, Orientation.TR_BL, 0));
            this.resList.add(initGradientItem("gradient27", R.color.gradient_27_1, R.color.gradient_27_2, Orientation.TOP_BOTTOM, 0));
            this.resList.add(initGradientItem("gradient28", R.color.gradient_28_1, R.color.gradient_28_2, Orientation.TL_BR, 0));
            this.resList.add(initGradientItem("gradient29", R.color.gradient_29_1, R.color.gradient_29_2, Orientation.LEFT_RIGHT, 0));
            this.resList.add(initGradientItem("gradient30", R.color.gradient_30_1, R.color.gradient_30_2, Orientation.TOP_BOTTOM, 0));
        } else if (this.mMode == MODE_DECO) {
            File rootFile = FileUtils.getDataDir(mContext, AppUtils.BACKGROUNDDECO);
            String rootpath = rootFile.getAbsolutePath();
            String[] files = rootFile.list();
            for (int i = 0; i < files.length; i++) {
                File f1 = new File(rootpath + "/" + files[i]);
                this.resList.add(initAssetItem("a1" + i, 1, f1, f1));
            }
        } else if (this.mMode == MODE_MATERIAL) {
            File rootFile = FileUtils.getDataDir(mContext, AppUtils.BACKGROUNDMATERIAL);
            String rootpath = rootFile.getAbsolutePath();
            String[] files = rootFile.list();
            for (int i = 0; i < files.length; i++) {
                File f2 = new File(rootpath + "/" + files[i]);
                this.resList.add(initAssetItem("a2" + i, 1, f2, f2));
            }
        } else if (this.mMode == MODE_TEXTURE) {
            File rootFile = FileUtils.getDataDir(mContext, AppUtils.BACKGROUNDTEXTURE);
            String rootpath = rootFile.getAbsolutePath();
            String[] files = rootFile.list();
            for (int i = 0; i < files.length; i++) {
                File f3 = new File(rootpath + "/" + files[i]);
                this.resList.add(initAssetItem("a3" + i, 1, f3, f3));
            }
        } else if (this.mMode == MODE_PATTERN) {
            File rootFile = FileUtils.getDataDir(mContext, AppUtils.BACKGROUNDPATTERN);
            String rootpath = rootFile.getAbsolutePath();
            String[] files = rootFile.list();
            for (int i = 0; i < files.length; i++) {
                File f4 = new File(rootpath + "/" + files[i]);
                this.resList.add(initAssetItem("a4" + i, 1, f4, f4));
            }
        }
    }

    public int getCount() {
        return this.resList.size();
    }

    public int getMode() {
        return this.mMode;
    }

    public IBgResource getRes(String name) {
        for (int i = 0; i < this.resList.size(); i++) {
            IBgResource res = (IBgResource) this.resList.get(i);
            if (res.getName().equals(name)) {
                return res;
            }
        }
        return null;
    }

    //this should be used when images are store in assets
    protected PattenResource initAssetItem(String name, String iconfilename, String bgfilename) {
        PattenResource res = new PattenResource();
        res.setContext(this.mContext);
        res.setName(name);
        res.setType(0);
        res.setIconName(iconfilename);
        res.setImageName(bgfilename);
        return res;
    }

    //this should be used when images are store in assets
    protected PattenResource initAssetItem(String name, int type, String iconfilename, String bgfilename) {
        PattenResource res = new PattenResource();
        res.setContext(this.mContext);
        res.setName(name);
        res.setType(type);
        res.setIconName(iconfilename);
        res.setImageName(bgfilename);
        return res;
    }

    protected PattenResource initAssetItem(String name, int type, File iconfilename, File bgfilename) {
        PattenResource res = new PattenResource();
        res.setContext(this.mContext);
        res.setName(name);
        res.setType(type);
        res.setIconFileName(iconfilename);
        res.setImageFileName(bgfilename);
        return res;
    }

    protected ColorResource initItem(String name, int color) {
        ColorResource var3 = new ColorResource();
        var3.setContext(this.mContext);
        var3.setName(name);
        var3.setColorID(color);
        return var3;
    }

    public boolean isRes(String var1) {
        return false;
    }

    protected GradientResource initGradientItem(String name, int color1, int color2, Orientation orientation, int type) {
        color1 = ContextCompat.getColor(this.mContext, color1);
        color2 = ContextCompat.getColor(this.mContext, color2);
        GradientResource res = new GradientResource();
        res.setName(name);
        res.setColors(new int[]{color1, color2});
        res.setOrientation(orientation);
        res.setDefaultOrientation(orientation);
        res.setGraType(type);
        return res;
    }

    public List<IBgResource> getList() {
        return this.resList;
    }

    public void dispose() {
        this.resList.clear();
    }
}
