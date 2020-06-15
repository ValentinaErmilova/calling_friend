package com.company.service;

import com.company.dao.ApplicationSettingDao;
import com.company.dao.CallDAO;
import com.company.dao.UserDAO;
import com.company.model.MyCall;
import com.company.model.Setting;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Call;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class CallService {

    @Autowired
    private CallDAO callDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ApplicationSettingDao settingDao;

    private final static String OUTBOUND = "OUTGOING";
    private final static String QUEUED = "queued";
    private final static String BUSY = "busy";


    public MyCall saveCall(String direction, MyCall call) {
        Setting setting = settingDao.getFirstBy();
        Twilio.init(setting.getAccountSid(), setting.getAuthToken());

        String callSid;
        boolean closedBeforeBeeps = false;

        boolean outbound = direction.equals(OUTBOUND);
        if(outbound) {
            ResourceSet<Call> calls = Call.reader().setParentCallSid(call.getCallSid()).read();

            if(calls.iterator().hasNext()){
                Call childCall = calls.iterator().next();
                callSid = childCall.getSid();
            }else {
                closedBeforeBeeps = true;
                callSid = call.getCallSid();
            }

        }else {
            callSid = call.getCallSid();
        }


        if(!closedBeforeBeeps) {
            Call callSave = Call.fetcher(callSid).fetch();

            String to = call.getToNumber();
            String from = call.getFromNumber();

            int toUser = userDAO.findIdByPhoneNumber(to);
            int fromUser = userDAO.findIdByPhoneNumber(from);

            DateTime startTime = callSave.getStartTime();
            Timestamp date = new Timestamp(startTime.getMillis());

            String status = callSave.getStatus().toString();
            String duration = callSave.getDuration();

            if (status.equals(QUEUED)) {
                status = BUSY;
                duration = "0";
            }

            MyCall myCall = new MyCall(
                    callSid,
                    toUser,
                    fromUser,
                    to,
                    from,
                    duration,
                    status,
                    date);

            if (outbound) {
                callDAO.save(myCall);
            }

            return myCall;

        } else {
            return null;
        }

    }
}
