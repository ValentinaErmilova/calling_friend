function numberInputFormat() {
    let numberinput =  document.getElementById("inputnumber");
    let phoneNumber = numberinput.value;

    if(phoneNumber.length <= 15) {
        phoneNumber = phoneNumber.replace(/\D/g, "");
        const formatted = phoneNumber.replace(/(\d{0,1})?(\d{1,2})(\d{1})?(\d{1,3})?(\d{1,4})?/, function (_, p1, p2, p3, p4, p5) {
            let output = "+"
            if (p1) output += `${p1} `;
            if (p2) output += `${p2}`;
            if (p3) output += `${p3}`;
            if (p4) output += ` ${p4}`
            if (p5) output += ` ${p5}`
            return output;
        });

        phoneValidation(numberinput,false);

        return formatted;
    }else {
        return phoneNumber;
    }

}

function numberInputUnfocus() {
    let input = document.getElementById("inputnumber");

    phoneValidation(input,true);
}

function phoneValidation(input,unfocus) {
    if(input.value.length === 15){
        input.classList.add("is-valid");
        input.classList.remove("is-invalid");
    }else {
        if (input.value.length === 0){
            input.classList.remove("is-invalid");
            input.classList.remove("is-valid");
        }else {
            if(unfocus) {
                input.classList.add("is-invalid");
                input.classList.remove("is-valid");
            }
        }
    }
}

function deleteLast() {
    let input = document.getElementById("inputnumber");

    let str = input.value;

    if(str.substr(str.length-1,str.length) === ' '){
        str = str.substr(0,str.length-1);
    }

    str = str.substr(0,str.length-1);
    input.value = str;

    phoneValidation(input, true);
}

function getToo() {
    let input = document.getElementById("inputnumber");
    let phoneNumber = input.value;
    return phoneNumber.replace(/\D/g, "");
}

function handleClick(evt) {
    const input = document.getElementById("inputnumber");

    const node = evt.target;

    if(input.value.length < 15) {
        input.value += node.textContent;
        input.value = numberInputFormat();
    }
}