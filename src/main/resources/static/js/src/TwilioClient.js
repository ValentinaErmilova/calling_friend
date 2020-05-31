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
let dialpad;

function loadDOM() {
    call = document.getElementById('callPanel');
    incomingDiv = document.getElementById('incomingCall');
    outgoingDiv = document.getElementById('outgoingCall');
    acceptButton = document.getElementById('acceptCall');
    rejectButton = document.getElementById('rejectCall');
    endButton = document.getElementById('endCall');
    to = document.getElementById("inputnumber");
    callButton = document.getElementById("button-call");
    incomingButtons = document.getElementById("incomingButtons");
    from = document.getElementById("from");
    toClient = document.getElementById("toClient");
    fromClient = document.getElementById("fromClient");
    dialpad = document.getElementById("dial-pad-panel")
}

function getToken() {
    loadDOM();
    $(function() {
        $.ajax('/rest/token')
            .done(function(token) {

                Twilio.Device.setup(token);

                Twilio.Device.ready(function(device) {
                    console.log('Ready');
                });

                Twilio.Device.error(function(error) {
                    console.log('Error: ' + error.message);
                });

                Twilio.Device.incoming(function(conn) {
                    call.style.display = 'block';
                    let from = conn.parameters.From
                    console.log('Incoming connection from ' + from);

                    fromClient.innerHTML = from;
                    incomingDiv.style.display = 'block';
                    dialpad.style.display = 'none';

                    call.classList.add("call-panel");

                    conn.on('cancel', function(conn) {
                        console.log("the call has ended");
                        endCall();
                    });

                    acceptButton.onclick = function() {
                        let callParam = conn.parameters;
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

                Twilio.Device.disconnect(function(connection){
                    console.log(connection);
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
    call.style.display = 'none';
    dialpad.style.display = 'block';
}

function outgoingCallWithNumber(number) {

    call.style.display = 'block';
    outgoingDiv.style.display = 'block';
    endButton.style.display = 'block';
    dialpad.style.display = 'none';

    toClient.innerHTML = number;
    let fromS = from.innerHTML;
    const params = {To: number, From: fromS.substr(1)};
    Twilio.Device.connect(params);
}

function outgoingCall() {
    if(to.value.length === 15) {
        call.style.display = 'block';
        outgoingDiv.style.display = 'block';
        endButton.style.display = 'block';
        dialpad.style.display = 'none';

        toClient.innerHTML = to.value;
        let fromS = from.innerHTML;

        const params = {To: getToo(), From: fromS.substr(1)};
        Twilio.Device.connect(params);
    }else {
        to.classList.add("is-invalid");
        to.classList.remove("is-valid");
    }
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