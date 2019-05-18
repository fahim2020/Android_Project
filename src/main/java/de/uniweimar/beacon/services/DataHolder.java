package de.uniweimar.beacon.services;

import java.util.Date;

import de.uniweimar.beacon.entities.Section;
import de.uniweimar.beacon.entities.User;

public class DataHolder {
    public Date enterDate;
    public Date getEnterDate() {return enterDate;}
    public void setEnterDate(Date enterDate) {this.enterDate = enterDate;}

    private long steps=0;
    public long getSteps() {return steps;}
    public void setSteps(long steps) {this.steps = steps;}

    private long totalSteps=0;
    public long getTotalSteps() {return totalSteps;}
    public void setTotalSteps(long totalSteps) {this.totalSteps = totalSteps;}

    //save current region name
    private Section section=null;
    public Section getSection() {return section;}
    public void setSection(Section section) {
        this.section = section;}

    private String regionTemp="";
    public String getRegionTemp() {return regionTemp;}
    public void setRegionTemp(String regionTemp) {this.regionTemp = regionTemp;}

    private User user=null;
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

    public Date lastFoundDate;
    public Date getLastFoundDate() {return lastFoundDate;}
    public void setlastFoundDate(Date lastFoundDate) {this.lastFoundDate = lastFoundDate;}
}
