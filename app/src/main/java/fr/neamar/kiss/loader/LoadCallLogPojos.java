package fr.neamar.kiss.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.neamar.kiss.forwarder.Permission;
import fr.neamar.kiss.pojo.CallLogPojo;

public class LoadCallLogPojos extends LoadPojos<CallLogPojo> {

    public LoadCallLogPojos(Context context) {
        super(context, "content://");
    }

    @Override
    protected ArrayList<CallLogPojo> doInBackground(Void... params) {
        long start = System.nanoTime();

        ArrayList<CallLogPojo> callLogs = new ArrayList<>();
        Context c = context.get();
        if(c == null) {
            return callLogs;
        }

        // Skip if we don't have permission to list contacts yet:(
        if(!Permission.checkCallLogPermission(c)) {
            Permission.askCallLogPermission();
            return callLogs;
        }

        // Run query

        Uri allCalls = Uri.parse("content://call_log/calls");
        Cursor cur = context.get().getContentResolver().query(allCalls, null, null, null, "DESC");

        if (cur != null) {
            if (cur.getCount() > 0) {

                int number = cur.getColumnIndex(CallLog.Calls.NUMBER);
                int name = cur.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int duration = cur.getColumnIndex(CallLog.Calls.DURATION);
                int callType = cur.getColumnIndex(CallLog.Calls.TYPE);
                int date = cur.getColumnIndex(CallLog.Calls.DATE);

                while (cur.moveToNext()) {
                    CallLogPojo callLog = new CallLogPojo();

                    callLog.name = cur.getString(name);
                    callLog.number = cur.getString(number);
                    callLog.date = cur.getString(date);
                    callLog.duration = cur.getString(duration);

                    callLog.setCallType(Integer.parseInt(cur.getString(callType)));

                    callLog.id = pojoScheme + callLog.callType + callLog.date + callLog.number;

                    callLogs.add(callLog);
                }
            }
            cur.close();
        }

        long end = System.nanoTime();
        Log.i("time", Long.toString((end - start) / 1000000) + " milliseconds to list contacts");
        return callLogs;
    }
}
