package com.company.controller;

import com.company.dao.ApplicationSettingDao;
import com.company.dao.CallDAO;
import com.company.dao.UserDAO;
import com.company.model.MyCall;
import com.company.model.Setting;

import com.company.model.User;
import com.company.service.CallService;
import com.company.utils.CallUtils;
import com.google.common.collect.Lists;
import com.twilio.jwt.client.OutgoingClientScope;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Say;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.twilio.jwt.client.ClientCapability;
import com.twilio.jwt.client.IncomingClientScope;
import com.twilio.jwt.client.Scope;
import com.twilio.twiml.voice.Client;

@RestController
@RequestMapping("/rest")
public class CallController {

    private final static String TOKEN_CONTENT_TYPE = "application/jwt";
    private final static String CAll_CONTENT_TYPE = "application/xml";
    public final static String FROM = "From";
    public final static String TO = "To";
    public final static String CALL_SID = "CallSid";


    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ApplicationSettingDao settingDao;

    @Autowired
    private CallService callService;

    @Autowired
    private CallDAO callDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(CallController.class);

    @GetMapping("/currentUser")
    public User currentUser(){
        User user = callService.getCurrentUser();
        return user;
    }

    @GetMapping("/token")
    public void token(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Setting setting = settingDao.getFirstBy();
        String username = callService.getCurrentUser().getPhonenumber().substring(1);


        OutgoingClientScope outgoingScope = new OutgoingClientScope.Builder(setting.getApplicationSid()).clientName(username).build();
        IncomingClientScope incomingScope = new IncomingClientScope(username);

        List<Scope> scopes = Lists.newArrayList(outgoingScope, incomingScope);

        ClientCapability capability = new ClientCapability.Builder(setting.getAccountSid(), setting.getAuthToken())
                .scopes(scopes)
                .build();

        String token = capability.toJwt();

        response.setContentType(TOKEN_CONTENT_TYPE);
        response.getWriter().print(token);
    }

    @GetMapping("/doCall")
    public void doCall(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String from = request.getParameter(FROM);
        String to = request.getParameter(TO);

        try {
            VoiceResponse.Builder responseBuilder = new VoiceResponse.Builder();
            if (CallUtils.checkNumber(from) && CallUtils.checkNumber(to)) {

                Dial.Builder dialBuilder = new Dial.Builder().callerId(from);
                Client client = new Client.Builder(to).build();
                dialBuilder = dialBuilder.client(client);
                Dial dial = dialBuilder.build();
                responseBuilder = responseBuilder.dial(dial);

            }else {
                String error = CallUtils.parseError(from,to);
                Say say = new Say.Builder(error).build();
                responseBuilder = responseBuilder.say(say);
            }

            VoiceResponse twiml = responseBuilder.build();

            response.setContentType(CAll_CONTENT_TYPE);

            response.getWriter().print(twiml.toXml());
        } catch (TwiMLException e) {
            LOGGER.warn("",e);
        }
    }

    @PostMapping("/save")
    public MyCall save(@RequestBody Map<String,String> data){

        return callService.saveCall(data);
    }

    @GetMapping("/getCallsList/{id}")
    public List<MyCall> getCallsList(@PathVariable int id){
        return callDAO.findCallsById(id);
    }
}
