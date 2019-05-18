package de.uniweimar.beacon.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.uniweimar.beacon.entities.Section;

public class BeaconService extends Service implements BeaconConsumer {
    private BeaconManager beaconManager;
    List<Section> sections=new ArrayList<>();
    Manager manager = new Manager();
    public BeaconService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        for (Section section : (List<Section>) Section.find(Section.class, ""))
            sections.add(section);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

        sections = new ArrayList<>();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                if (collection != null && !collection.isEmpty()) {
                    //update last found date
                    DataHolder.getInstance().setlastFoundDate(new Date(System.currentTimeMillis()));
                    Iterator iter = collection.iterator();
                    //Find the nearest beacon
                    Beacon nearestBeacon=(Beacon) iter.next();
                    for (Beacon bc : collection) {
                        double dis = bc.getDistance();
                        if(nearestBeacon.getDistance()>dis)
                            nearestBeacon=bc;
                    }

                    String uuId=nearestBeacon.getIdentifier(0).toString();
                    //when the beacon is changed
                    if (DataHolder.getInstance().getSection() != null && !DataHolder.getInstance().getSection().getSectionId().equals(uuId)) {
                        try {
                            DataHolder.getInstance().setEnterDate(new Date(System.currentTimeMillis()));
                            manager.resetSteps(DataHolder.getInstance().getSection(), DataHolder.getInstance().getUser(), DataHolder.getInstance().enterDate, new Date(System.currentTimeMillis()), DataHolder.getInstance().getSteps());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        Section reg = manager.getSection(uuId);
                        DataHolder.getInstance().setSection(reg);
                        DataHolder.getInstance().setSteps(0);
                    }
                    //when new beacon discovers
                    else if (DataHolder.getInstance().getSection() == null)
                    {
                        DataHolder.getInstance().setEnterDate(new Date(System.currentTimeMillis()));

                        DataHolder.getInstance().setSteps(0);
                        Section reg = manager.getSection(uuId);
                        DataHolder.getInstance().setSection(reg);

                    }


                }
            }
        });
    }

    @Override
    public void onDestroy() {
        beaconManager.unbind(this);
        super.onDestroy();

    }
}
