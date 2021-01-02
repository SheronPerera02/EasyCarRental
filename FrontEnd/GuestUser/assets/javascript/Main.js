$('#btnRegisterNow').on('click', function () {
    $('.sign-in-container').css({display: 'none'});
    $('.register-now-container').css({display: 'block'});
    $('#txtEmail').focus();
});

$('#signInButton').on('click', function () {
    $('.register-now-container').css({display: 'none'});
    $('.sign-in-container').css({display: 'block'});
    $('#txtLoginNic').focus();
});

$('#btnRegisterNowPanelCancel').on('click', function () {
    $('.register-now-container').css({display: 'none'});
    let fields = $('.register-now-panel-body .form-control');
    for (let i = 0; i < fields.length; i++) {
        $(fields.get(i)).val(null);
    }
});

$('#btnSignInCancel').on('click', function () {
    $('.sign-in-container').css({display: 'none'});
    let fields = $('.sign-in-panel-body .form-control');
    for (let i = 0; i < fields.length; i++) {
        $(fields.get(i)).val(null);
    }
});

$('#btnSignIn').on('click', function () {
    login();
});


function loadAllCars() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/Car/getAllCars",
        success: function (result) {
            $('#tblGuest').empty();

            let frontImage = '';
            let backImage = '';
            let sideImage = '';
            let interiorImage = '';


            for (let car of result.data) {
                if (car.isAvailable === "Yes") {
                    frontImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.frontImage + "'>";
                    backImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.backImage + "'>";
                    sideImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.sideImage + "'>";
                    interiorImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.interiorImage + "'>";

                    let row = "<tr>" +
                        "<td>" + frontImage + "</td>" +
                        "<td>" + backImage + "</td>" +
                        "<td>" + sideImage + "</td>" +
                        "<td>" + interiorImage + "</td>" +
                        "<td>" + car.dailyRate + ".00</td>" +
                        "<td>" + car.monthlyRate + ".00</td>" +
                        "<td>" + car.freeMileagePerDay + "</td>" +
                        "<td>" + car.freeMileagePerMonth + "</td>" +
                        "<td>" + car.pricePerKm + ".00</td>" +
                        "</tr>";
                    $('#tblGuest').append(row);
                }
            }
        }
    });
}

$('#btnRegister').on('click', function () {
    if (emptyFieldExists()) {
        alert('Fill The Whole Form!');
    } else {
        registerUser();
    }
});

function emptyFieldExists() {
    let fields = $('.register-now-panel-body .form-control');
    for (let i = 0; i < fields.length; i++) {
        if ($(fields.get(i)).val() === "") {
            return true;
        }
    }
}

function registerUser() {
    let nic = $('#txtNic').val();
    let email = $('#txtEmail').val();
    let password = $('#txtPassword').val();
    let address = $('#txtAddress').val();
    let contact = $('#txtContact').val();

    let formData = new FormData();

    for (let file of document.getElementById('imgNic').files) {
        formData.append("file", file);
    }

    formData.append("nic", nic);

    $.ajax({
        method: "POST",
        contentType: false,
        processData: false,
        url: "http://localhost:8080/EasyCar/api/v1/User/uploadIdImage",
        data: formData,
        success: function (resp) {
            if (resp.code === 200) {

                $.ajax({
                    method: "POST",
                    contentType: "application/json",
                    url: "http://localhost:8080/EasyCar/api/v1/User/registerUser",
                    data: JSON.stringify({
                        "nic": nic,
                        "email": email,
                        "password": password,
                        "idPhoto": nic + "." + resp.data,
                        "address": address,
                        "contact": contact
                    }),
                    success: function (resp) {
                        if (resp.code === 200) {
                            alert('Registered!');
                            $('#txtLoginNic').val(nic);
                            $('#txtLoginPassword').val(password);
                            $('#btnSignIn').click();
                        } else {
                            alert(resp.message);
                        }
                    }
                });

            } else {
                alert(resp.message);
            }
        }
    });
}


function login() {

    let formData = new FormData();

    formData.append("nic", $('#txtLoginNic').val());
    formData.append("password", $('#txtLoginPassword').val());


    $.ajax({
        method: "POST",
        contentType: false,
        processData: false,
        url: "http://localhost:8080/EasyCar/api/v1/User/login",
        data: formData,
        success: function (resp) {
            if (resp.code === 200) {
                sessionStorage.setItem("nic", resp.data);
                $('#loginLink').attr('href', '../RegisteredUser/index.html');
                $('#btnSignIn').click();
            } else {
                alert(resp.message);
            }
        }
    });
}













