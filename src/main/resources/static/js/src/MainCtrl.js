//installation of controllers, setting up switching between call history and friends list
let myApp = angular.module('myApp', ['ngRoute'])
    .config(function($routeProvider) {
            $routeProvider
                .when('/calls', {
                    templateUrl: 'calls',
                    controller: 'HistoryController'
                })
                .when('/friends', {
                    templateUrl: 'friends',
                    controller: 'FriendController'
                })
                .otherwise({redirectTo: '/calls'});
    });

myApp.controller('MainCtrl', function ($window, $scope, $http) {
    $scope.logout = function () {
        $http({
            method: 'POST',
            url: '/rest/logout'
        })

        $window.location.href = '/login';
    }
});

// function to request a list of ringing and friends
function loadData($http,$scope) {
    $http({
        method: 'GET',
        url: '/rest/currentUser'
    }).then(function success(response) {

        $scope.user = response.data;

        $http({
            method: 'GET',
            url: '/rest/callsList/' + $scope.user.id
        }).then(function success(response) {
            $scope.calls = response.data;
        });

        $http({
            method: 'GET',
            url: '/rest/getFriends/' + $scope.user.id
        }).then(function success(response) {
            $scope.friends = response.data;
        });
    })
}