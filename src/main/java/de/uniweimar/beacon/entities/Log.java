package de.uniweimar.beacon.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orm.SugarRecord;

import java.util.Date;

public class Log extends SugarRecord {
    Section section;
    User user;
    Date enter;
    Date exit;
    int steps;

    public Log(Section section, User user, Date enter, Date exit, int steps) {
        this.enter = enter;
        this.exit = exit;
        this.section = section;
        this.steps = steps;
        this.user = user;
    }

    public Log() {
    }

    public int getSteps() {
        return this.steps;
    }

    public String getJSon() throws JsonProcessingException {
        String jsonInString= String.format("{'Section': {'SectionId': '%s','SectionName': '%s'},'User': {'UserName': '%s','DeviceId': '%s','Gender': '%s','BirthDate': '%s','textEmail': '%s' },'EnterDate': '%s','ExitDate': '%s','Steps': %d}",
                this.section.sectionId,this.section.sectionName,this.user.userName,this.user.deviceId,this.user.gender,this.user.birthDate,this.user.textEmail,this.enter.toString(),this.exit.toString(),this.steps);
        return jsonInString;
    }
}