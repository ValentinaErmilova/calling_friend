package com.company.controller;

import com.company.dao.ApplicationSettingDao;
import com.company.dao.UserDAO;
import com.company.model.Setting;
import com.google.common.collect.Lists;
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
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ApplicationSettingDao settingDao;

    private static final Logger logger = LoggerFactory.getLogger(CallController.class);;

    private String getCurrentName(){
        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        username = userDAO.findByEmail(username).getPhonenumber();

        return username;
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
        String from = request.getParameter("From");
        String to = request.getParameter("To");

        VoiceResponse.Builder responseBuilder = new VoiceResponse.Builder();
        if (checkNumber(from) && checkNumber(to)) {
            Dial.Builder dialBuilder = new Dial.Builder().callerId(from);

            Client client = new Client.Builder(to).build();
            dialBuilder = dialBuilder.client(client);
            Dial dial = dialBuilder.build();
            responseBuilder = responseBuilder.dial(dial);
        }else {

            //also maybe need to add validation on UI side
            List<String> errors = new ArrayList<>();
            if(!checkNumber(from)){
                errors.add("invalid 'from' number");
            }
            if(!checkNumber(to)){
                errors.add("invalid 'to' number");
            }

            String error = String.join(", ", errors);
            Say say = new Say.Builder(error).build();
            responseBuilder = responseBuilder.say(say);


            String callSid = request.getParameter("CallSid");
            logger.warn("error in call: call sid = "+callSid+" : "+error);
        }

        VoiceResponse twiml = responseBuilder.build();

        response.setContentType(CAll_CONTENT_TYPE);

        try {
            response.getWriter().print(twiml.toXml());
        } catch (TwiMLException e) {
            logger.warn("",e);
        }
    }

    private boolean checkNumber(String number){
        return !(number == null || "".equals(number));
    }
}
