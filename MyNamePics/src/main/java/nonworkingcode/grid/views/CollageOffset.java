package nonworkingcode.grid.views;


public class CollageOffset {
    public double f0a;
    public double f1b;
    public double f2c;
    public double f3d;

    public CollageOffset(double var1, double var3, double var5, double var7) {
        this.f0a = 0.0d;
        this.f1b = 0.0d;
        this.f2c = 1.0d;
        this.f3d = 1.0d;
        this.f0a = var1;
        this.f1b = var3;
        this.f2c = var5;
        this.f3d = var7;
    }

    public CollageOffset(float var1, float var2, float var3, float var4) {
        this.f0a = 0.0d;
        this.f1b = 0.0d;
        this.f2c = 1.0d;
        this.f3d = 1.0d;
        this.f0a = (double) var1;
        this.f1b = (double) var2;
        this.f2c = (double) var3;
        this.f3d = (double) var4;
    }

    public static CollageOffset m9a(int var0) {
        double var1 = Math.sqrt(2.0d);
        switch (var0) {
            case 0 /*0*/:
                return new CollageOffset(0.0f, 0.0f, (float) 1.0, (float) 1.0);
            case 1 /*1*/:
                return new CollageOffset((float) 1.0, 0.0f, (float) -1, (float) 1.0);
            case 2 /*2*/:
                return new CollageOffset((float) 1.0, (float) 1.0, (float) -1, (float) -1);
            case 3 /*3*/:
                return new CollageOffset(0.0f, (float) 1.0, (float) 1.0, (float) -1);
            case 4 /*4*/:
                return new CollageOffset(0.0d, 0.0d, 1.0d + (0.5d * var1), 1.0d);
            case 5 /*5*/:
                return new CollageOffset(1.0d, 0.0d, -1.0d - (0.5d * var1), 1.0d);
            case 6/*6*/:
                return new CollageOffset(0.5d, 0.5d, 0.0d, -0.5d * var1);
            case 7 /*7*/:
                return new CollageOffset(1.0d, 0.0d, -1.0d, (0.5d * var1) + 1.0d);
            case 8 /*8*/:
                return new CollageOffset(1.0d, 1.0d, -1.0d, -1.0d - (0.5d * var1));
            case 9 /*9*/:
                return new CollageOffset(0.5d, 0.5d, 0.5d * var1, 0.0d);
            case 10 /*10*/:
                return new CollageOffset(1.0d, 1.0d, -1.0d - (0.5d * var1), -1.0d);
            case 11 /*11*/:
                return new CollageOffset(0.0d, 1.0d, 1.0d + (0.5d * var1), -1.0d);
            case 12 /*12*/:
                return new CollageOffset(0.5d, 0.5d, 0.0d, 0.5d * var1);
            case 13 /*13*/:
                return new CollageOffset(0.0d, 0.0d, 1.0d, (0.5d * var1) + 1.0d);
            case 14 /*14*/:
                return new CollageOffset(0.0d, 1.0d, 1.0d, -1.0d - (0.5d * var1));
            case 15 /*15*/:
                return new CollageOffset(0.5d, 0.5d, -0.5d * var1, 0.0d);
            case 16 /*16*/:
                return new CollageOffset(0.5d, 0.0d, -0.5d, 1.0d);
            case 17 /*17*/:
                return new CollageOffset(0.5d, 0.0d, 0.5d, 1.0d);
            case 18 /*18*/:
                return new CollageOffset(1.0d, 0.5d, -1.0d, -0.5d);
            case 19 /*19*/:
                return new CollageOffset(1.0d, 0.5d, -1.0d, 0.5d);
            case 20 /*20*/:
                return new CollageOffset(0.5d, 1.0d, 0.5d, -1.0d);
            case 21 /*21*/:
                return new CollageOffset(0.5d, 1.0d, -0.5d, -1.0d);
            case 22 /*22*/:
                return new CollageOffset(0.0d, 0.5d, 1.0d, 0.5d);
            case 23 /*23*/:
                return new CollageOffset(0.0d, 0.5d, 1.0d, -0.5d);
            case 24 /*24*/:
                return new CollageOffset(0.5d, 0.5d, -0.5d, -0.5d);
            case 25 /*25*/:
                return new CollageOffset(0.5d, 0.5d, 0.5d, -0.5d);
            case 26 /*26*/:
                return new CollageOffset(0.5d, 0.5d, 0.5d, 0.5d);
            case 27 /*27*/:
                return new CollageOffset(0.5d, 0.5d, -0.5d, 0.5d);
            default:
                return new CollageOffset(0.0f, 0.0f, (float) 1.0, (float) 1.0);
        }
    }
}
