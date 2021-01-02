checkRentStatus();

function loadAllCars() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/Car/getAllCars",
        success: function (result) {
            $('#tblUserViewCars').empty();

            let frontImage = '';
            let backImage = '';
            let sideImage = '';
            let interiorImage = '';

            let chooseButton = "<button style=\"font-family: 'DejaVu Sans'\" type='button' " +
                "class='btn btn-danger' onclick='carSelected($(this))'>Select</button>";

            for (let car of result.data) {
                if (car.isAvailable === "Yes") {
                    frontImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.frontImage + "'>";
                    backImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.backImage + "'>";
                    sideImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.sideImage + "'>";
                    interiorImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.interiorImage + "'>";

                    let row = "<tr>" +
                        "<td style='display: none'>" + car.registrationNo + "</td>" +
                        "<td>" + frontImage + "</td>" +
                        "<td>" + backImage + "</td>" +
                        "<td>" + sideImage + "</td>" +
                        "<td>" + interiorImage + "</td>" +
                        "<td>" + car.brand + "</td>" +
                        "<td>" + car.type + "</td>" +
                        "<td>" + car.transmissionType + "</td>" +
                        "<td>" + car.fuelType + "</td>" +
                        "<td>" + car.dailyRate + ".00</td>" +
                        "<td>" + car.monthlyRate + ".00</td>" +
                        "<td>" + car.freeMileagePerDay + "</td>" +
                        "<td>" + car.freeMileagePerMonth + "</td>" +
                        "<td>" + car.pricePerKm + ".00</td>" +
                        "<td>" + chooseButton + "</td>" +
                        "</tr>";
                    $('#tblUserViewCars').append(row);
                }
            }
        }
    });
}

function carSelected(ref) {

    let selectedRow = ref.parent().parent();

    let frontImage = "<img style='width: 150px; height: 100px' src='" + $(selectedRow.children().get(1)).children().attr('src') + "'>";

    let waiverPaymentAmount = "";

    let carType = $(selectedRow.children().get(6)).text();

    if (carType === "General") {
        waiverPaymentAmount = "10000.00";
    } else if (carType === "Premium") {
        waiverPaymentAmount = "15000.00";
    } else {
        waiverPaymentAmount = "20000.00"
    }

    let rdoNoEventParams = "$(this), $($($(this).parent().parent().children()[0]).children()[0])";

    let rdoYesEventParams = "$(this), $($($(this).parent().parent().children()[1]).children()[0])";

    let waiverProof = "<div class='row justify-content-around'>\n" +
        "                                <div class='custom-file' style='width: 200px'>\n" +
        "                                    <label class='custom-file-label'>Photo</label>\n" +
        "                                    <input type='file' class='custom-file-input form-control'>\n" +
        "                                </div>\n" +
        "                            </div>";

    let radioButtons = "<div class='form-check form-check-inline'>\n" +
        "                                <input onchange='uncheckTheOtherRadio(" + rdoYesEventParams + ")' class='form-check-input rdo-yes' type='checkbox' value='option1' checked>\n" +
        "                                <label class='form-check-label'>Yes</label>\n" +
        "                            </div>\n" +
        "                            <div class=\"form-check form-check-inline\">\n" +
        "                                <input onchange='uncheckTheOtherRadio(" + rdoNoEventParams + ")' class='form-check-input rdo-no' type='checkbox' value='option2'>\n" +
        "                                <label class='form-check-label'>No</label>\n" +
        "                            </div>";

    let row = "<tr>" +
        "<td style='display: none'>" + $(selectedRow.children().get(0)).text() + "</td>" +
        "<td>" + frontImage + "</td>" +
        "<td>" + waiverPaymentAmount + "</td>" +
        "<td>" + waiverProof + "</td>" +
        "<td>" + radioButtons + "</td>" +
        "</tr>";


    $('#tblRentCar').append(row);
    selectedRow.remove();
    $('.view-to-rent-cars').css('display', 'none');
    $('.rent-car-panel').css('display', 'block');
}

$('#btnMyAccount').on('click', function () {
    $('.my-rents-panel').css('display', 'none');
    $('.rent-car-panel').css('display', 'none');
    $('.view-to-rent-cars').css('display', 'none');
    $('.update-profile-panel').css('display', 'block');
    getAccountDetails();
});

$('#btnMyRent').on('click', function () {
    $('.rent-car-panel').css('display', 'none');
    $('.view-to-rent-cars').css('display', 'none');
    $('.update-profile-panel').css('display', 'none');
    $('.my-rents-panel').css('display', 'block');
});

