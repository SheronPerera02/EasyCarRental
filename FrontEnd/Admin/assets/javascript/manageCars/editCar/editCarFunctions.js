function viewToEditCars() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/Car/getAllCarsForAdmin",
        success: function (result) {
            $('#tblEditCars').empty();

            let edit =
                "<button onclick=\"setValues($(this))\" class=\"btn btn-primary\" data-target=\"#editCarMdl\" data-toggle=\"modal\" type=\"button\">" +
                "Edit</button>";

            let markDamaged =
                "<button type=\"button\" class=\"btn btn-warning\" onclick=\"markDamaged($(this))\">Mark</button>";

            let addToMaintenance =
                "<button type=\"button\" class=\"btn btn-success\" onclick=\"addToMaintenance($(this))\">Add</button>";

            let markRepaired =
                "<button type=\"button\" class=\"btn btn-primary\" onclick=\"markRepaired($(this))\">Mark Repaired</button>";

            let remove =
                "<button type=\"button\" class=\"btn btn-danger\" onclick=\"removeCar($(this))\">Remove</button>";

            let dash = "-";

            let markDamagedButton = "";

            let markRepairedOrAddToMaintenanceButton = "";

            let frontImage = '';
            let backImage = '';
            let sideImage = '';
            let interiorImage = '';

            for (let car of result.data) {
                markDamagedButton = car.isDamaged === "Yes" ? dash : markDamaged;
                markRepairedOrAddToMaintenanceButton = car.isDamaged === "Yes" && car.underMaintenance === "Yes" ? markRepaired
                    : car.isDamaged === "Yes" ? addToMaintenance : dash;

                frontImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.frontImage + "'>";
                backImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.backImage + "'>";
                sideImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.sideImage + "'>";
                interiorImage = "<img style='width: 150px; height: 100px' src='../CarImages/" + car.interiorImage + "'>";

                let row = "<tr>" +
                    "<td>" + car.registrationNo + "</td>" +
                    "<td>" + frontImage + "</td>" +
                    "<td>" + backImage + "</td>" +
                    "<td>" + sideImage + "</td>" +
                    "<td>" + interiorImage + "</td>" +
                    "<td>" + car.color + "</td>" +
                    "<td>" + car.dailyRate + ".00</td>" +
                    "<td>" + car.monthlyRate + ".00</td>" +
                    "<td>" + car.freeMileagePerDay + "</td>" +
                    "<td>" + car.freeMileagePerMonth + "</td>" +
                    "<td>" + car.pricePerKm + ".00</td>" +
                    "<td>" +
                    edit
                    + "</td>" +
                    "<td>" +
                    markDamagedButton
                    + "</td>" +
                    "<td>" +
                    markRepairedOrAddToMaintenanceButton
                    + "</td>" +
                    "<td>" +
                    remove
                    + "</td>" +
                    "</tr>";
                $('#tblEditCars').append(row);
            }
        }
    });
}

function setValues(ref) {
    $('#txtRegNo').val($(ref.parent().parent().children()[0]).text());
    $('#editCarColor').val($(ref.parent().parent().children()[5]).text());
    $('#editDailyRate').val($(ref.parent().parent().children()[6]).text());
    $('#editMonthlyRate').val($(ref.parent().parent().children()[7]).text());
    $('#editFreeMileagePerDay').val($(ref.parent().parent().children()[8]).text());
    $('#editFreeMileagePerMonth').val($(ref.parent().parent().children()[9]).text());
    $('#editPricePerKm').val($(ref.parent().parent().children()[10]).text());

    $('#imgFront').attr('src', $($(ref.parent().parent().children().get(1)).children()).attr('src'));
    $('#imgBack').attr('src', $($(ref.parent().parent().children().get(2)).children()).attr('src'));
    $('#imgSide').attr('src', $($(ref.parent().parent().children().get(3)).children()).attr('src'));
    $('#imgInterior').attr('src', $($(ref.parent().parent().children().get(4)).children()).attr('src'));
}

function markDamaged(ref) {
    let regNo = $(ref.parent().parent().children()[0]).text();

    $.ajax({
        method: "PUT",
        url: "http://localhost:8080/EasyCar/api/v1/Car/markDamaged/" + regNo,
        success: function (result) {
            viewToEditCars();
            if (result.code === 200) {
                alert('Done!');
            }
        }
    });
}

function addToMaintenance(ref) {
    let regNo = $(ref.parent().parent().children()[0]).text();

    $.ajax({
        method: "PUT",
        url: "http://localhost:8080/EasyCar/api/v1/Car/addToMaintenance/" + regNo,
        success: function (result) {
            viewToEditCars();
            if (result.code === 200) {
                alert('Done!');
            } else{
                alert(result.message);
            }
        }
    });
}

function markRepaired(ref) {
    let regNo = $(ref.parent().parent().children()[0]).text();

    $.ajax({
        method: "PUT",
        url: "http://localhost:8080/EasyCar/api/v1/Car/markRepaired/" + regNo,
        success: function (result) {
            viewToEditCars();
            if (result.code === 200) {
                alert('Done!');
            }
        }
    });
}

