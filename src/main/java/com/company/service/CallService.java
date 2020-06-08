package com.company.service;

import com.company.dao.ApplicationSettingDao;
import com.company.dao.CallDAO;
import com.company.dao.UserDAO;
import com.company.model.MyCall;
import com.company.model.Setting;
import com.company.model.User;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Call;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

import static com.company.controller.CallController.*;

@Service
public class CallService {

    @Autowired
    private CallDAO callDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ApplicationSettingDao settingDao;

    private final static String INBOUND = "INCOMING";
    private final static String OUTBOUND = "OUTGOING";
    private final static String DIRECTION = "Direction";

    public User getCurrentUser(){
        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userDAO.findByEmail(username);
    }

    public MyCall saveCall(Map<String, String> data){
        Setting setting = settingDao.getFirstBy();
        Twilio.init(setting.getAccountSid(), setting.getAuthToken());



        String callSid;
        boolean closedBeforeBeeps = false;

        boolean outbound = data.get(DIRECTION).equals(OUTBOUND);
        if(outbound) {
            ResourceSet<Call> calls = Call.reader().setParentCallSid(data.get(CALL_SID)).read();

            if(calls.iterator().hasNext()){
                Call childCall = calls.iterator().next();
                callSid = childCall.getSid();

            }else {
                closedBeforeBeeps = true;
                callSid = data.get(CALL_SID);
            }

        }else {
            callSid = data.get(CALL_SID);
        }

        Call call = Call.fetcher(callSid).fetch();

        String to = data.get(TO);
        String from = data.get(FROM);

        int toUser = userDAO.findIdByPhoneNumber(to);
        int fromUser = userDAO.findIdByPhoneNumber(from);

        DateTime startTime = call.getStartTime();
        Timestamp date = new Timestamp(startTime.getMillis());

        MyCall myCall = new MyCall(
                callSid,
                toUser,
                fromUser,
                to,
                from,
                call.getDuration(),
                call.getStatus().toString(),
                date);

        if(outbound && !closedBeforeBeeps) {
            callDAO.save(myCall);
        }

        if(!closedBeforeBeeps) {
            return myCall;
        }else {
            return null;
        }
    }
}
