package com.company.controller;

import com.google.common.collect.Lists;
import com.twilio.Twilio;
import com.twilio.jwt.client.OutgoingClientScope;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.TwiMLException;

@RestController
@RequestMapping("/rest")
public class CallController {


    static String username;

    @GetMapping("/token")
    public void token(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        username = username.substring(0,username.indexOf("@"));
        System.out.println("token username = "+username);

        System.out.println("get token");
        String ACCOUNT_SID = "ACdcacccdf505f9194fa1d718da40bfff5";
        String AUTH_TOKEN = "d7120dfd13ea7e3ef63afdb714ba06af";
        String applicationSid = "AP4aa3920c2cd3ac669a0dcc1a102ed3ee";

        OutgoingClientScope outgoingScope = new OutgoingClientScope.Builder(applicationSid).build();
        IncomingClientScope incomingScope = new IncomingClientScope(username);//имя нашего клиента, те куда звонить

        List<Scope> scopes = Lists.newArrayList(outgoingScope, incomingScope);

        ClientCapability capability = new ClientCapability.Builder(ACCOUNT_SID, AUTH_TOKEN)
                .scopes(scopes)
                .build();

        String token = capability.toJwt();

        response.setContentType("application/jwt");
        response.getWriter().print(token);
    }

    @PostMapping("/acceptCall")
    public void acceptCall(HttpServletRequest request, HttpServletResponse response) throws IOException{
        /*System.out.println("incoming call");

        System.out.println("accept username = "+username);

        String to = request.getParameter("To");
        System.out.println("to = "+to);

        Client client = new Client.Builder(to).build();
        Dial dial = new Dial.Builder().client(client).build();
        VoiceResponse twiml = new VoiceResponse.Builder().dial(dial).build();

        response.setContentType("application/xml");

        try {
            response.getWriter().print(twiml.toXml());
        } catch (TwiMLException e) {
            e.printStackTrace();
        }*/

        System.out.println("incoming call");

        System.out.println("accept username = "+username);

        String to = request.getParameter("To");
        String from = request.getParameter("From");
        System.out.println("to = "+to);
        System.out.println("from = "+from);


        VoiceResponse.Builder responseBuilder = new VoiceResponse.Builder();
        if (to != null) {

            Client client = new Client.Builder(to).build();
            Dial dial = new Dial.Builder().client(client).build();
            VoiceResponse twiml = new VoiceResponse.Builder().dial(dial).build();

            response.setContentType("application/xml");

            try {
                response.getWriter().print(twiml.toXml());
            } catch (TwiMLException e) {
                e.printStackTrace();
            }

        }



    }
}
