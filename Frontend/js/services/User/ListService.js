//Listar al cargar el documento por primera vez
$(document).ready(main);

//Buscar usuario por id Unico
$('#cargar').click(main);

//Busca usuarios checkeados, se comunica con el servidor para procesar los usuarios
$('#procesar').click(function() {  
    var ids = [];
    $("input:checkbox:checked").each(function() {
         ids.push($(this).val());     
    });
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/users/process",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(ids),
        success: function (data) {
            alert('Usuarios procesados con exito');
          main();
        },
        error: function (data) {
          alert(data.responseJSON.message);
          alert(data.responseJSON.action);
          main();
        },
      });
    console.log(JSON.stringify(ids));
  });


//funcion para listar los objetos JSON que retorna la Api Rest
function main() {
    var search = $("#search").val();
    if (search == "") {
      search = "all";
    }
    $.ajax({
      type: "GET",
      url: "http://localhost:8080/api/users/find/" + search,
      contentType: "application/json; charset=utf-8",
      success: function (data) {
        $("#table_users").empty();
        $("#table_users").append(
          "<thead>" +
            "<tr>" +
            '<th scope="col">Select</th>' +
            '<th scope="col">ID</th>' +
            '<th scope="col">Nombres</th>' +
            '<th scope="col">Apellidos</th>' +
            '<th scope="col">Procesado</th>' +
            "</tr>" +
            "</thead>"
        );
        for (var i in data) {
            if(!$.isEmptyObject(data)){
                if(data.hasOwnProperty(i)){
                    var process = "";
                    if(data[i].processed ==true){
                        process ="hidden";
                    }
                    else{
                        process = "checkbox"
                    }
                        $("#table_users").append(
                        "<tbody>" +
                          "<tr>" +
                          '<td><input type="'+process+'" value="'+data[i].id+'" /></td>'+
                          '<th scope="row"><h4>'+data[i].id+'</h4></th>' +
                          '<td><h4>'+data[i].name+'</h4></td>' +
                          '<td><h4>'+data[i].lastName+'</h4></td>' +
                          '<td><h4>'+data[i].processed+'</h4></td>' +
                          '</tr>' +
                          '</tbody>'
                      );
                }
            }
            else if($.isEmptyObject(data)){
                console.log('No se encontro ningun dato');
            }
        }
      },
      error: function (data) {
        alert(data.responseJSON.message);
        alert(data.responseJSON.action);
        console.log(data);
        window.location.reload();
      },
    });
  }