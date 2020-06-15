myApp.controller("FriendController", function($scope, $rootScope, $http) {

    $scope.addF = false;

    if(typeof $scope.user === 'undefined') {
        loadData($http,$rootScope);
    }


    $scope.change = function () {
        $scope.friendNumber = numberInput($scope.friendNumber)
    }

    $scope.unfocus = function () {
        numberInput($scope.friendNumber);
    };

    $scope.callback = function (friend) {
        $rootScope.$emit ('callback',  friend.phonenumber)
    }

    $scope.showAddPanel = function () {
        $scope.addF = true;
    }

    $scope.addFriend = function () {

        let alreadyHave = false;
        let friendNumber = '+'+ spaceDelete($scope.friendNumber);

        $scope.friends.forEach((element) => {
            if (element.phonenumber === friendNumber) {
                $scope.invalidMessage = ALREADYHAVE;
                $scope.valid = false;
                $scope.invalid = true;
                alreadyHave = true;
            }
        })


        if($scope.valid && !alreadyHave) {
            $http({
                method: 'POST',
                url: '/rest/addFriend/'+$rootScope.user.id,
                params : {phoneNumber: '+'+spaceDelete($scope.friendNumber)}
            }).then(function success(response) {
                if (response.data !== '') {
                    $scope.friends.unshift(response.data);
                    $scope.valid = false;
                    $scope.invalid = false;
                    $scope.friendNumber = '';
                } else {

                }
            }, function error(response) {
                $scope.valid = false;
                $scope.invalid = true;
                $scope.invalidMessage = response.data.message;
            });
        }
    }

    $scope.addClose = function(){
        $scope.addF = false;
        $scope.friendNumber = '';
        $scope.valid = false;
        $scope.invalid = false;
        $scope.invalidMessage = '';
    }

    $scope.deleteFriend = function (friend) {

        $http({
            method: 'DELETE',
            url: '/rest/deleteFriend/' + $scope.user.id + '/'+friend.id
        }).then(function success(response) {

            if(response.status === 200) {
                let index = $scope.friends.findIndex(x => x.id === friend.id);
                $scope.friends.splice(index, 1);
            }
        });
    }

    function numberInput(phoneNumber) {
        phoneNumber = inputFormat(phoneNumber);

        let checkResult = validation(phoneNumber,$scope.user.phonenumber);

        setNumberStyle($scope, checkResult, friendPage);

        return phoneNumber;
    }
});