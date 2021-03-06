// Сontroller for managing calls on ui side
myApp.controller("CallController", function($scope, $rootScope, $http) {

    let NeedSetDefault = true;
    $scope.toNumber = '';

    $scope.outgoingCall = function(){
        let to = $scope.toNumber;
        let from = $scope.user.phonenumber;

        // if the number is entered correctly and there is no active call, then we make a new outgoing call
        if($scope.valid && $scope.ActiveCall !== true){
            $scope.TWconnected = false;
            $scope.ActiveCall = true;
            $scope.ActiveOutbound = true;
            $scope.EndActive = true;
            $scope.to = to;

            const params = {To: spaceDelete(to), From: spaceDelete(from)};
            Twilio.Device.connect(params);
            $scope.toNumber = '';
            $scope.valid = false;
            $scope.invalid = false;
        }else {
            $scope.toNumber = '';
            $scope.valid = false;
            $scope.invalid = false;
        }

    }

    // this function is called if the user calls back from the call page or from the list of friends
    $rootScope.$on('callback', function (event, number) {
        $scope.toNumber = numberInput(number);
        $scope.outgoingCall();
    })

    // Capability token request
    $http({
        method: 'GET',
        url: '/rest/token'
    }).then(function success(response) {
        // The Twilio.Device object is available when twilio.js is included in page.
        // It represents a soft device, the client that provides connections into Twilio.

        // pass the token that we received from the server
        // now we can make calls
        Twilio.Device.setup(response.data,{allowIncomingWhileBusy : false});

        Twilio.Device.ready(function() {
            $scope.$apply(function(){
                $scope.TWconnected = true;
            });
        });

        Twilio.Device.error(function(error) {
            console.log('Error: ' + error.message);
        });

        // incoming call processing
        Twilio.Device.incoming(function(conn) {
            if($scope.ActiveCall !== true) {
                $scope.connection = conn;

                $scope.from = inputFormat(conn.parameters.From);

                $scope.$apply(function () {
                    $scope.TWconnected = false;
                    $scope.ActiveCall = true;
                    $scope.ActiveInbound = true;
                    $scope.InboundOption = true;
                });

                conn.on('cancel', function () {
                    $scope.$apply(function () {
                        $scope.setDefault();
                        DisconnectCall(conn);
                    });
                });
            }else {
                conn.reject();
            }

        });

        // call termination processing
        Twilio.Device.disconnect(function(conn){
            if(NeedSetDefault) {
                $scope.$apply(function() {
                    $scope.setDefault();
                });
            }

            setTimeout(() => {  DisconnectCall(conn); }, 2000);

        });

    }, function error(response) {
        alert('Could not authenticate!');
    });

    $scope.accept = function () {
        $scope.connection.accept();
        $scope.InboundOption = false;
        $scope.EndActive = true;
    }

    $scope.reject = function () {
        $scope.connection.reject();
        $scope.endCall();
        DisconnectCall($scope.connection);
    }

    $scope.endCall = function () {
        NeedSetDefault = false;
        Twilio.Device.disconnectAll();
        $scope.setDefault();
    }

    $scope.setDefault = function () {
        $scope.ActiveInbound = false;
        $scope.ActiveOutbound = false;
        $scope.InboundOption = false;
        $scope.EndActive = false;
        $scope.ActiveCall = false;
        $scope.TWconnected = true;
        NeedSetDefault = true;
    }

    $scope.pressNumber = function(added) {

        if($scope.toNumber.length < 15) {
            $scope.toNumber = numberInput($scope.toNumber + added)
        }
    }

    $scope.change = function () {
        $scope.toNumber = numberInput(spaceDelete($scope.toNumber));
    }

    $scope.unfocus = function () {
        numberInput($scope.toNumber);
    };

    $scope.deleteLast = function () {
        let number = $scope.toNumber;

        if(number.substr(number.length-1,number.length) === ' '){
            number = number.substr(0,number.length-1);
        }

        number = number.substr(0,number.length-1);

        $scope.toNumber = numberInput(number)
    }

    function numberInput(phoneNumber) {
        phoneNumber = inputFormat(phoneNumber);
        let checkResult = validation(phoneNumber,$scope.user.phonenumber);

        setNumberStyle($scope, checkResult, callsPage);

        return phoneNumber;
    }

    // function sends to the server information about the call to be saved
    function DisconnectCall(conn) {
        let direction = conn._direction;
        let to;
        let from;

        if(direction === 'INCOMING'){
            to = conn.parameters.To;
            to = '+' + to.substr(to.indexOf(':') + 1);
            from = conn.parameters.From;
        }else {
            to = '+' + conn.message.To;
            from = '+' + conn.message.From;
        }

        let call = {
            callSid : conn.parameters.CallSid,
            toNumber : to,
            fromNumber : from,
        }


        $http({
            method: 'POST',
            url: '/rest/save/'+direction,
            data: call
        }).then( function success(response) {
            if(response.data !== '') {
                $rootScope.calls.unshift(response.data);
            }else {
            }

        });
    }
});
