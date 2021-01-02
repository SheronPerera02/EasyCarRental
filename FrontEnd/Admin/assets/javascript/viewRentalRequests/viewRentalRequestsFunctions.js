function setSelected(ref) {
    ref.parent().children().css({
        backgroundColor: "white",
        color: "#24c0cd"
    });
    ref.css({
        backgroundColor: "#24c0cd",
        color: "white"
    });
}

function btnEditFunction(ref) {
    let row = ref.parent().parent();

    sessionStorage.setItem("reqId", $(row.children()[0]).text());

    let driverIdSet = $(row.children()[9]).children();
    let driverNameSet = $(row.children()[10]).children();

    $('#tblCurrentDrivers').empty();
    for (let i = 0; i < driverIdSet.length; i++) {
        let row = "<tr>" +
            "<td>" + $(driverIdSet[i]).text() + "</td>" +
            "<td>" + $(driverNameSet[i]).text() + "</td>" +
            "<td></td>" +
            "</tr>";
        $('#tblCurrentDrivers').append(row);
    }


    loadAvailableDrivers();
    currentDriversClickEvent();
    $('.view-rentals').css('display', 'none');
    $('.change-driver').css('display', 'block');
}

function currentDriversClickEvent() {
    $('#tblCurrentDrivers>tr').on('click', function () {
        $('#tblCurrentDrivers>tr').removeClass("selected-row");
        $(this).addClass("selected-row");
        $('#tblCurrentDrivers>tr').css('backgroundColor', 'white');
        $(this).css('backgroundColor', 'lightgrey');
        showReplacements();
        $('#tblCurrentDrivers').css({
            pointerEvents: "none",
            opacity: "0.6"
        });
    });
}

function viewRentals(ref) {
    setSelected(ref);
    viewRentalRequests();
    $('.view-rentals').css('display', 'block');
    $('.change-driver').css('display', 'none');
    $('.calculate-payment').css('display', 'none');
    $('#tblCarsForPayment').empty();
    $('#txtAdditionalPayment').val(null);
}

function viewRequestsForPayment(ref) {
    setSelected(ref);
    loadRequestsToCalculatePayment();
    $('.calculate-payment').css('display', 'block');
    $('.view-rentals').css('display', 'none');
    $('.change-driver').css('display', 'none');

    $('.calculate-payment>div:nth-child(2)').css('display','none');
    $('.calculate-payment>div:nth-child(3)').css('display','none');
    $('#btnFinalizePayment').css('display','none');
}


$('#btnCancel').on('click', function () {
    hideReplacements();
    $('#tblCurrentDrivers>tr>td:nth-child(3)').text(null);
    $('#tblCurrentDrivers>tr').css('backgroundColor', 'white');
    loadAvailableDrivers();
    $('#tblCurrentDrivers').css({
        pointerEvents: "auto",
        opacity: "1"
    });
    $('#tblCurrentDrivers>tr').css({
        pointerEvents: "auto",
        opacity: "1"
    });
});

$('#btnReplace').on('click', function () {
    $('#tblDriverReplacements .selected-row').remove();
    $('#tblCurrentDrivers .selected-row').css({
        pointerEvents: "none",
        opacity: "0.6"
    });
    $('#tblCurrentDrivers>tr').removeClass("selected-row");
    $('#tblCurrentDrivers>tr').css('backgroundColor', 'white');
    hideReplacements();
    $('#tblCurrentDrivers').css({
        pointerEvents: "auto",
        opacity: "1"
    });
});

function hideReplacements() {
    $('.tbl-driver-replacements-container').css({overflowY: 'auto'});
    $('.tbl-driver-replacements-container>div').css('display', 'none');
    $('#btnReplace').css('display', 'none');
}

function showReplacements() {
    $('.tbl-driver-replacements-container').css({overflowY: 'scroll'});
    $('.tbl-driver-replacements-container>div').css('display', 'block');
    $('#btnReplace').css('display', 'block');
}


function loadAvailableDrivers() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/Driver/getAvailableDrivers",
        success: function (resp) {
            $('#tblDriverReplacements').empty();
            for (let obj of resp.data) {
                let row = "<tr>" +
                    "<td>" + obj.id + "</td>" +
                    "<td>" + obj.name + "</td>" +
                    "</tr>";
                $('#tblDriverReplacements').append(row);
            }
            $('#tblDriverReplacements>tr').on('click', function () {
                $($('#tblCurrentDrivers .selected-row').children()[2]).text($($(this).children()[0]).text());
                $('#tblDriverReplacements>tr').removeClass("selected-row");
                $(this).addClass("selected-row");
            });
        }
    });
}


