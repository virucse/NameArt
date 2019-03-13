package formationapps.helper.stickers;

/**
 * Created by caliber fashion on 5/9/2017.
 */

public class StickerDataHolder {
    private String parent = "";
    private String[] childs;
    private String tag = "server";

    public StickerDataHolder() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String[] getChilds() {
        return childs;
    }

    public void setChilds(String[] childs) {
        this.childs = childs;
    }
}
