package formationapps.helper.db;

/**
 * Created by caliber fashion on 5/1/2017.
 */

public class TemplateData {
    private String templeFile = "tfile";
    private String templateId = "tid";

    public TemplateData() {
    }

    public String getTemplateFile() {
        return templeFile;
    }

    public void setTemplateFile(String file) {
        templeFile = file;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String id) {
        templateId = id;
    }
}
