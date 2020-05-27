function addTestingCallsInfo() {
    let content = document.getElementById("calls");

    for(let i = 0; i < 30; i++) {

        let div = document.createElement("div");

        div.classList.add("call-row");

        let from = document.createElement("label");
        from.textContent = "\"from\"";

        let br = document.createElement("br")

        let status = document.createElement("img");
        status.classList.add("call-status-icon");
        status.src = "https://cdn.icon-icons.com/icons2/806/PNG/512/phone-1_icon-icons.com_65969.png";

        let time = document.createElement("label");
        time.textContent = "\"20.02.2020 22:34\"";
        time.classList.add("call-element");

        let duration = document.createElement("label");
        duration.textContent = "\"100sec\"";
        duration.classList.add("call-element");


        let recall = document.createElement("button");
        recall.textContent = "call";

        div.appendChild(from);
        div.appendChild(br);
        div.appendChild(status);
        div.appendChild(time);
        div.appendChild(duration);
        div.appendChild(recall);

        content.appendChild(div);
    }
}

function numberInputFormat() {
    let numberinput =  document.getElementById("inputnumber");
    var phoneNumber = numberinput.value;

    if(phoneNumber.length <= 15) {
        phoneNumber = phoneNumber.replace(/\D/g, "");
        var formatted = phoneNumber.replace(/(\d{0,1})?(\d{1,2})(\d{1})?(\d{1,3})?(\d{1,4})?/, function (_, p1, p2, p3, p4, p5) {
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
    if(input.value.length == 15){
        input.classList.add("is-valid");
        input.classList.remove("is-invalid");
    }else {
        if (input.value.length == 0){
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

    if(str.substr(str.length-1,str.length) == ' '){
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
    var input = document.getElementById("inputnumber");

    var node = evt.target;

    if(input.value.length < 15) {
        input.value += node.textContent;
        input.value = numberInputFormat();
    }
}