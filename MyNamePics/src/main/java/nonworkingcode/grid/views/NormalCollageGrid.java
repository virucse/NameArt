package nonworkingcode.grid.views;

import java.util.ArrayList;
import java.util.List;

public class NormalCollageGrid {
    static final boolean f54j = false;
    public int left;
    public int top;
    public int right;
    public int bottom;
    public float f60f;
    public float f61g;
    public float f62h;
    public float f63i;
    private int f55a;

    public NormalCollageGrid(int left, int top, int right, int bottom) {
        this.f55a = 0;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.f55a = 0;
    }

    public static List<NormalCollageGrid> m37a(int var0) {
        ArrayList<NormalCollageGrid> var1 = new ArrayList<NormalCollageGrid>();
        switch (var0) {
            case 0 /*0*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, -1));
                return var1;
            case 1 /*1*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, -1));
                return var1;
            case 2 /*2*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, -1));
                return var1;
            case 3 /*3*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, -1));
                return var1;
            case 4 /*4*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(0, 0, -1, -1));
                return var1;
            case 5 /*5*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, 0, -1, -1));
                return var1;
            case 6 /*6*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, -1));
                return var1;
            case 7 /*7*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, -1));
                return var1;
            case 8/*8*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, -1));
                return var1;
            case 9 /*9*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, 2, -1));
                var1.add(new NormalCollageGrid(2, -1, -1, -1));
                return var1;
            case 10/*10*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, 2));
                var1.add(new NormalCollageGrid(-1, 2, -1, -1));
                return var1;
            case 11 /*11*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, -1));
                var1.add(new NormalCollageGrid(1, 0, -1, -1));
                return var1;
            case 12 /*12*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(0, 0, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, -1));
                return var1;
            case 13 /*13*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(0, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, -1));
                return var1;
            case 14 /*14*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, 1));
                var1.add(new NormalCollageGrid(1, 1, -1, -1));
                return var1;
            case 15 /*15*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, 1, -1, -1));
                return var1;
            case 16 /*16*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(0, 0, -1, 1));
                var1.add(new NormalCollageGrid(0, 1, -1, -1));
                return var1;
            case 17 /*17*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(-1, 1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, -1));
                return var1;
            case 18 /*18*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, 0, 1, -1));
                var1.add(new NormalCollageGrid(1, 0, -1, -1));
                return var1;
            case 19/*19*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, -1));
                return var1;
            case 20 /*20*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, 1));
                var1.add(new NormalCollageGrid(0, 1, -1, 2));
                var1.add(new NormalCollageGrid(0, 2, -1, -1));
                return var1;
            case 21 /*21*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(-1, 1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, 2));
                var1.add(new NormalCollageGrid(0, 2, -1, -1));
                return var1;
            case 22 /*22*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, -1));
                var1.add(new NormalCollageGrid(1, 0, 2, -1));
                var1.add(new NormalCollageGrid(2, 0, -1, -1));
                return var1;
            case 23 /*23*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 2, -1));
                var1.add(new NormalCollageGrid(2, 0, -1, -1));
                return var1;
            case 24/*24*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(0, 0, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, 1));
                var1.add(new NormalCollageGrid(1, 1, -1, -1));
                return var1;
            case 25 /*25*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 1));
                var1.add(new NormalCollageGrid(0, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, -1));
                return var1;
            case 26 /*26*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(0, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, 1, -1, -1));
                return var1;
            case 27/*27*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, 1));
                var1.add(new NormalCollageGrid(1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, -1));
                return var1;
            case 28 /*28*/:
                var1.add(new NormalCollageGrid(-1, -4, 0, -4));
                var1.add(new NormalCollageGrid(0, -4, 1, -4));
                var1.add(new NormalCollageGrid(1, -4, -1, -4));
                return var1;
            case 29 /*29*/:
                var1.add(new NormalCollageGrid(-4, -1, -4, 0));
                var1.add(new NormalCollageGrid(-4, 0, -4, 1));
                var1.add(new NormalCollageGrid(-4, 1, -4, -1));
                return var1;
            case 30 /*30*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -3));
                var1.add(new NormalCollageGrid(0, -2, 1, -2));
                var1.add(new NormalCollageGrid(1, -3, -1, -1));
                return var1;
            case 31 /*31*/:
                var1.add(new NormalCollageGrid(-1, -1, -3, 0));
                var1.add(new NormalCollageGrid(-2, 0, -2, 1));
                var1.add(new NormalCollageGrid(-3, 1, -1, -1));
                return var1;
            case 32 /*32*/:
                var1.add(new NormalCollageGrid(-1, -4, 0, -4));
                var1.add(new NormalCollageGrid(0, -4, -1, -4));
                return var1;
            case 33 /*33*/:
                var1.add(new NormalCollageGrid(-4, -1, -4, 0));
                var1.add(new NormalCollageGrid(-4, 0, -4, -1));
                return var1;
            case 34 /*34*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, 0));
                var1.add(new NormalCollageGrid(1, 0, -1, 1));
                var1.add(new NormalCollageGrid(1, 1, -1, -1));
                return var1;
            case 35/*35*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(-1, 1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, -1));
                return var1;
            case 36 /*36*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 0, -1));
                var1.add(new NormalCollageGrid(0, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, 1, -1, -1));
                return var1;
            case 37 /*37*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, -1));
                return var1;
            case 38 /*38*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(0, 0, -1, 1));
                var1.add(new NormalCollageGrid(0, 1, -1, 2));
                var1.add(new NormalCollageGrid(0, 2, -1, -1));
                return var1;
            case 39 /*39*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(-1, 1, 0, 2));
                var1.add(new NormalCollageGrid(-1, 2, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, -1, -1));
                return var1;
            case 40 /*40*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, 0, 1, -1));
                var1.add(new NormalCollageGrid(1, 0, 2, -1));
                var1.add(new NormalCollageGrid(2, 0, -1, -1));
                return var1;
            case 41 /*41*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(1, -1, 2, 0));
                var1.add(new NormalCollageGrid(2, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, -1));
                return var1;
            case 42 /*42*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(0, 0, 1, 1));
                var1.add(new NormalCollageGrid(0, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, -1));
                return var1;
            case 43/*43*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(0, 0, 1, 1));
                var1.add(new NormalCollageGrid(1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, -1));
                return var1;
            case 44 /*44*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 1));
                var1.add(new NormalCollageGrid(0, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, 2));
                var1.add(new NormalCollageGrid(1, 2, -1, -1));
                return var1;
            case 45 /*45*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, 1));
                var1.add(new NormalCollageGrid(1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 2, -1));
                var1.add(new NormalCollageGrid(2, 1, -1, -1));
                return var1;
            case 46 /*46*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(0, 0, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, 1));
                var1.add(new NormalCollageGrid(1, 1, -1, 2));
                var1.add(new NormalCollageGrid(1, 2, -1, -1));
                return var1;
            case 47 /*47*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(-1, 1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 2));
                var1.add(new NormalCollageGrid(0, 2, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, -1));
                return var1;
            case 48 /*48*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(0, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, 1, 2, -1));
                var1.add(new NormalCollageGrid(2, 1, -1, -1));
                return var1;
            case 49 /*49*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 2, 1));
                var1.add(new NormalCollageGrid(2, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, -1));
                return var1;
            case 50 /*50*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 1));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(0, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, 1, 2, -1));
                var1.add(new NormalCollageGrid(2, 1, -1, -1));
                return var1;
            case 51 /*51*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(0, -1, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, 1, 2, -1));
                var1.add(new NormalCollageGrid(2, 1, -1, -1));
                return var1;
            case 52 /*52*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, -1, 1));
                var1.add(new NormalCollageGrid(1, 1, -1, 2));
                var1.add(new NormalCollageGrid(1, 2, -1, -1));
                return var1;
            case 53 /*53*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 1));
                var1.add(new NormalCollageGrid(-1, 1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 2));
                var1.add(new NormalCollageGrid(1, -1, -1, 2));
                var1.add(new NormalCollageGrid(0, 2, -1, -1));
                return var1;
            case 54 /*54*/:
                var1.add(new NormalCollageGrid(-1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, 2));
                var1.add(new NormalCollageGrid(-1, 2, -1, 3));
                var1.add(new NormalCollageGrid(-1, 3, -1, -1));
                return var1;
            case 55 /*55*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, 2, -1));
                var1.add(new NormalCollageGrid(2, -1, 3, -1));
                var1.add(new NormalCollageGrid(3, -1, -1, -1));
                return var1;
            case 56 /*56*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 1));
                var1.add(new NormalCollageGrid(0, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, -1, 2, 2));
                var1.add(new NormalCollageGrid(1, 2, 2, -1));
                var1.add(new NormalCollageGrid(2, -1, -1, 3));
                var1.add(new NormalCollageGrid(2, 3, -1, -1));
                return var1;
            case 57 /*57*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, 1));
                var1.add(new NormalCollageGrid(1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 2, 2));
                var1.add(new NormalCollageGrid(2, 1, -1, 2));
                var1.add(new NormalCollageGrid(-1, 2, 3, -1));
                var1.add(new NormalCollageGrid(3, 2, -1, -1));
                return var1;
            case 58 /*58*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 1, -1));
                var1.add(new NormalCollageGrid(1, 0, 2, 2));
                var1.add(new NormalCollageGrid(1, 2, 2, -1));
                var1.add(new NormalCollageGrid(2, 0, 3, 3));
                var1.add(new NormalCollageGrid(2, 3, 3, -1));
                var1.add(new NormalCollageGrid(3, 0, -1, 4));
                var1.add(new NormalCollageGrid(3, 4, -1, -1));
                return var1;
            case 59 /*59*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, 4));
                var1.add(new NormalCollageGrid(0, -1, 1, 1));
                var1.add(new NormalCollageGrid(0, 1, 1, 4));
                var1.add(new NormalCollageGrid(1, -1, 2, 2));
                var1.add(new NormalCollageGrid(1, 2, 2, 4));
                var1.add(new NormalCollageGrid(2, -1, -1, 3));
                var1.add(new NormalCollageGrid(2, 3, -1, 4));
                var1.add(new NormalCollageGrid(-1, 4, 3, -1));
                var1.add(new NormalCollageGrid(3, 4, -1, -1));
                return var1;
            case 60 /*60*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(-1, 0, 0, -1));
                var1.add(new NormalCollageGrid(0, -1, 1, 1));
                var1.add(new NormalCollageGrid(1, -1, -1, 1));
                var1.add(new NormalCollageGrid(0, 1, 2, 2));
                var1.add(new NormalCollageGrid(2, 1, -1, 2));
                var1.add(new NormalCollageGrid(0, 2, 3, 3));
                var1.add(new NormalCollageGrid(3, 2, -1, 3));
                var1.add(new NormalCollageGrid(0, 3, 4, -1));
                var1.add(new NormalCollageGrid(4, 3, -1, -1));
                return var1;
            case 61 /*61*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 4, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, 1));
                var1.add(new NormalCollageGrid(1, 0, 4, 1));
                var1.add(new NormalCollageGrid(-1, 1, 2, 2));
                var1.add(new NormalCollageGrid(2, 1, 4, 2));
                var1.add(new NormalCollageGrid(-1, 2, 3, -1));
                var1.add(new NormalCollageGrid(3, 2, 4, -1));
                var1.add(new NormalCollageGrid(4, -1, -1, 3));
                var1.add(new NormalCollageGrid(4, 3, -1, -1));
                return var1;
            case 62 /*62*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 2, 1));
                var1.add(new NormalCollageGrid(2, 0, 3, 1));
                var1.add(new NormalCollageGrid(3, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, 4, -1));
                var1.add(new NormalCollageGrid(4, 1, 5, -1));
                var1.add(new NormalCollageGrid(5, 1, -1, -1));
                return var1;
            case 63 /*63*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(1, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 2, 1));
                var1.add(new NormalCollageGrid(2, 0, 3, 1));
                var1.add(new NormalCollageGrid(3, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, -1));
                return var1;
            case 64 /*64*/:
                var1.add(new NormalCollageGrid(-1, -1, 0, 0));
                var1.add(new NormalCollageGrid(0, -1, 1, 0));
                var1.add(new NormalCollageGrid(1, -1, 2, 0));
                var1.add(new NormalCollageGrid(2, -1, -1, 0));
                var1.add(new NormalCollageGrid(-1, 0, 1, 1));
                var1.add(new NormalCollageGrid(1, 0, -1, 1));
                var1.add(new NormalCollageGrid(-1, 1, -1, -1));
                return var1;
            default:
                return null;
        }
    }

