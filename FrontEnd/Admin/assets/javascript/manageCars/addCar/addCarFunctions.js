function addCarToTheLot() {
    if(!isValid()){
        return;
    }


    let registrationNo = $('#txtRegistrationNo').val();
    let brand = $('#txtBrand').val();
    let type = $('#cmbType').val();
    let noOfPassengers = $('#cmbNoOfPassengers').val();
    let transmission = $('#cmbTransmissionType').val();
    let fuel = $('#cmbFuelType').val();
    let color = $('#txtColor').val();
    let dailyRate = $('#txtDailyPriceIncrement').val();
    let monthlyRate = $('#txtMonthlyPriceIncrement').val();
    let freeMileagePerDay = $('#txtFreeMileagePerDay').val();
    let freeMileagePerMonth = $('#txtFreeMileagePerMonth').val();
    let pricePerKm = $('#txtPricePerKm').val();
    let kmMeterVal = $('#txtKmMeterValue').val();

    let formData = new FormData();

    for (let file of document.getElementById('frontImg').files) {
        formData.append("files", file);
    }
    for (let file of document.getElementById('backImg').files) {
        formData.append("files", file);
    }
    for (let file of document.getElementById('sideImg').files) {
        formData.append("files", file);
    }
    for (let file of document.getElementById('interiorImg').files) {
        formData.append("files", file);
    }

    formData.append("regNo", registrationNo);

    if(confirm("Add Car?")){
        $.ajax({
            method: "POST",
            contentType: false,
            processData: false,
            url: "http://localhost:8080/EasyCar/api/v1/Car/uploadImages",
            data: formData,
            success: function (resp) {
                if (resp.code === 200) {

                    $.ajax({
                        method: "POST",
                        contentType: "application/json",
                        url: "http://localhost:8080/EasyCar/api/v1/Car/addCar",
                        data: JSON.stringify({
                            "registrationNo": registrationNo,
                            "brand": brand,
                            "type": type,
                            "frontImage": registrationNo + "Front." + resp.data[0],
                            "backImage": registrationNo + "Back." + resp.data[1],
                            "sideImage": registrationNo + "Side." + resp.data[2],
                            "interiorImage": registrationNo + "Interior." + resp.data[3],
                            "numberOfPassengers": noOfPassengers,
                            "transmissionType": transmission,
                            "fuelType": fuel,
                            "color": color,
                            "dailyRate": dailyRate,
                            "monthlyRate": monthlyRate,
                            "freeMileagePerDay": freeMileagePerDay,
                            "freeMileagePerMonth": freeMileagePerMonth,
                            "pricePerKm": pricePerKm,
                            "kmMeterValue": kmMeterVal,
                            "lastReturnDate": "N/A",
                            "isAvailable": "Yes",
                            "isDamaged": "No",
                            "underMaintenance": "No"
                        }),
                        success: function (result) {
                            if (result.code === 200) {
                                alert('Success!');
                                location.reload();
                            } else {
                                alert(result.message);
                            }
                        }
                    });

                } else {
                    alert(resp.message);
                }
            }
        });
    }


}

function isValid() {
    $('.add-car input').css({border:"1px solid lightgrey"});
    for(let element of $('.add-car input')){
        if($(element).val()===""){
            if($(element).hasClass('custom-file-input')){
                alert('Add all images!');
                return false;
            }
            emptyAlert(element);
            return false;
        }
    }
    return true;
}

function emptyAlert(ref) {
    $(ref).css({border:"1px solid red"});
    $(ref).focus();
}


