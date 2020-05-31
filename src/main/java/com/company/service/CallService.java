package com.company.service;

import com.company.dao.ApplicationSettingDao;
import com.company.dao.CallDAO;
import com.company.model.MyCall;
import com.company.model.Setting;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service
public class CallService {

    @Autowired
    private CallDAO callDAO;

    @Autowired
    private ApplicationSettingDao settingDao;

    private final static String DIAL_CALL_SID = "DialCallSid";

    public void writeCallToDB(HttpServletRequest request){

        new Thread(() -> {
            String callSid = request.getParameter(DIAL_CALL_SID);

            Setting setting = settingDao.getFirstBy();

            Twilio.init(setting.getAccountSid(), setting.getAuthToken());

            Call call = Call.fetcher(callSid).fetch();
            String callTo = call.getTo();

            String to = "+" + callTo.substring(callTo.indexOf(":") + 1);

            DateTime startTime = call.getStartTime();
            Timestamp ts = new Timestamp(startTime.getMillis());

            MyCall myCall = new MyCall(callSid, call.getFrom(), to,call.getDuration(), call.getStatus().toString(), ts);

            callDAO.save(myCall);
        }).start();
    }
}
