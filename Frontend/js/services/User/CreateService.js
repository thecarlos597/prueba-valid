//Agregar usuario (Envio Datos al backend Spring boot)
$("#nuevo").click(function () {
  var varName = $("#name").val();
  var varLastName = $("#last_name").val();
  var varProcessed = $("#processed").val();
  $.ajax({
    type: "POST",
    url: "http://localhost:8080/api/users/create",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify({
      name: varName,
      lastName: varLastName,
      processed: varProcessed,
    }),
    success: function (data) {
      alert(data.message);
      $('input[type="text"]').val('');
    },
    error: function (data) {
      alert(data.responseJSON.message);
      alert(data.responseJSON.action);
      console.log(data);
      $('input[type="text"]').val('');
    },
  });
});
