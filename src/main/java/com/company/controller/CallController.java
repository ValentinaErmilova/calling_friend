package com.company.controller;

import com.company.dao.UserDAO;
import com.google.common.collect.Lists;
import com.twilio.jwt.client.OutgoingClientScope;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
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

    @Autowired
    private UserDAO userDAO;

    private final String ACCOUNT_SID = "ACdcacccdf505f9194fa1d718da40bfff5";
    private final String AUTH_TOKEN = "d7120dfd13ea7e3ef63afdb714ba06af";
    private final String applicationSid = "AP4aa3920c2cd3ac669a0dcc1a102ed3ee";

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

        String username = getCurrentName();
        System.out.println("get token "+username);

        OutgoingClientScope outgoingScope = new OutgoingClientScope.Builder(applicationSid).build();
        IncomingClientScope incomingScope = new IncomingClientScope(username);

        List<Scope> scopes = Lists.newArrayList(outgoingScope, incomingScope);

        ClientCapability capability = new ClientCapability.Builder(ACCOUNT_SID, AUTH_TOKEN)
                .scopes(scopes)
                .build();

        String token = capability.toJwt();

        response.setContentType("application/jwt");
        response.getWriter().print(token);
    }

    @GetMapping("/doCall")
    public void doCall(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String to = request.getParameter("To");
        String from = request.getParameter("From");

        VoiceResponse.Builder responseBuilder = new VoiceResponse.Builder();
        if (to != null) {
            Dial.Builder dialBuilder = new Dial.Builder().callerId(from);

            Client client = new Client.Builder(to).build();
            dialBuilder = dialBuilder.client(client);
            Dial dial = dialBuilder.build();
            responseBuilder = responseBuilder.dial(dial);
        }

        VoiceResponse twiml = responseBuilder.build();

        response.setContentType("application/xml");

        try {
            response.getWriter().print(twiml.toXml());
        } catch (TwiMLException e) {
            e.printStackTrace();
        }
    }
}
