package de.uniweimar.beacon.entities;

import com.orm.SugarRecord;

public class Section extends SugarRecord {
    String sectionId;
    String sectionName;

    public Section() {
    }

    public Section(String sectionId, String sectionName) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getSectionId() {
        return sectionId.toLowerCase();
    }
}
