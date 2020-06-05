const myApp = angular.module("myApp", [])
    .service('share', function () {
        let calls;
    });

let currentUser;
myApp.controller("HistoryController", function($scope,$http, share) {

    $http({
        method: 'GET',
        url: '/rest/currentUser'
    }).then(function success(response) {
        $scope.user = response.data;
        currentUser = response.data;
        getCalls();
    })

    function getCalls() {
        $http({
            method: 'GET',
            url: '/rest/getCallsList/' + $scope.user.id
        }).then(function success(response) {
            $scope.calls = response.data;
            share.calls = $scope.calls;

            $scope.styleFunction = function (to, status) {
                if (status === 'no-answer' || status == 'busy') {
                    return 'no-answer';
                } else {
                    let incoming = isInbound(to, $scope.user.id);

                    if (incoming) {
                        return 'incoming';
                    } else {
                        return 'outgoing';
                    }
                }
            }

            $scope.callback = function (call) {
                if(isInbound(call.to,$scope.user.id)){
                    $scope.$broadcast ('callback',  { message: call.fromNumber })
                }else {
                    $scope.$broadcast ('callback',  { message: call.toNumber })
                }
            }

        }, function error(response) {

        });

        function callback(call) {

        }
    }

});

function isInbound(to,from) {
    return to === from;
}