let call;
let incomingDiv;
let outgoingDiv;
let acceptButton;
let rejectButton;
let endButton;
let callButton;
let to;
let from;
let incomingButtons;
let toClient;
let fromClient;

function loadDOM() {
    call = document.getElementById('call');
    incomingDiv = document.getElementById('incomingCall');
    outgoingDiv = document.getElementById('outgoingCall');
    acceptButton = document.getElementById('acceptCall');
    rejectButton = document.getElementById('rejectCall');
    endButton = document.getElementById('endCall');
    to = document.getElementById("toInput");
    callButton = document.getElementById("button-call");
    incomingButtons = document.getElementById("incomingButtons");
    from = document.getElementById("from");
    toClient = document.getElementById("toClient");
    fromClient = document.getElementById("fromClient");
}

function getToken() {
    loadDOM();
    $(function() {
        $.ajax('/rest/token')
            .done(function(token) {

                console.log('Got a token: ', token);

                Twilio.Device.setup(token);

                Twilio.Device.ready(function(device) {
                    console.log('Ready');
                });

                Twilio.Device.error(function(error) {
                    console.log('Error: ' + error.message);
                });

                Twilio.Device.incoming(function(conn) {
                    let from = conn.parameters.From
                    console.log('Incoming connection from ' + from);

                    fromClient.innerHTML = from;
                    incomingDiv.style.display = 'block';

                    conn.on('cancel', function(conn) {
                        console.log("the call has ended");
                        endCall();
                    });

                    acceptButton.onclick = function() {
                        let callParam = conn.parameters;
                        console.log(callParam);
                        acceptCall();
                        console.log("accept call");
                        conn.accept();
                    }

                    rejectButton.onclick = function() {
                        endCall();
                        console.log("reject call");
                        conn.reject();
                    }
                });

                Twilio.Device.disconnect(function(){
                    setDefaultStyle();
                    console.log("disconnect call");
                });

            })
            .fail(function() {
                alert('Could not authenticate!');
            });
    });
}

function setDefaultStyle() {
    incomingDiv.style.display = 'none';
    incomingButtons.style.display = 'block';
    outgoingDiv.style.display = 'none';
    endButton.style.display = 'none';
    toClient.innerHTML = "";
    fromClient.innerHTML = "";
}

function outgoingCall() {
    outgoingDiv.style.display = 'block';
    endButton.style.display = 'block';

    //toClient
    toClient.innerHTML = to.value;
    let fromS = from.innerHTML;
    const params = { To: to.value, From: fromS};
    console.log(params)
    Twilio.Device.connect(params);
}

function endCall() {
    console.log("end call");
    setDefaultStyle();
    Twilio.Device.disconnectAll();
}

function acceptCall() {
    incomingButtons.style.display = 'none';
    endButton.style.display = 'block';
}