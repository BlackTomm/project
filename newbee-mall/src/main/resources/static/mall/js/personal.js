function openUpdateModal() {
    $('#personalInfoModal').modal('show');
}

//保存用户修改的个人信息
$('#saveButton').click(function(){
    var address = $("#address").val();
    if (address.trim().length < 10) {
        swal("请输入正确的收货信息", {
            icon: "error",
        });
        return;
    }
    var introduceSign = $("#introduceSign").val();
    if (introduceSign.trim().length < 4) {
        swal("请输入正确的个性签名", {
            icon: "error",
        });
        return;
    }
    var nickName = $("#nickName").val();
    if (nickName.trim().length < 2) {
        swal("请输入正确的昵称", {
            icon: "error",
        });
        return;
    }
    var userId = $("#userId").val();
    var data = {
        "userId": userId,
        "address": address,
        "introduceSign": introduceSign,
        "nickName": nickName
    };

    $.ajax({
        type:'POST',
        url: '/personal/updateInf',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            if (result.resultCode == 200) {
                $('#personalInfoModal').modal('hide');
                window.location.reload();
            } else {
                $('#personalInfoModal').modal('hide');
                alert(result.message);
            }
            ;
        },
        error: function () {
            alert('操作失败');
        }
    });
});