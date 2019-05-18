package de.uniweimar.beacon.services;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import de.uniweimar.beacon.entities.Log;
import de.uniweimar.beacon.entities.Section;
import de.uniweimar.beacon.entities.User;

public class Manager {

    public void addsection(String sectionId,String sectionName)
    {
        new Section(sectionId,sectionName).save();

    }
    public void addUser(String userName,String deviceId,String numberToPass,String textEmail,String unparsedDate)
    {
        new User( userName,deviceId,numberToPass,new String(unparsedDate),textEmail).save();

    }

    public Log addLog(Section section,User user,   Date enter,  Date exit,  long steps)
    {
        Log log=new Log(section,user,enter,exit,(int) steps);
        log.save();
        return log;
    }


    public long getSteps(User user)
    {

        List<Log> logs=Log.find(Log.class,"USER="+user.getId());
        long steps=0;
        for(Log log:logs)
            steps+=log.getSteps();
        return steps;
    }

    public User getUserByDeviceId(String deviceId)
    {
       List<User> users=User.find(User.class,"DEVICE_ID='"+deviceId+"'",null);
       if(users.size()>0)
           return users.get(0);
       else
           return null;
    }

    public Section getSection(String sectionId)
    {
        List<Section> sections=Section.find(Section.class,"SECTION_ID='"+sectionId+"'",null);
        if(sections.size()>0)
            return sections.get(0);
        else
            return null;
    }

    public  Section getSectionBySectionName(String sectionId)
    {
        List<Section> sections=Section.find(Section.class,"SECTION_NAME='"+sectionId+"'",null);
        if(sections.size()>0)
            return sections.get(0);
        else
            return null;
    }

    public long resetSteps(Section section,User user,   Date enter,  Date exit,  long steps) throws JsonProcessingException {
        if( DataHolder.getInstance().getEnterDate()==DataHolder.getInstance().getLastFoundDate())
            return DataHolder.getInstance().getTotalSteps();
        DataHolder.getInstance().setSection(null);
        DataHolder.getInstance().setSteps(0);

        Log lastLog=addLog(section,user,enter,exit,steps);
        //Update data on server
        PostToServer server = new PostToServer();
        server.Post_JSON(lastLog.getJSon());




        DataHolder.getInstance().setEnterDate(DataHolder.getInstance().getLastFoundDate());
        List<Log> logs=Log.find(Log.class,"USER="+user.getId());
        steps=0;
        for(Log log:logs)
            steps+=log.getSteps();
        DataHolder.getInstance().setTotalSteps(steps);

        return steps;

    }
}
