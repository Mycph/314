function back() {
    window.open("../customer/customer.html", target = "_self");
}

function refreshBtn() {
    var curorder_id = getCookie("oid");
    const req       = new XMLHttpRequest();
    req.open("POST", "current_order", true);
    req.setRequestHeader('Content-Type', 'application/json');
    var jsonObj = new Object();
    jsonObj.oid = curorder_id;
    console.log(curorder_id);
    req.send(JSON.stringify(jsonObj));
    req.onreadystatechange = function () {
        if (req.readyState === 4) {
            console.log(req.response);
            const json                                          = JSON.parse(req.response);
            document.getElementById("orderId").innerHTML        = json.cur_orderid;
            document.getElementById("cusID").innerHTML          = json.O_cusNum;
            document.getElementById("vehicle").innerHTML        = json.vehiclePlate;
            document.getElementById("issue").innerHTML          = json.issue;
            document.getElementById("sstate").innerHTML         = json.sstate;
            document.getElementById("price").innerHTML          = json.EstimatePayment;
            if (json.sstate == "Finished") {
                document.getElementById("pay").style.display = "inline";
            } else {
                document.getElementById("pay").style.display = "none";
            }
        }
    }
}

function payBtn() {
    window.open("../payment/payment.html")
}


