package formationapps.helper.stickers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.formationapps.nameart.R;

import java.io.IOException;

public class StickerConst {
    public static final int[] progressColors;
    public static RelativeLayout drawingLayout;
    public static String stickerJsonString;
    public static String[] fonts;
    public static boolean keyboardShown;
    public static TextAndStickerView selectedSticker;

    static {
        keyboardShown = false;
        drawingLayout = null;
        selectedSticker = null;
        progressColors = new int[]{-1, -2500135, -5723992, -11250604, -14277082,
                ViewCompat.MEASURED_STATE_MASK, -6094848, SupportMenu.CATEGORY_MASK, -43776, -29696, -23808, -25009, -12439,
                InputDeviceCompat.SOURCE_ANY, -107, -3342545, -7667968, -16727803, -16017354, -16752098, -16758223, -10431616,
                -9708289, -16661505, -16741633, -16750849, -16761157, -16766287, -12255007, -12451712, -10747726, -6408449,
                -4325145, -3538782, -49447, -53388, -37999, -29256, -8820409, -9948416, -12574976};
        String[] strArr = new String[31];
        strArr[1] = "Acidic.ttf";
        strArr[2] = "Akronim-Regular.ttf";
        strArr[3] = "Angelina.ttf";
        strArr[4] = "Atreyu.otf";
        strArr[5] = "Bangers.ttf";
        strArr[6] = "Calligraffitti-Regular.ttf";
        strArr[7] = "CevicheOne-Regular.ttf";
        strArr[8] = "CheekyRabbit.ttf";
        strArr[9] = "Condiment-Regular.ttf";
        strArr[10] = "Courgette.ttf";
        strArr[11] = "DancingScript-Bold.ttf";
        strArr[12] = "DancingScript.ttf";
        strArr[13] = "Eater-Regular.ttf";
        strArr[14] = "Escuela.ttf";
        strArr[15] = "FaracoHand.ttf";
        strArr[16] = "FreebooterScript.ttf";
        strArr[17] = "HomemadeApple.ttf";
        strArr[18] = "Infinity.ttf";
        strArr[19] = "Inkburrow.ttf";
        strArr[20] = "JoshHandwriting.ttf";
        strArr[21] = "Journal.ttf";
        strArr[22] = "LobsterTwo.ttf";
        strArr[23] = "MarcelleScript.ttf";
        strArr[24] = "Metropolis1920.otf";
        strArr[25] = "Playball.ttf";
        strArr[26] = "SoulMission.ttf";
        strArr[27] = "StraightBaller.ttf";
        strArr[28] = "TheMockingBird.ttf";
        strArr[29] = "Tommaso.ttf";
        strArr[30] = "Trochut.ttf";
        //fonts = strArr;
        String[] strArr2 = new String[9];
        strArr2[0] = "Alpenkreuzer.ttf";
        strArr2[1] = "Angles Octagon.ttf";
        strArr2[2] = "CFGoliathDemo-Regular.ttf";
        strArr2[3] = "Chunkfive.ttf";
        strArr2[4] = "EventShark.ttf";
        strArr2[5] = "forgetmenot.ttf";
        strArr2[6] = "girlnextdoor.ttf";
        strArr2[7] = "LuxuryImport.ttf";
        strArr2[8] = "WhisperADream.ttf";
        fonts = new String[strArr.length + strArr2.length];
        for (int i = 0; i < strArr2.length; i++) {
            fonts[i + 1] = strArr2[i];
        }
        for (int i = 0; i < strArr.length - 1; i++) {
            fonts[i] = strArr[i + 1];
        }
    }

    public static void initFont(Activity activity) {
        try {
            fonts = activity.getAssets().list("fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void keyboardToggler(Activity activity, View view, boolean show) {
        if (view != null) {
            InputMethodManager var3 = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (show) {
                var3.showSoftInput(view, 1);
            } else {
                var3.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static Drawable getThumbDrawable(Resources resource, int colorIndex) {
        Options option = new Options();
        if (VERSION.SDK_INT >= 11) {
            option.inMutable = true;
        }
        option.inPreferredConfig = Config.ARGB_8888;
        Bitmap baseBitmap = BitmapFactory.decodeResource(resource, R.drawable.ic_hint_fg, option);
        Bitmap btm = Bitmap.createBitmap(baseBitmap.getWidth() + 0, baseBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(btm);
        if (colorIndex < progressColors.length) {
            Paint var5 = new Paint();
            var5.setColor(progressColors[colorIndex]);
            var5.setStyle(Style.FILL);
            canvas.drawRect(new RectF((float) ((baseBitmap.getWidth() / 5) + 0), (float) (baseBitmap.getHeight() / 6), (float) (((baseBitmap.getWidth() * 8) / 10) + 0), (float) ((baseBitmap.getHeight() * 60) / 94)), var5);
        }
        canvas.drawBitmap(baseBitmap, new Rect(0, 0, baseBitmap.getWidth(), baseBitmap.getHeight()), new RectF(0.0f, 0.0f, (float) (baseBitmap.getWidth() + 0), (float) baseBitmap.getHeight()), null);
        return new BitmapDrawable(resource, btm);
    }

    public static void invalidateStickers() {
        if (drawingLayout != null && drawingLayout.getChildCount() != 0) {
            for (int var1 = 0; var1 < drawingLayout.getChildCount(); var1++) {
                View var0 = drawingLayout.getChildAt(var1);
                if (var0 instanceof TextAndStickerView) {
                    ((TextAndStickerView) var0).setActive(false);
                }
            }
        }
    }

    public static void responseStickers(boolean disable) {
        if (drawingLayout != null && drawingLayout.getChildCount() != 0) {
            for (int var1 = 0; var1 < drawingLayout.getChildCount(); var1++) {
                View var0 = drawingLayout.getChildAt(var1);
                if (var0 instanceof TextAndStickerView) {
                    ((TextAndStickerView) var0).setActive(disable);
                    ((TextAndStickerView) var0).setIsResponse(disable);
                }
            }
        }
    }
}
