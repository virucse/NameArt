package formationapps.helper.db;

/**
 * Created by caliber fashion on 4/28/2017.
 */

public class ServerFont {
    //private variables
    int _id;
    String pngName;
    String ttfName;

    public ServerFont() {

    }

    // constructor
    public ServerFont(int id, String pngName, String ttfName) {
        this._id = id;
        this.pngName = pngName;
        this.ttfName = ttfName;
    }

    // constructor
    public ServerFont(String pngName, String ttfName) {
        this.pngName = pngName;
        this.ttfName = ttfName;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting name
    public String getPngName() {
        return this.pngName;
    }

    // setting name
    public void setPngName(String pngName) {
        this.pngName = pngName;
    }

    // getting phone number
    public String geTttfName() {
        return this.ttfName;
    }

    // setting phone number
    public void setTtfName(String ttfName) {
        this.ttfName = ttfName;
    }
}
