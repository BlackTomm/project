/*定义了 search() 方法的实现，在用户点击搜索按钮后会将用户输入的字段取出来并放入搜索请求
的路径中进行请求，同时也定义了输入框的 keypress() 事件，当用户在输入框中输入需要搜索的字
段后敲下 Enter 回车键，也可以发起搜索请求*/
$(function () {
    $('#keyword').keypress(function (e) {
        var key = e.which; //e.which是按键的值
        if (key == 13) {
            var q = $(this).val();
            if (q && q != '') {
                window.location.href = '/search?keyword=' + q;
            }
        }
    });
});

function search() {
    var q = $('#keyword').val();
    if (q && q != '') {
        window.location.href = '/search?keyword=' + q;
    }
}