function viewRentalRequests() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/getAllRentalRequests",
        success: function (resp) {
            $('#viewRentalsTable').empty();
            for (let req of resp.data) {

                let drivers = "";
                let names = "";
                let cars = "";
                for (let driverId of req.driverIds) {
                    drivers += "<p>" + driverId + "</p>";
                }
                for (let driverName of req.driverNames) {
                    names += "<p>" + driverName + "</p>";
                }
                for (let car of req.carRegNumbers) {
                    cars += "<p>" + car + "</p>";
                }

                let editButton = "<button onclick='btnEditFunction($(this))' type='button' class='btn btn-primary' style='margin-bottom: 5px'>" +
                    "Edit</button>";

                let acceptDenyButtons = "<button onclick='acceptRequest($(this))' type='button' class='btn btn-success' style='margin-bottom: 5px'>Accept</button>\n" +
                    "                                <br>\n" +
                    "                                <button onclick='denyRequest($(this))' data-target='#denialMessageMdl' data-toggle='modal' type='button' class='btn btn-danger'>Deny</button>";

                editButton = req.rentStatus !== "Pending" ? "-" : editButton;
                acceptDenyButtons = req.rentStatus !== "Pending" ? "-" : acceptDenyButtons;

                let row = "<tr>" +
                    "<td>" + req.requestId + "</td>" +
                    "<td>" + req.durationPlan + "</td>" +
                    "<td>" + getIdWithoutEx(req.userId) + "</td>" +
                    "<td>" + req.pickupDate + "</td>" +
                    "<td>" + req.returnDate + "</td>" +
                    "<td>" + req.pickupVenue + "</td>" +
                    "<td>" + req.returnVenue + "</td>" +
                    "<td>" + req.pickupTime + "</td>" +
                    "<td>" + req.returnTime + "</td>" +
                    "<td>" + drivers + "</td>" +
                    "<td>" + names + "</td>" +
                    "<td>" + editButton + "</td>" +
                    "<td>" + cars + "</td>" +
                    "<td>" + req.rentStatus + "</td>" +
                    "<td>" + req.description + "</td>" +
                    "<td>" + acceptDenyButtons + "</td>" +
                    "</tr>";
                $('#viewRentalsTable').append(row);
            }

        }
    });
}

function getIdWithoutEx(id) {
    let string = "";
    for (let i = 0; i < id.length; i++) {
        if (id.charAt(i) === ".") return string;
        string += id.charAt(i);
    }
}

$('#btnFinalizeDrivers').on('click', function () {
    for (let replacement of $('#tblCurrentDrivers>tr>td:nth-child(3)')) {
        if ($(replacement).text() !== "") {
            finalizeDrivers();
            return;
        }
    }
    alert('Have not done any Replacements!')
});

function finalizeDrivers() {
    let currentDrivers = [];
    let replacements = [];
    for (let i = 0; i < $('#tblCurrentDrivers>tr').length; i++) {
        if ($($($('#tblCurrentDrivers>tr').get(i)).children()[2]).text() !== "") {
            currentDrivers.push($($($('#tblCurrentDrivers>tr').get(i)).children()[0]).text());
            replacements.push($($($('#tblCurrentDrivers>tr').get(i)).children()[2]).text());
        }
    }

    let data = [];
    data.push(currentDrivers);
    data.push(replacements);


    let formData = new FormData();

    formData.append("data", JSON.stringify(data));
    formData.append("reqId", sessionStorage.getItem("reqId"));

    if(confirm("Finalize Drivers?")){
        $.ajax({
            method: "POST",
            url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/changeDrivers",
            contentType: false,
            processData: false,
            data: formData,
            success: function (resp) {
                if (resp.code === 200) {
                    $('#btnViewRentalRequests').click();
                    alert("Replacement Successful!");
                }
            }
        });
    }
}

function acceptRequest(ref) {
    let row = ref.parent().parent();

    let reqId = $(row.children()[0]).text();

    let formData = new FormData();

    formData.append('reqId', reqId);

    if(confirm("Accept Request?")){
        $.ajax({
            method: "POST",
            url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/acceptRequest",
            contentType: false,
            processData: false,
            data: formData,
            success: function (resp) {
                if (resp.code === 200) {
                    $('#btnViewRentalRequests').click();
                    alert("Request Accepted and is now Active!");
                }
            }
        });
    }
}

