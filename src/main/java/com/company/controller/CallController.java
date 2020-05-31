package com.company.controller;

import com.company.dao.ApplicationSettingDao;
import com.company.dao.CallDAO;
import com.company.dao.UserDAO;
import com.company.model.MyCall;
import com.company.model.Setting;

import com.company.service.CallService;
import com.company.utils.CallUtils;
import com.google.common.collect.Lists;
import com.twilio.http.HttpMethod;
import com.twilio.jwt.client.OutgoingClientScope;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Say;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import com.twilio.jwt.client.ClientCapability;
import com.twilio.jwt.client.IncomingClientScope;
import com.twilio.jwt.client.Scope;
import com.twilio.twiml.voice.Client;

@RestController
@RequestMapping("/rest")
public class CallController {

    private final static String TOKEN_CONTENT_TYPE = "application/jwt";
    private final static String CAll_CONTENT_TYPE = "application/xml";
    private final static String FROM = "From";
    private final static String TO = "To";
    private final static String CALL_SID = "CallSid";


    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ApplicationSettingDao settingDao;

    @Autowired
    private CallService callService;

    @Autowired
    private CallDAO callDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(CallController.class);

    private String getCurrentName(){
        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        username = userDAO.findByEmail(username).getPhonenumber();

        return username.substring(1);
    }

    @GetMapping("/token")
    public void token(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Setting setting = settingDao.getFirstBy();
        String username = getCurrentName();

        OutgoingClientScope outgoingScope = new OutgoingClientScope.Builder(setting.getApplicationSid()).build();
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
        String callSid = request.getParameter(CALL_SID);

        try {
            VoiceResponse.Builder responseBuilder = new VoiceResponse.Builder();
            if (CallUtils.checkNumber(from) && CallUtils.checkNumber(to)) {
                Dial.Builder dialBuilder = new Dial.Builder().callerId(from).action("/rest/callEnd").method(HttpMethod.GET);

                Client client = new Client.Builder(to).build();
                dialBuilder = dialBuilder.client(client);
                Dial dial = dialBuilder.build();
                responseBuilder = responseBuilder.dial(dial);
            }else {
                String error = CallUtils.parseError(callSid,from,to);
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


    @GetMapping("/callEnd")
    public void callEnd(HttpServletRequest request, HttpServletResponse response){

        callService.writeCallToDB(request);

        VoiceResponse.Builder responseBuilder = new VoiceResponse.Builder();
        VoiceResponse twiml = responseBuilder.build();

        response.setContentType(CAll_CONTENT_TYPE);

        try {
            response.getWriter().print(twiml.toXml());
        } catch (IOException e) {
            LOGGER.warn("",e);
        }
    }

    @GetMapping("/getCallsList{phoneNumber}")
    public List<MyCall> getCallsList(@PathVariable String phoneNumber){
        return callDAO.getAllCall(phoneNumber);
    }
}
