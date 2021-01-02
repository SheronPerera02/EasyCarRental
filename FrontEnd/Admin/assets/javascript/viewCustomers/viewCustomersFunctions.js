function loadAllCustomers() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/User/getAllUsers",
        success: function (resp) {
            $('#tblViewCustomers').empty();

            let verifyButton =
                "<button type='button' data-target='#idPhotoMdl' data-toggle='modal' class='btn btn-outline-primary' " +
                "onclick='loadIdPhoto($(this))'>View ID</button>";

            for (let customer of resp.data) {
                let row = "<tr>" +
                    "<td>" + customer.nic + "</td>" +
                    "<td>" + customer.email + "</td>" +
                    "<td>" + customer.password + "</td>" +
                    "<td>" + customer.address + "</td>" +
                    "<td>" + customer.contact + "</td>" +
                    "<td>" + verifyButton + "</td>" +
                    "</tr>";
                $('#tblViewCustomers').append(row);
            }
        }
    });
}

function loadIdPhoto(ref) {
    let nic = $(ref.parent().parent().children().get(0)).text();
    
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/EasyCar/api/v1/User/getIdPhoto/" + nic,
        success: function (resp) {
            $('#idPhotoMdl .modal-body').empty().append("<img style='height: 300px; width: 465px' src='../IdImages/" + resp.data + "'>");
        }
    });

}


$('#btnDone').on('click',function(){
    $('#idPhotoMdl .modal-body').empty();
});


