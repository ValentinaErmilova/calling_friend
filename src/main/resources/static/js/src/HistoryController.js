const app = angular.module("myApp", []);
app.controller("HistoryController", function($scope,$http) {
    let myPhone = document.getElementById("from").textContent;

    $http({
        method: 'GET',
        url: '/rest/getCallsList'+myPhone
    }).then(function successCallback(response) {
        $scope.calls = response.data;

        $scope.styleFunction = function (to, status) {
            if(status !== 'no-answer') {
                let incoming = myPhone === to;

                if (incoming) {
                    return 'incoming';
                } else {
                    return 'outgoing';
                }
            }else {
                return 'no-answer';
            }
        }

        $scope.callback = function(to, from){
            if(myPhone === from){
                outgoingCallWithNumber(to);
            }else {
                outgoingCallWithNumber(from);
            }
        }

    }, function errorCallback(response) {

    });
});