function denyRequest(ref) {
    let row = ref.parent().parent();
    sessionStorage.setItem("rentalRequestId", $(row.children()[0]).text());
}

function deny(ref) {

    if ($('#txtMessage').val() === "") {
        $('#spnEnterMsg').css({opacity: "1"});
    } else {
        ref.attr('data-dismiss', 'modal');
        $('#spnEnterMsg').css({opacity: "0"});

        let reqId = sessionStorage.getItem("rentalRequestId");
        let formData = new FormData();

        formData.append("message", $('#txtMessage').val());

        $.ajax({
            method: "POST",
            url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/denyRequest/" + reqId,
            contentType: false,
            processData: false,
            data: formData,
            success: function (resp) {
                if (resp.code === 200) {
                    $('#btnViewRentalRequests').click();
                    alert("Request successfully Denied!");
                }
            }
        });

    }

}


function loadRequestsToCalculatePayment() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/getRequestsForPayment",
        success: function (resp) {
            $('#tblCalculatePayment').empty();
            for (let req of resp.data) {

                let drivers = "";
                let names = "";
                for (let driverId of req.driverIds) {
                    drivers += "<p>" + driverId + "</p>";
                }
                for (let driverName of req.driverNames) {
                    names += "<p>" + driverName + "</p>";
                }

                let row = "<tr>" +
                    "<td>" + req.requestId + "</td>" +
                    "<td>" + req.durationPlan + "</td>" +
                    "<td>" + getIdWithoutEx(req.userId) + "</td>" +
                    "<td>" + req.pickupDate + "</td>" +
                    "<td>" + req.returnDate + "</td>" +
                    "<td>" + req.pickupVenue + "</td>" +
                    "<td>" + req.returnVenue + "</td>" +
                    "<td>" + drivers + "</td>" +
                    "<td>" + names + "</td>" +
                    "<td>" + "<button onclick='calculatePayment($(this))' type='button' class='btn btn-primary'>Calculate</button>" + "</td>" +
                    "</tr>";
                $('#tblCalculatePayment').append(row);
            }

        }
    });
}

function calculatePayment(ref) {
    $('.calculate-payment>div:nth-child(2)').css('display','block');
    $('.calculate-payment>div:nth-child(3)').css('display','block');
    $('#btnFinalizePayment').css('display','block');
    let requestId = $(ref.parent().parent().children()[0]).text();
    sessionStorage.setItem("reqId", requestId);
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/getCarsForPayment/" + requestId,
        success: function (resp) {
            $('#tblCarsForPayment').empty();
            for (let car of resp.data) {
                let row = "<tr>" +
                    "<td>" + car.registrationNo + "</td>" +
                    "<td>" + car.brand + "</td>" +
                    "<td>" + car.type + "</td>" +
                    "<td>" + car.color + "</td>" +
                    "<td>" + "<input type='number' class='form-control'>" + "</td>" +
                    "<td>" + "<input type='number' class='form-control'>" + "</td>" +
                    "</tr>";
                $('#tblCarsForPayment').append(row);
            }
        }
    });

}

$('#btnFinalizePayment').on('click', function () {

    let formData = new FormData();

    let carDetails = [];

    for (let row of $('#tblCarsForPayment>tr')) {
        let arr = [];
        arr.push($($(row).children()[0]).text());
        arr.push($($($(row).children()[4]).children()[0]).val());
        arr.push($($($(row).children()[5]).children()[0]).val());
        carDetails.push(arr);
    }

    formData.append("details", JSON.stringify(carDetails));

    let additional = $('#txtAdditionalPayment').val() === "" ? 0 : Number($('#txtAdditionalPayment').val());

    $.ajax({
        method: "POST",
        contentType: false,
        processData: false,
        url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/finalizePayment/" + sessionStorage.getItem("reqId") + "/" + additional,
        data: formData,
        success: function (resp) {
            if (resp.code === 200) {
                let info = "";
                info += resp.data[0] + "\n";
                info += resp.data[1] + "\n";
                info += resp.data[2] + "\n";
                info += resp.data[3] + "\n";
                alert(info);
            }
            $('#btnViewRentalRequests').click();
        }
    });

});
















