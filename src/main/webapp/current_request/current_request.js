function back() {
    window.open("../professional/professional.html", target = "_self");
}

function finishOrder() {
    // var curorder_id = getCookie("requestID");
    const req = new XMLHttpRequest();
    req.open("POST", "FinishOrder", true);
    req.setRequestHeader('Content-Type', 'application/json');
    var jsonObj         = new Object();
    var currequest_id   = getCookie("requestID");
    jsonObj.requestID   = currequest_id;
    jsonObj.pid         = getCookie("uid");
    jsonObj.coordinates = getCookie("coordinates");
    jsonObj.status      = "finished";
    console.log(currequest_id);
    req.addEventListener('error', event => {
        console.error(event)
    })
    req.onreadystatechange = function (){
        console.log(req.readyState);
        if(req.readyState === 4){
            window.open("../professional/professional.html", "_self");
        }
    }
    req.send(JSON.stringify(jsonObj));

}

function payBtn() {
    window.open("../payment/payment.html")
}
