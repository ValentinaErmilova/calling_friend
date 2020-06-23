// Сontroller for managing friends on ui side
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

    // add friend
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

        // if the phone number is entered correctly and there is no contact in the friends list with this
        // number we are trying to add a user with this number to your contact list
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
                // if the user with the specified phone number does not exist, an error message is displayed
                $scope.valid = false;
                $scope.invalid = true;
                $scope.invalidMessage = response.data.message;
            });
        }
    }

    // close add panel
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