package com.company.controller;

import com.company.dao.ApplicationSettingDao;
import com.company.dao.CallDAO;
import com.company.dao.UserDAO;
import com.company.model.MyCall;
import com.company.model.Setting;

import com.company.service.CallService;
import com.company.service.UserService;
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

    @Autowired
    private ApplicationSettingDao settingDao;

    @Autowired
    private CallService callService;

    @Autowired
    private UserService userService;

    @Autowired
    private CallDAO callDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(CallController.class);

    // Twilio Client relies on capability tokens to sign communications from devices to Twilio.
    // These tokens are a secure way of setting up your device to access various features of Twilio.
    // Capability tokens allow you to add Twilio capabilities to web and mobile applications
    // Create a token on your server and specify what capabilities you'd like your device to have.
    @GetMapping("/token")
    public void token(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Setting setting = settingDao.getFirstBy();
        String username = userService.getCurrentUser().getPhonenumber().substring(1);


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

    // Twilio account must be configured so that it receives TwiML( XML document with special tags defined by
    // Twilio to help you build your Programmable Voice application), which will instruct Twilio how to
    // connect an outgoing call. In our case, we want to provide him with the name of the client (phone number)
    // for the call.
    // For every outgoing call, Twilio makes this request to receive instructions for processing the call
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

    // Save the call after completion, direction = outgoing or incoming
    @PostMapping("/save/{direction}")
    public MyCall save(@PathVariable String direction, @RequestBody MyCall call){
        return callService.saveCall(direction, call);
    }

    // Receive user calls by id
    @GetMapping("/callsList/{id}")
    public List<MyCall> callsList(@PathVariable int id){
        return callDAO.findCallsById(id);
    }
}
