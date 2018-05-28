package fr.neamar.kiss.pojo;

import android.provider.CallLog;

public class CallLogPojo extends Pojo {

    public String name;
    public Integer callType;
    public String number;
    public String date;
    public String duration;
    
    private String _callType;


    public void setCallType(Integer type) {
        this.callType = type;

        String dir = null;
        switch (type) {
            case CallLog.Calls.OUTGOING_TYPE:
                dir = "OUTGOING";
                break;

            case CallLog.Calls.INCOMING_TYPE:
                dir = "INCOMING";
                break;

            case CallLog.Calls.MISSED_TYPE:
                dir = "MISSED";
                break;
        }
        this._callType = dir;
    }
}
