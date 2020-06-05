myApp.controller("CallController", function($scope, $http, share) {

    let NeedSetDefault = true;

    $scope.toNumber = '';

    $scope.outgoingCall = function(){

        let to = $scope.toNumber;
        let from = currentUser.phonenumber;

        if($scope.valid){
            $scope.TWconnected = false;
            $scope.ActiveCall = true;
            $scope.ActiveOutbound = true;
            $scope.EndActive = true;
            $scope.to = to;

            const params = {To: spaceDelete(to), From: spaceDelete(from)};
            Twilio.Device.connect(params);
        }else {
            validation(to, true);
        }

    }

    $scope.$on('callback', function (event, args) {
        let phoneNumber = numberInputFormat(args.message);
        $scope.toNumber = phoneNumber;
        $scope.outgoingCall();
    })

    $http({
        method: 'GET',
        url: '/rest/token'
    }).then(function success(response) {
        Twilio.Device.setup(response.data);

        Twilio.Device.ready(function() {
            $scope.$apply(function(){
                $scope.TWconnected = true;
            });
        });

        Twilio.Device.error(function(error) {
            console.log('Error: ' + error.message);
        });

        Twilio.Device.incoming(function(conn) {
            $scope.connection = conn;

            let from = numberInputFormat(conn.parameters.From);

            $scope.from = from;

            $scope.$apply(function(){
                $scope.TWconnected = false;
                $scope.ActiveCall = true;
                $scope.ActiveInbound = true;
                $scope.InboundOption = true;
            });

            conn.on('cancel', function() {
                $scope.$apply(function() {
                    $scope.setDefault();
                    $scope.reject();
                });
            });

        });

        Twilio.Device.disconnect(function(conn){
            if(NeedSetDefault) {
                $scope.$apply(function() {
                    $scope.setDefault();
                });
            }

            DisconnectCall(conn);
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
            $scope.toNumber = numberInputFormat($scope.toNumber + added);
        }
    }

    $scope.change = function () {
        $scope.toNumber = numberInputFormat($scope.toNumber);
    }

    $scope.unfocus = function () {
        validation($scope.toNumber);
    };

    $scope.deleteLast = function () {
        let number = $scope.toNumber;

        if(number.substr(number.length-1,number.length) === ' '){
            number = number.substr(0,number.length-1);
        }

        number = number.substr(0,number.length-1);

       $scope.toNumber = numberInputFormat(number)
    }

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

        let data = {
            Direction : direction,
            CallSid : conn.parameters.CallSid,
            To : to,
            From : from,
        }


        $http({
            method: 'POST',
            url: '/rest/save',
            data: data
        }).then( function success(response) {
            if(response.data !== '') {
                share.calls.unshift(response.data);
            }
        });
    }

    function validation(phoneNumber, callPress) {

        phoneNumber = spaceDelete(phoneNumber);
        if(phoneNumber.length === 11){
            $scope.valid = true;
            $scope.invalid = false;
        }else {
            if(phoneNumber.length === 0){
                if(callPress){
                    $scope.invalid = true;
                    $scope.valid = false;
                }else {
                    $scope.invalid = false;
                    $scope.valid = false;
                }
            }else {
                $scope.invalid = true;
                $scope.valid = false;
            }
        }

    }

    function numberInputFormat(phoneNumber) {
        if(phoneNumber.length < 15) {
            phoneNumber = spaceDelete(phoneNumber);
            const formatted = phoneNumber.replace(/(\d{0,1})?(\d{1,2})(\d{1})?(\d{1,3})?(\d{1,4})?/, function (_, p1, p2, p3, p4, p5) {
                let output = "+"
                if (p1) output += `${p1} `;
                if (p2) output += `${p2}`;
                if (p3) output += `${p3}`;
                if (p4) output += ` ${p4}`
                if (p5) output += ` ${p5}`
                return output;
            });
            validation(phoneNumber, false);
            return formatted;
        }else {
            validation(phoneNumber, false);
            return phoneNumber;
        }
    }

    function spaceDelete(phoneNumber) {
        return phoneNumber.replace(/\D/g, "");
    }
});

