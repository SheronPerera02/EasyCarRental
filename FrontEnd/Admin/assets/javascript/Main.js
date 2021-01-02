function switchToDashboard() {

    $.ajax({
        method:"GET",
        url:"http://localhost:8080/EasyCar/api/v1/Dashboard/dashboardDetails",
        success:function (resp) {
            let obj = resp.data;

            $('#registeredUsers').text(obj.registeredUsers);
            $('#totalBookings').text(obj.totalBookings);
            $('#activeBookings').text(obj.activeBookings);
            $('#availableCars').text(obj.availableCars);
            $('#reservedCars').text(obj.reservedCars);
            $('#carsNeedMaintenance').text(obj.carsNeedMaintenance);
            $('#availableDrivers').text(obj.availableDrivers);
            $('#occupiedDrivers').text(obj.occupiedDrivers);
            $('#carsUnderMaintenance').text(obj.carsUnderMaintenance);

        }
    });

    $('.main-container>section').css({display: "none"});
    $('.dashboard-container').css({display: "block"});
}

function switchToManageCars() {
    /*
    $('#addCar').parent().children().css({
        backgroundColor: "white",
        color: "#24c0cd"
    });
    $('#addCar').css({
        backgroundColor: "#24c0cd",
        color: "white"
    });
     */
    $('#addCar').click();
    $('.main-container>section').css({display: "none"});
    $('.manage-cars-container').css({display: "block"});

}

function switchToViewCustomers() {
    loadAllCustomers();
    $('.main-container>section').css({display: "none"});
    $('.view-customers-container').css({display: "block"});
}

function switchToViewIncome() {
    calculateIncome();
    $('.main-container>section').css({display: "none"});
    $('.calculate-income-container').css({display: "block"});
}

function switchToViewRentalRequests() {
    $('#btnViewRentalRequests').click();
    viewRentalRequests();
    $('.main-container>section').css({display: "none"});
    $('.view-rental-requests-container').css({display: "block"});
}

