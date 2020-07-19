//部署时未开发注册方法
function register2() {
    swal('未开放，请联系作者获得测试账号', {
        icon: "error",
    });
}

//注册按钮onclick事件改为register()即可
function register() {
    var loginName = $("#loginName").val();
    if (!validPhoneNumber(loginName)) {
        swal('请输入正确的登录名(即手机号)', {
            icon: "error",
        });
        return false;
    }
    var password = $("#password").val();
    if (!validPassword(password)) {
        swal('请输入正确的密码格式(6-20位字符和数字组合)', {
            icon: "error",
        });
        return false;
    }
    var verifyCode = $("#verifyCode").val();
    if (!validLength(verifyCode, 7)) {
        swal('请输入正确的验证码', {
            icon: "error",
        });
        return false;
    }
    //验证
    var params = $("#registerForm").serialize();
    var url = '/register';
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        data: params,
        success: function (result) {
            if (result.resultCode == 200) {
                swal({
                    title: "注册成功",
                    text: "是否跳转至登录页?",
                    icon: "success",
                    buttons: true,
                    dangerMode: true,
                }).then((flag) => {
                    if (flag) {
                        window.location.href = '/login';
                    }
                }
            )
                ;
            } else {
                swal(result.message, {
                    icon: "error",
                });
            }
            ;
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
}