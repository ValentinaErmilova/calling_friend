// controller for call list
myApp.controller('HistoryController',
    function HistoryController($scope, $rootScope, $http){

        if(typeof $scope.user === 'undefined') {
            loadData($http,$rootScope);
        }

        // set the style for the call, depending on its type
        $scope.styleFunction = function (call) {

            let to = call.to;
            let status = call.status;

            if (status === 'no-answer' || status === 'busy') {
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

        // we call back from the history of the call, transfer control to CallController
        $scope.callback = function (call) {
            if(isInbound(call.to,$scope.user.id)){
                $rootScope.$emit ('callback', call.fromNumber)
            }else {
                $rootScope.$emit ('callback', call.toNumber)
            }
        }
    }
)

function isInbound(to,from) {
    return to === from;
}