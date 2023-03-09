package com.functionapps.mview_sdk2.main;

import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;

import java.util.ArrayList;
import java.util.Date;

public class MyCall {

	public int calltype; //1 means incoming, 2 means outgoing
	public String callerPhoneNumber;
	public String myLat;
	public String myLon;
	public Date timeofCall;
	public Date endTime;
	public long timeofcallInMS;
	public long endTimeInMS;
	public String disconnectCause;
	public boolean isCallTaken;
	public boolean iscallNotAnswered;
	public boolean isDroppedCall;
	public String operator;
	public String cellid;
	public long duration;
	public boolean isRoaming;
	public double speed;

	public ArrayList<ServiceState> serviceStateArr;
	public ArrayList<CellLocation> cellLocationArr;
	public ArrayList<SignalStrength> signalStrengthArr;
	public String timeofcallInMSNew;

	public MyCall()
	{
		disconnectCause = "";
		serviceStateArr = new ArrayList<ServiceState>();
		cellLocationArr = new ArrayList<CellLocation>();
		signalStrengthArr = new ArrayList<SignalStrength>();
	}
}