function removeCar(ref) {
    let regNo = $(ref.parent().parent().children()[0]).text();

    if (confirm("Confirm Remove Car - " + regNo)) {
        $.ajax({
            method: "DELETE",
            url: "http://localhost:8080/EasyCar/api/v1/Car/removeCar/" + regNo,
            success: function (result) {
                viewToEditCars();
                if (result.code === 200) {
                    alert('Removed!');
                }
            }
        });
    }
}

function updateCar() {
    let formData = new FormData();

    let regNo = $('#txtRegNo').val();
    let color = $('#editCarColor').val();
    let dailyRate = $('#editDailyRate').val();
    let monthlyRate = $('#editMonthlyRate').val();
    let freeMileagePerDay = $('#editFreeMileagePerDay').val();
    let freeMileagePerMonth = $('#editFreeMileagePerMonth').val();
    let pricePerKm = $('#editPricePerKm').val();

    if (
        regNo === "" || color === "" || dailyRate === "" || monthlyRate === "" ||
        freeMileagePerDay === "" || freeMileagePerMonth === "" || pricePerKm === ""
    ) {
        alert("Cannot Leave Empty Fields!");
    } else {

        let frontImg = $('#imgFront').attr('src').split("../CarImages/")[1];
        let backImg = $('#imgBack').attr('src').split("../CarImages/")[1];
        let sideImg = $('#imgSide').attr('src').split("../CarImages/")[1];
        let interiorImg = $('#imgInterior').attr('src').split("../CarImages/")[1];

        let updatingImages = [];

        for (let file of document.getElementById('editFrontImg').files) {
            if ($('#editFrontImg').val() !== "") {
                formData.append("files", file);
                formData.append("updateFileNames", getFilePrefix($('#imgFront').attr('src')));
                formData.append("deletableFilesNames", $('#imgFront').attr('src').split("../CarImages/")[1]);
                frontImg = regNo + "Front.";
                updatingImages.push(0);
            }
        }
        for (let file of document.getElementById('editBackImg').files) {
            if ($('#editBackImg').val() !== "") {
                formData.append("files", file);
                formData.append("updateFileNames", getFilePrefix($('#imgBack').attr('src')));
                formData.append("deletableFilesNames", $('#imgBack').attr('src').split("../CarImages/")[1]);
                backImg = regNo + "Back.";
                updatingImages.push(1);
            }
        }
        for (let file of document.getElementById('editSideImg').files) {
            if ($('#editSideImg').val() !== "") {
                sideImg = regNo + "Side.";
                formData.append("files", file);
                formData.append("updateFileNames", getFilePrefix($('#imgSide').attr('src')));
                formData.append("deletableFilesNames", $('#imgSide').attr('src').split("../CarImages/")[1]);
                updatingImages.push(2);
            }
        }
        for (let file of document.getElementById('editInteriorImg').files) {
            if ($('#editInteriorImg').val() !== "") {
                formData.append("files", file);
                formData.append("updateFileNames", getFilePrefix($('#imgInterior').attr('src')));
                formData.append("deletableFilesNames", $('#imgInterior').attr('src').split("../CarImages/")[1]);
                interiorImg = regNo + "Interior.";
                updatingImages.push(3);
            }
        }

        if (allImgReplacementsNull()) {
            formData.append("updateFileNames", "");
            formData.append("deletableFilesNames", "");
        }

        let allImages = [frontImg, backImg, sideImg, interiorImg];

        $.ajax({
            method: "POST",
            contentType: false,
            processData: false,
            url: "http://localhost:8080/EasyCar/api/v1/Car/updateImages",
            data: formData,
            success: function (resp) {
                if (resp.code === 200) {
                    for (let i = 0; i < resp.data.length; i++) {
                        allImages[updatingImages[i]] += resp.data[i];
                    }
                    $.ajax({
                        method: "PUT",
                        contentType: "application/json",
                        url: "http://localhost:8080/EasyCar/api/v1/Car/updateCar",
                        data: JSON.stringify({
                            "registrationNo": regNo,
                            "brand": null,
                            "type": null,
                            "frontImage": allImages[0],
                            "backImage": allImages[1],
                            "sideImage": allImages[2],
                            "interiorImage": allImages[3],
                            "numberOfPassengers": 0,
                            "transmissionType": null,
                            "fuelType": null,
                            "color": color,
                            "dailyRate": dailyRate,
                            "monthlyRate": monthlyRate,
                            "freeMileagePerDay": freeMileagePerDay,
                            "freeMileagePerMonth": freeMileagePerMonth,
                            "pricePerKm": pricePerKm,
                            "kmMeterValue": 0,
                            "lastReturnDate": null,
                            "isAvailable": null,
                            "isDamaged": null,
                            "underMaintenance": null
                        }),
                        success: function (result) {
                            if (result.code === 200) {
                                $('#btnCancelUpdateCar').click();
                                viewToEditCars();
                                alert('Success!');
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

function allImgReplacementsNull() {
    return $('#editFrontImg').val() === ""
        &&
        $('#editBackImg').val() === ""
        &&
        $('#editSideImg').val() === ""
        &&
        $('#editInteriorImg').val() === "";
}

function getFilePrefix(path) {
    let fullFileName = path.split("../CarImages/")[1];

    let filePrefix = "";

    for (let i = 0; i < fullFileName.length; i++) {
        filePrefix += fullFileName[i];

        if (fullFileName[i] === ".") {
            break;
        }
    }
    return filePrefix;
}
