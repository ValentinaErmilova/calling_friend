const VALID = 'valid';
const INVALID = 'invalid';
const SAME = 'same';
const ZERO = 'zero';
const callsPage = 'calls';
const friendPage = 'friends';

const NOTFULL = 'The number is not fully entered';
const SAMENUMBER = 'You can’t call yourself';
const SAMEFRIEND = 'You can’t add yourself to friends';
const ALREADYHAVE = 'You already have such friend'

function validation(phoneNumber, myNumber) {
    phoneNumber = spaceDelete(phoneNumber);

    if(phoneNumber.length === 11) {
        if(phoneNumber === spaceDelete(myNumber)){
            return SAME
        }else {
            return VALID
        }
    }else {
        if(phoneNumber.length === 0){
            return ZERO;
        }else {
            return INVALID;
        }
    }
}

function spaceDelete(phoneNumber) {
    return phoneNumber.replace(/\D/g, "");
}

function inputFormat(phoneNumber) {
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
        return formatted;
    }else {
        return phoneNumber;
    }
}

function setNumberStyle($scope, styleType, pageName) {
    if(styleType === VALID){
        $scope.valid = true;
        $scope.invalid = false;
        $scope.invalidMessage = NOTFULL;
    }
    if (styleType === INVALID){
        $scope.valid = false;
        $scope.invalid = true;
        $scope.invalidMessage = NOTFULL;
    }
    if(styleType === ZERO){
        $scope.valid = false;
        $scope.invalid = false;
        $scope.invalidMessage = NOTFULL;
    }

    if(styleType === SAME){
        $scope.valid = false;
        $scope.invalid = true;
        if(pageName === callsPage) {
            $scope.invalidMessage = SAMENUMBER;
        }else {
            $scope.invalidMessage = SAMEFRIEND
        }
    }
}