function calculateIncome(){
    $('#year').text(new Date().getFullYear());
    $.ajax({
        method:"GET",
        url:"http://localhost:8080/EasyCar/api/v1/RentalRequest/getIncome/"+new Date().getFullYear(),
        success:function (resp) {
            let data = resp.data;
            $('#dailyIncome').text(data[0]+"/=");
            $('#weeklyIncome').text(data[1]+"/=");
            $('#monthlyIncome').text(data[2]+"/=");
            $('#yearlyIncome').text(data[3]+"/=");
        }
    });
}