    public String toString() {
        return "left:" + this.left + ",top:" + this.top + ",right:" + this.right + ",bottom:" + this.bottom;
    }

    public Boolean m38a(float var1, float var2, float var3, List<CollageRatioManager> var4, List<CollageRatioManager> var5) {
        var3 *= Math.max(var1, var2);
        if (this.f55a == 0) {
            if (this.top >= 0) {
                if (var5 == null || this.top >= var5.size()) {
                    throw new AssertionError();
                }
                this.f61g = ((var5.get(this.top)).ratio * var2) + (var3 / 2.0f);
            } else if (this.top == -1) {
                this.f61g = var3;
            } else {
                this.f61g = var3 - ((var2 * 0.05f) * ((float) this.top));
            }
            if (this.bottom < 0) {
                if (this.bottom == -1) {
                    this.f63i = var3;
                } else {
                    this.f63i = var3 - ((var2 * 0.05f) * ((float) this.bottom));
                }
            } else if (var5 == null || this.bottom >= var5.size()) {
                throw new AssertionError();
            } else {
                this.f63i = ((1.0f - (var5.get(this.bottom)).ratio) * var2) + (var3 / 2.0f);
            }
            if (this.left >= 0) {
                if (var4 == null || this.left >= var4.size()) {
                    throw new AssertionError();
                }
                this.f60f = ((var4.get(this.left)).ratio * var1) + (var3 / 2.0f);
            } else if (this.left == -1) {
                this.f60f = var3;
            } else {
                this.f60f = var3 - ((var1 * 0.05f) * ((float) this.left));
            }
            if (this.right < 0) {
                if (this.right == -1) {
                    this.f62h = var3;
                } else {
                    this.f62h = var3 - ((var1 * 0.05f) * ((float) this.right));
                }
            } else if (var4 == null || this.right >= var4.size()) {
                throw new AssertionError();
            } else {
                this.f62h = ((1.0f - (var4.get(this.right)).ratio) * var1) + (var3 / 2.0f);
            }
        }
        if ((var1 - this.f60f) - this.f62h < 0.0f || (var2 - this.f61g) - this.f63i < 0.0f) {
            return false;
        }
        return true;
    }
}