function goBack() {
    loadAllCars();
    $('.rent-car-panel').css('display', 'none');
    $('.update-profile-panel').css('display', 'none');
    $('.my-rents-panel').css('display', 'none');
    $('.view-to-rent-cars').css('display', 'block');
    $('#tblRentCar').empty();
    $('#pickupDate').val(null);
    $('#pickupTime').val(null);
    $('#pickupVenue').val(null);
    $('#returnDate').val(null);
    $('#returnTime').val(null);
    $('#returnVenue').val(null);
    $('#cmbDurationPlan').val("Daily");
}

$('#btnAddMoreCars').on('click', function () {
    if ($('#tblUserViewCars>tr').length === 0) {
        alert('No more Cars available!')
    } else {
        $('.rent-car-panel').css('display', 'none');
        $('.view-to-rent-cars').css('display', 'block');
    }
});


$('.rdo-no').on('change', function () {
    uncheckTheOtherRadio();
});

$('.rdo-yes').on('change', function () {
    uncheckTheOtherRadio();
});


function uncheckTheOtherRadio(thisRadio, otherRadio) {
    if (thisRadio.is(':checked')) {
        otherRadio.prop('checked', false);
    } else if (!thisRadio.is(':checked')) {
        otherRadio.prop('checked', true);
    }
}

function emptyFieldsExist(){
    return $('#pickupDate').val()===""||$('#pickupTime').val()===""||$('#pickupVenue').val()===""||$('#cmbDurationPlan').val()===""||
        $('#returnDate').val()===""||$('#returnTime').val()===""||$('#returnVenue').val()==="";
}

