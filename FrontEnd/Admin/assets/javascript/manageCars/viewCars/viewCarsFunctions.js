function viewAllCars() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/Car/getAllCarsForAdmin",
        success: function (result) {
            $('#tblViewCars').empty();

            let frontImage = '';
            let backImage = '';
            let sideImage = '';
            let interiorImage = '';

            for(let car of result.data){
                frontImage= "<img style='width: 150px; height: 100px' src='../CarImages/"+car.frontImage+"'>";
                backImage= "<img style='width: 150px; height: 100px' src='../CarImages/"+car.backImage+"'>";
                sideImage= "<img style='width: 150px; height: 100px' src='../CarImages/"+car.sideImage+"'>";
                interiorImage= "<img style='width: 150px; height: 100px' src='../CarImages/"+car.interiorImage+"'>";

                let row="<tr>" +
                    "<td>"+car.registrationNo+"</td>" +
                    "<td>"+car.brand+"</td>" +
                    "<td>"+car.type+"</td>" +
                    "<td>"+frontImage+"</td>" +
                    "<td>"+backImage+"</td>" +
                    "<td>"+sideImage+"</td>" +
                    "<td>"+interiorImage+"</td>" +
                    "<td>"+car.numberOfPassengers+"</td>" +
                    "<td>"+car.transmissionType+"</td>" +
                    "<td>"+car.fuelType+"</td>" +
                    "<td>"+car.color+"</td>" +
                    "<td>"+car.dailyRate+".00</td>" +
                    "<td>"+car.monthlyRate+".00</td>" +
                    "<td>"+car.freeMileagePerDay+"</td>" +
                    "<td>"+car.freeMileagePerMonth+"</td>" +
                    "<td>"+car.pricePerKm+".00</td>" +
                    "<td>"+car.kmMeterValue+"</td>" +
                    "<td>"+car.isAvailable+"</td>" +
                    "<td>"+car.isDamaged+"</td>" +
                    "<td>"+car.underMaintenance+"</td>" +
                    "</tr>";
                $('#tblViewCars').append(row);
            }
        }
    });
}


