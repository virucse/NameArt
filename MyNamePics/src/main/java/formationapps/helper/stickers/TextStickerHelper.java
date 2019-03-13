package formationapps.helper.stickers;

import android.content.Context;
import android.graphics.Typeface;

import com.formationapps.nameart.helper.TypefacesUtils;

/**
 * Created by caliber fashion on 8/14/2017.
 */

public class TextStickerHelper {
    public static void setFont(int index, Context context) {

        if (index > 0) {
            Typeface typeface = TypefacesUtils.get(context, "fonts/" + StickerConst.fonts[index]);
        } else {

        }
    }
}