$('#btnFinalizeRent').on('click', function () {

    let durationPlan = $('#cmbDurationPlan').val();
    let userId = sessionStorage.getItem('nic');
    let pickupDate = $('#pickupDate').val();
    let pickupTime = $('#pickupTime').val();
    let pickupVenue = $('#pickupVenue').val();
    let returnDate = $('#returnDate').val();
    let returnTime = $('#returnTime').val();
    let returnVenue = $('#returnVenue').val();

    if(emptyFieldsExist()){
        alert("Cannot leave Empty fields!");
        return;
    }

    let formData1 = new FormData();

    let formData2 = new FormData();



        $.ajax({
            method: "GET",
            url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/getNextId",
            success: function (resp) {
                if (resp.code === 200) {
                    for (let i = 0; i < $('#tblRentCar>tr').length; i++) {
                        let iterableFiles = $($($('#tblRentCar>tr')[i]).children().get(3)).children().children().children().get(1).files;
                        if($($($($('#tblRentCar>tr')[i]).children().get(3)).children().children().children().get(1)).val()===""){
                            alert("Waiver Payment Proof Required!");
                            return;
                        }
                        for (let file of iterableFiles) {
                            formData1.append("proofImages", file);
                            formData1.append("fileNames", resp.data + "-" + $($($('#tblRentCar>tr')[i]).children().get(0)).text());
                        }
                    }
                    if(confirm("Finalize Rent?")){
                        $.ajax({
                            method: "POST",
                            contentType: false,
                            processData: false,
                            url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/uploadProofImages",
                            data: formData1,
                            success: function (result) {
                                if (result.code === 200) {
                                    let rentalRequestObj = {
                                        "requestId": resp.data,
                                        "durationPlan": durationPlan,
                                        "userId": userId,
                                        "pickupDate": pickupDate,
                                        "pickupTime": pickupTime,
                                        "pickupVenue": pickupVenue,
                                        "returnDate": returnDate,
                                        "returnTime": returnTime,
                                        "returnVenue": returnVenue,
                                        "rentStatus": "Pending",
                                        "description": ""
                                    };
                                    formData2.append("rentalRequest", JSON.stringify(rentalRequestObj));

                                    let rentedCarsArr = [];
                                    let count = 0;
                                    let driverNeeded = "";
                                    for (let row of $('#tblRentCar>tr')) {
                                        if ($($($($(row).children()[4]).children()[0]).children()[0]).is(':checked')) {
                                            driverNeeded = "Yes";
                                        } else {
                                            driverNeeded = "No";
                                        }
                                        rentedCarsArr.push({
                                            "id": resp.data + $($(row).children()[0]).text(),
                                            "carRegistrationNo": $($(row).children()[0]).text(),
                                            "rentalReqId": resp.data,
                                            "waiverAmount": $($(row).children()[2]).text(),
                                            "waiverProof": resp.data + "-" + $($(row).children()[0]).text() + "." + result.data[count],
                                            "driverNeeded": driverNeeded
                                        });
                                        count++;
                                    }
                                    formData2.append("rentedCars", JSON.stringify(rentedCarsArr));

                                    $.ajax({
                                        method: "POST",
                                        contentType: false,
                                        processData: false,
                                        url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/addRentalRequest",
                                        data: formData2,
                                        success: function (response) {
                                            if (response.code === 200) {
                                                alert('Success!');
                                                location.reload();
                                            } else {
                                                alert(response.message);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });

});


function checkRentStatus() {

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/checkRentStatus/" + sessionStorage.getItem("nic"),
        success: function (resp) {
            if (resp.message === "Pending") {
                sessionStorage.setItem("requestId", resp.data);
                $('.btn-go-back-my-rents>button,.btn-go-back-update-profile>button').attr('disabled', 'disabled');
                $('.request-status-container').css('backgroundColor', '#007bff');
                $('.request-status-container h1').text("Pending");
                hideUnnecessaryPanels();
            } else if (resp.message === "Rejected") {
                $('#denialMessage').text(resp.data);
                $('.request-status-container').css('backgroundColor', 'crimson');
                $('.request-status-container h1').text("Rejected");
                hideUnnecessaryPanels();
                $('#btnCancelReq').css('display', 'none');
                $('.driver-contact-table-container').css('display', 'none');
                $('.rejection-reason-container').css('display', 'block');
            } else if (resp.message === "Active") {
                for (let driver of resp.data) {
                    $('#tblDriverContacts').append("<tr>" +
                        "<td style='display: none'>" + driver.id + "</td>" +
                        "<td>" + driver.name + "</td>" +
                        "<td>" + driver.contact + "</td>" +
                        "</tr>");
                }
                $('.btn-go-back-my-rents>button,.btn-go-back-update-profile>button').attr('disabled', 'disabled');
                $('.request-status-container').css('backgroundColor', '#28a745');
                $('.request-status-container h1').text("Active");
                hideUnnecessaryPanels();
                $('#btnCancelReq').css('display', 'none');
            } else {
                $('#btnMyRent').css('display', 'none');
            }
        }
    });
}

function hideUnnecessaryPanels(){
    $('.rent-car-panel').css('display', 'none');
    $('.view-to-rent-cars').css('display', 'none');
    $('.update-profile-panel').css('display', 'none');
    $('.my-rents-panel').css('display', 'block');
}

$('#btnCancelReq').on('click', function () {

    if(confirm("Cancel Request?")){
        $.ajax({
            method: "DELETE",
            url: "http://localhost:8080/EasyCar/api/v1/RentalRequest/cancelRequest/" + sessionStorage.getItem("requestId"),
            success: function (resp) {
                if (resp.code === 200) {
                    location.reload();
                    alert("Cancelled!");
                } else {
                    alert(resp.message);
                }
            }
        });
    }
});


function Navigate() {
    if (confirm("Sign Out?")) {
        window.location.replace('http://localhost:63342/EasyCarRental/FrontEnd/GuestUser/index.html?_ijt=p15qkqq6ida526vkmeb0486g1v');
    }
}


function updateAccount() {

    if ($('#imgNic').val() === "") {
        updateDetails(sessionStorage.getItem("idPhoto"));
    } else {
        let formData = new FormData();

        for (let file of $('#imgNic')[0].files) {
            formData.append("file", file);
        }
        formData.append("nic", sessionStorage.getItem("nic"));

        $.ajax({
            method: "POST",
            url: "http://localhost:8080/EasyCar/api/v1/User/updateIdPhoto",
            contentType: false,
            processData: false,
            data: formData,
            success: function (resp) {
                updateDetails(sessionStorage.getItem("nic") + "." + resp.data);
            }
        });
    }


}

function updateDetails(fileName) {

    $.ajax({
        method: "PUT",
        url: "http://localhost:8080/EasyCar/api/v1/User/updateUser",
        contentType: "application/json",
        data: JSON.stringify({
            "nic": sessionStorage.getItem("nic"),
            "email": $('#txtEmail').val(),
            "password": $('#txtPassword').val(),
            "idPhoto": fileName,
            "address": $('#txtAddress').val(),
            "contact": $('#txtContact').val()
        }),
        success: function (resp) {
            if (resp.code === 200) {
                alert('Account updated Successfully!');
            }
        }
    });

}

function getAccountDetails() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/User/getAccountDetails/" + sessionStorage.getItem("nic"),
        success: function (resp) {
            $('#txtAddress').val(resp.data.address);
            $('#txtContact').val(resp.data.contact);
            $('#txtEmail').val(resp.data.email);
            $('#txtPassword').val(resp.data.password);
            sessionStorage.setItem("idPhoto", resp.data.idPhoto);
        }
    });
}


$('#rdoShowPassword').on('click', function () {
    let isChecked = $('#rdoShowPassword').is(':checked');
    if (isChecked) {
        $('#txtPassword').attr({type: "text"});
    } else {
        $('#txtPassword').attr({type: "password"});
    }
});


$('#btnCancel').on('click', function () {
    getAccountDetails();
});

