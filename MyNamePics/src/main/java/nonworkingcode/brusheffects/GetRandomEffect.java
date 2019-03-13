package nonworkingcode.brusheffects;

import android.content.Context;

import java.util.Random;

public class GetRandomEffect {
    public int f2023a;
    public int color;
    public int f2025c;
    public int f2026d;
    public float f2027e;
    public float f2028f;

    public GetRandomEffect(Context context, float f, float f2, BrushEffectLoad brushEffectLoad) {
        String key = brushEffectLoad.getFileName();
        boolean randomeColor = BrushPref.getIsBrushRandomColor(context, key);
        if (randomeColor) {
            this.color = GetColorList.getRandomColor();
        } else {
            color = BrushPref.getColor(context, key);
        }

        this.f2027e = f;
        this.f2028f = f2;
        Random random = new Random();
        this.f2025c = random.nextInt(brushEffectLoad.getRow() * brushEffectLoad.getColumn());
        this.f2026d = random.nextInt(45);
        this.f2023a = random.nextInt(2);
    }
}
